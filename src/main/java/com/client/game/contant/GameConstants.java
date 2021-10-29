package com.client.game.contant;

import java.time.Duration;

public class GameConstants {

    //game Constants
    public static final int NO_OF_ATTEMPTS_FOR_NEW_GAME = 10;
    public static final Duration DELAY_FOR_RETRIES = Duration.ofSeconds(5);
    public static final int NO_OF_ATTEMPTS_FOR_NEXT_MOVE = 10;

    //Apis Urls
    public static final String SERVER_URL = "HTTP://localhost:8080/games/gamefortwo";
    //FIND GAME
    public static final String CREATE_JOIN_GAME_URL = "/findGame";
    //PLAYERS TURN
    public static final String PLAYERS_TURN_URL = "/playersTurn";
    // PLAY NEXT MOVE
    public static final String PLAY_NEXT_MOVE_URL = "/makeMove";
    // FINISH THE GAME
    public static final String FINISH_GAME_URL = "/finishGame";

    public static final char EMPTY_CHAR = ' ';
    public static final int TOTAL_MOVES_ALLOWED_FOR_SINGLE_PLAYER = 27;
    public static final int RETRY_COUNT_MAKING_MOVE = 3;
}
