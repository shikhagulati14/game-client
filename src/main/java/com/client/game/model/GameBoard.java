package com.client.game.model;


import com.client.game.exception.GameForTwoClientException;

import java.util.ArrayList;

public class GameBoard {

    //game board define
    ArrayList<StringBuilder> board = new ArrayList<>();

    public void nextMove(Player player, int column) throws GameForTwoClientException {
        //check for invalid move ie. column is full
    }

    public GameBoard() {
        initializeGameBoard();
    }

    private void initializeGameBoard() {

        for(int column = 0; column<9 ; column++){
            StringBuilder row = new StringBuilder("______");
            board.add(row);
        }
    }

    public ArrayList<StringBuilder> getBoard() {
        return board;
    }

    public void setBoard(ArrayList<StringBuilder> board) {
        this.board = board;
    }

}
