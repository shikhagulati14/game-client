/**
 * 
 */
package com.client.game.model;


public final class NextMove {
	
	String gameId;
    String playerId;
    int columnId;
	char playerToken;

	/**
	 * @param gameId
	 * @param playerId
	 * @param columnId
	 * @param playerToken
	 */
	public NextMove(String gameId, String playerId, int columnId, char playerToken) {
		this.gameId = gameId;
		this.playerId = playerId;
		this.columnId = columnId;
		this.playerToken = playerToken;
	}


	public final String getGameId() {
		return gameId;
	}
	public final void setGameId(String gameId) {
		this.gameId = gameId;
	}
	public final String getPlayerId() {
		return playerId;
	}
	public final void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	public final int getColumnId() {
		return columnId;
	}
	public final void setColumnId(int columnId) {
		this.columnId = columnId;
	}

	public char getPlayerToken() {
		return playerToken;
	}

	public void setPlayerToken(char playerToken) {
		this.playerToken = playerToken;
	}

	@Override
	public String toString() {
		return "NextMove{" +
				"gameId='" + gameId + '\'' +
				", playerId='" + playerId + '\'' +
				", columnId=" + columnId +
				", playerToken=" + playerToken +
				'}';
	}
}
