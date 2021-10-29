package com.client.game.state;


import com.client.game.model.GameBoard;
import com.client.game.model.Player;
import com.client.game.model.Result;

public class GameState {

    private String gameId;
    private GameBoard gameBoard;

    private Player player1, player2, playerToMove, playerWon=null;

    private Result result;

    public GameState(Player player1, Player player2, Player playerToMove, String gameId) {

        this.player1 = player1;
        this.player2 = player2;
        this.playerToMove = playerToMove;
        this.gameId = gameId;
    }

    //this to used for entity to json conversion and vice versa
    public GameState(String gameId, GameBoard gameBoard, Player player1, Player player2, Player playerToMove, Result result) {
        this.gameId = gameId;
        this.gameBoard = gameBoard;
        this.player1 = player1;
        this.player2 = player2;
        this.playerToMove = playerToMove;
        this.result = result;
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public Player getPlayer1() {
        return player1;
    }

    public void setPlayer1(Player player1) {
        this.player1 = player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setPlayer2(Player player2) {
        this.player2 = player2;
    }

    public Player getPlayerToMove() {
        return playerToMove;
    }

    public void setPlayerToMove(Player playerToMove) {
        this.playerToMove = playerToMove;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result playerWaitingForNewGame) {
        result = playerWaitingForNewGame;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public Player getPlayerWon() {
        return playerWon;
    }

    public void setPlayerWon(Player playerWon) {
        this.playerWon = playerWon;
    }
}
