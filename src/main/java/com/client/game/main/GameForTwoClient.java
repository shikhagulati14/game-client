package com.client.game.main;

import com.client.game.contant.GameConstants;
import com.client.game.exception.GameForTwoClientException;
import com.client.game.model.NextMove;
import com.client.game.model.Player;
import com.client.game.model.Result;
import com.client.game.state.GameState;
import com.client.game.utils.GameClientUtils;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class GameForTwoClient {

    GameState gameState = null;
    Player player = null;
    Player opponent = null;

    //starting point of a new game
    public void startGame() throws GameForTwoClientException {

        //Create Players profile
        player = GameClientUtils.createNewPlayerProfile();


        //find a game or wait for another player to join an existing game

        while(null == gameState){
            gameState = createOrJoinAGame(player);
        }

        //Couldn't find an opponent for the player
        if(gameState.getResult() == Result.PLAYER_WAITING_FOR_NEW_GAME){
            System.out.println("Unable to find a game/opponent. Exiting the current game... Please try again after sometime.");
            finishTheGame(gameState);
            System.exit(0);
        }


        //Player token can be 'X' or 'O'. Player 1 will be 'X'
        char playerToken = GameClientUtils.determinePlayerToken(player, gameState.getPlayerToMove());


        if(!(playerToken =='X')) {
            opponent = gameState.getPlayer2();
            System.out.println("Game found. You will be playing against "+ opponent.getPlayerName() +  " as Player 1");
            gameState = waitForOpponentToPlay(gameState, player.getPlayerId());
        }else{
            opponent = gameState.getPlayer1();
            System.out.println("Game found. You will be playing against "+ opponent.getPlayerName() + " as Player 2");
        }

        //Plays next Move until game is finishes i.e: either player quits/either players wins/ game is tied
        int movesCount = 1;
        int retryCountPerMove=1;
        while (!GameClientUtils.isGameFinished(gameState) && movesCount <= GameConstants.TOTAL_MOVES_ALLOWED_FOR_SINGLE_PLAYER) {

            //Paint the Game Board for next move
            GameClientUtils.paintTheGameBoard(gameState.getGameBoard());

            //Get the player Input for column value
            int column = GameClientUtils.getPlayerInput(player) -1;

            if(!makeNextMove(gameState, player, column, playerToken, GameConstants.RETRY_COUNT_MAKING_MOVE) ){
                if(retryCountPerMove <= GameConstants.RETRY_COUNT_MAKING_MOVE) {
                    retryCountPerMove++;
                    continue;
                }else{
                    boolean player1IsOpponent = opponent.getPlayerId().equalsIgnoreCase(gameState.getPlayer1().getPlayerId());
                    gameState.setResult(player1IsOpponent?Result.PLAYER_1_WIN:Result.PLAYER_2_WIN);
                    gameState.setPlayerWon(opponent);
                    gameState = finishTheGame(gameState);
                    break;
                }
            }
            if(GameClientUtils.isGameFinished(gameState)){
                finishTheGame(gameState);
                break;

            }

            gameState = waitForOpponentToPlay(gameState, player.getPlayerId());
            movesCount++;
        }

        gameState = finishTheGame(gameState);
        System.out.println("Game finished. Result: " + gameState.getResult());
    }

    /**
     * @return
     * @param gameState
     * @param playerId
     * @throws GameForTwoClientException
     */
    private GameState waitForOpponentToPlay(GameState gameState, String playerId) throws GameForTwoClientException {

        System.out.println("Waiting for opponent to make his move...");
        int retries = GameConstants.NO_OF_ATTEMPTS_FOR_NEXT_MOVE;
        do{

            try {
                Thread.sleep(GameConstants.DELAY_FOR_RETRIES.toMillis());
            } catch (InterruptedException e) {
               //ignore and try next time
            }

            String queryParameters = "?gameId=" + gameState.getGameId();
            String url = GameConstants.SERVER_URL + GameConstants.PLAYERS_TURN_URL + queryParameters;
            HttpGet httpGet = new HttpGet(url);
            try {
                gameState = GameClientUtils.getHttpResultFromGameServer(null, httpGet);
            } catch (GameForTwoClientException | IOException e) {
                System.out.println("Unable to get the Player's move from server. trying again.");
                retries++;
            }

        }while(null!=gameState && !gameState.getPlayerToMove().getPlayerId().equalsIgnoreCase(playerId) && --retries > 0);

        if(retries == 0){
            //opponent wins
            boolean player1IsOpponent = opponent.getPlayerId().equalsIgnoreCase(gameState.getPlayer1().getPlayerId());
            gameState.setResult(player1IsOpponent?Result.PLAYER_1_WIN:Result.PLAYER_2_WIN);
            gameState.setPlayerWon(opponent);
            gameState = finishTheGame(gameState);
        }
        return gameState;

    }

    /**
     * @param gameState
     * @param player
     * @param column
     * @param playerToken
     */
    private boolean makeNextMove(GameState gameState, Player player, int column, char playerToken, int retryCount)  {

        while(retryCount >0) {
            NextMove nextMove = new NextMove(gameState.getGameId(), player.getPlayerId(), column, playerToken);
            String url = GameConstants.SERVER_URL + GameConstants.PLAY_NEXT_MOVE_URL;
            HttpPost post = new HttpPost(url);
            try {
                String nextMoveJson = GameClientUtils.getGsonObject().toJson(nextMove);
                post.setEntity(new StringEntity(nextMoveJson));
                post.setHeader("Content-type", "application/json");
                GameState gameStateResponse = GameClientUtils.getHttpResultFromGameServer(post, null);
                if(null != gameStateResponse)
                {
                    gameState = gameStateResponse;
                    return true;
                }else
                {
                    System.out.println("Unable to register the Move with server. Trying again. ");
                    Thread.sleep(2000);
                    retryCount--;
                }

            }catch(GameForTwoClientException ge){
                if(ge.getMessage().contains("Invalid Move")){
                    System.out.println("Invalid Move. Column is already full. Please try again.");
                    return false;
                }

            }
            catch (IOException | InterruptedException e) {

                System.out.println("Unable to register the Move with server. Trying again. ");
                retryCount--;
            }
        }
        System.out.println("Server not responding");
        return false;

    }



    /**
     * @param gameState
     */
    public GameState finishTheGame(GameState gameState) throws GameForTwoClientException {

      String url = GameConstants.SERVER_URL+GameConstants.FINISH_GAME_URL;
      HttpPost post = new HttpPost(url);
        try {
            post.setEntity(new StringEntity(GameClientUtils.getGsonObject().toJson(gameState)));
            return GameClientUtils.getHttpResultFromGameServer(post, null);

        } catch (IOException e) {
            throw new GameForTwoClientException("Unable to finish the game.");
        }
    }

    /** Creates a new Game or Join a new Game
     * @param player
     * @return
     */
    private GameState createOrJoinAGame(Player player) {
        //Make Request to server with new Player profile
        GameState gameState=null;
        int retries = 0;
        System.out.println("Creating/finding new Game for : " + player.getPlayerName());
        do{
            System.out.print("..");
            try {
                gameState = createOrGetNewGameFromServer(player);
            } catch (GameForTwoClientException |IOException e) {
                System.out.println("Unable to get response from server. Trying again...");
                retries--;
            }
            waitForRetry();
            //check result for exiting game or Player being added to wait list Or Player is still in waiting mode
        }while((null != gameState && gameState.getResult() == Result.PLAYER_WAITING_FOR_NEW_GAME) && ++retries <= GameConstants.NO_OF_ATTEMPTS_FOR_NEW_GAME);
        return gameState;
    }

    private void waitForRetry() {
        try {
            Thread.sleep(GameConstants.DELAY_FOR_RETRIES.toMillis());
        } catch (InterruptedException e) {
            //ignore
        }
    }

    /**
     * @param player
     * @return
     */
    private GameState createOrGetNewGameFromServer(Player player) throws GameForTwoClientException, IOException {

        String queryParameters = "?playerName="+player.getPlayerName()+"&playerId="+player.getPlayerId();
        String url = GameConstants.SERVER_URL+GameConstants.CREATE_JOIN_GAME_URL+queryParameters;
        HttpGet httpGet = new HttpGet(url);
        GameState gameState = null;
        gameState = GameClientUtils.getHttpResultFromGameServer(null,httpGet);

        return gameState;
    }

    public GameState getGameState() {
        return gameState;
    }
}
