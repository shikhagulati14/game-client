package com.client.game.utils;

import com.client.game.exception.GameForTwoClientException;
import com.client.game.model.GameBoard;
import com.client.game.model.Player;
import com.client.game.model.Result;
import com.client.game.state.GameState;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

public class GameClientUtils {

    private static final GsonBuilder builder;
    private static final Gson gson;

    static {
        builder = new GsonBuilder();
        gson = builder.serializeNulls().setPrettyPrinting().create();
    }


    //Creates new Player for starting the game
    public static Player createNewPlayerProfile() {
        Scanner myObj = new Scanner(System.in);
        System.out.println("Enter Player Name: ");
        String playerName = myObj.nextLine();
        String playerId = UUID.randomUUID().toString();
        return new Player(playerId,playerName);

    }

    public static boolean isGameFinished(GameState gameState) {
        return(gameState.getResult()== Result.PLAYER_1_WIN
                || gameState.getResult() == Result.PLAYER_2_WIN
                || gameState.getResult() == Result.TIED);
    }

    public static Gson getGsonObject(){
        return gson;
    }

    /**
     * @param entity
     * @return
     */
    public static GameState parseResponseToGameState(String entity) {
        return getGsonObject().fromJson(entity, GameState.class);
    }

    /**
     * get Http Response from game server
     * @param postRequest
     * @param httpGet
     * @return
     */
    public static GameState getHttpResultFromGameServer(HttpPost postRequest, HttpGet httpGet) throws GameForTwoClientException, IOException {

        try (final CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = null!=postRequest?httpClient.execute(postRequest):httpClient.execute(httpGet)) {

            HttpEntity entity = response.getEntity();

            if(response.getStatusLine().getStatusCode() == 200) {
                if (entity != null) {
                    return GameClientUtils.parseResponseToGameState(EntityUtils.toString(entity));
                } else {
                    return null;
                }
            }else{
                throw new GameForTwoClientException(EntityUtils.toString(response.getEntity()));
            }
        }
    }

    /**
     * @param gameBoard
     */
    public static void paintTheGameBoard(GameBoard gameBoard) {
        ArrayList<StringBuilder> board = gameBoard.getBoard();

        for(int row = 0; row<6 ; row++){
            for(int column = 0 ; column<9;column++){
                char tokenToPrint = board.get(row).charAt(column);
                if(tokenToPrint != '_') {
                    System.out.print("[" + tokenToPrint + "] ");
                }else{
                    System.out.print("[ ] ");
                }
            }
            System.out.println("\n");
        }

    }

    /**
     * @param player
     * @param playerToMove
     * @return
     */
    public static char determinePlayerToken(Player player, Player playerToMove) {
        if(player.getPlayerId().equalsIgnoreCase(playerToMove.getPlayerId())){
            //This player is Deemed Player 1
            return 'X';
        }else{
            //player 2
            return 'O';
        }
    }

    /**
     * @param player
     * @return
     */
    public static int getPlayerInput(Player player) {
        try {
            Scanner getPlayerInput = new Scanner(System.in);
            System.out.println("It's your turn "+player.getPlayerName()+ ", Please Enter column(1-9): ");
            int column = getPlayerInput.nextInt();
            if(column < 1 || column > 9){
                System.out.println("Invalid Input. Please enter value between(1-9).");
                return getPlayerInput(player);
            }
            return column;
        } catch (Exception e) {
            System.out.println("Invalid Input. Please enter a number.");
            return getPlayerInput(player);
        }
    }
}
