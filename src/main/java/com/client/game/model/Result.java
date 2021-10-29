package com.client.game.model;

public enum Result {
	PLAYER_WAITING_FOR_NEW_GAME("Player is waiting for new game."),
    PLAYER_1_WIN("Player 1 Wins."),
    PLAYER_2_WIN("Player 2 Wins."),
    TIED("Game is Tied."),
    PLAY_NEXT_MOVE("Play the next move."),
    ALREADY_PARTICIPATING_IN_GAME("Player already participating in the game.");

    public final String description;

    private Result(String description) {
        this.description = description;
    }


}
