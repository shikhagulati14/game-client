package com.client.game.exception;

public class GameForTwoClientException extends Exception{

    public GameForTwoClientException() {
        super();
    }

    public GameForTwoClientException(String message) {
        super(message);
    }

    public GameForTwoClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public GameForTwoClientException(Throwable cause) {
        super(cause);
    }

    protected GameForTwoClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
