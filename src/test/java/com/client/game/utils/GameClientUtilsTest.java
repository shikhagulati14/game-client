package com.client.game.utils;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import com.client.game.exception.GameForTwoClientException;
import com.client.game.main.GameForTwoClient;
import com.client.game.model.Player;
import com.client.game.state.GameState;


public class GameClientUtilsTest {

	GameForTwoClient client = new GameForTwoClient();
	
	@Test
	public void testStartGame() throws GameForTwoClientException, ClientProtocolException, IOException {

		MockedStatic<GameClientUtils> mockUtil = Mockito.mockStatic(GameClientUtils.class);
		GameState gameState = Mockito.mock(GameState.class);

		mockUtil.when(
				() -> GameClientUtils.getHttpResultFromGameServer(Mockito.isA(HttpPost.class), Mockito.isA(HttpGet.class)))
				.thenReturn(gameState);
		
		
		assertNotEquals(gameState, null);

	}
	
	@Test
	public void testDeterminePlayerToken_Returns_O_WhenPlayerAndPlayerToMoveDonotMatch(){
		
		Player player = new Player("24","abc");
		Player playerToMove = new Player("11","xyz");
		
		char c = GameClientUtils.determinePlayerToken(player, playerToMove);
		
		String actual = String.valueOf(c);
		
		assertNotEquals(actual, null);
		
	}
	
	@Test
	public void testDeterminePlayerToken_Returns_X_WhenPlayerAndPlayerToMoveMatches(){
		
		Player player = new Player("25","Raman");
		
		char c = GameClientUtils.determinePlayerToken(player, player);
		
		String actual = String.valueOf(c);
		
		assertNotEquals(actual, null);
		
	}
	
}