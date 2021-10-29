package com.client.game;


import com.client.game.exception.GameForTwoClientException;
import com.client.game.main.GameForTwoClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GameClientApplication {

	private static GameForTwoClient gameClient;
	public static void main(String[] args) {
		SpringApplication.run(GameClientApplication.class, args);
		Runtime r=Runtime.getRuntime();
		r.addShutdownHook(new ShutDownHookThread());
		try {
			gameClient = new GameForTwoClient();
			gameClient.startGame();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
	}
	
	static class ShutDownHookThread extends Thread{
		public void run(){
			try {
				gameClient.getGameState().setPlayerWon(gameClient.getGameState().getPlayer2());
				gameClient.finishTheGame(gameClient.getGameState());
			} catch (GameForTwoClientException e) {
				e.printStackTrace();
			}
		}
	}

}
