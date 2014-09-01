package io.metarogue;

import java.io.IOException;

import io.metarogue.client.GameClient;
import io.metarogue.client.view.threed.Vector3d;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.server.GameServer;
import io.metarogue.game.Game;
import io.metarogue.client.view.OpenGLRenderer;
import org.lwjgl.*;

public class Main {
	
    public static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    // Game gameClient and servers
    public static GameServer server;
    public static GameClient gameClient;
    // Game model
    public static Game game;

    // Temp init logic
    static void temptInit() {
    }

 public static void main(String[] args) throws IOException {
		// Initialization:
        gameClient = new GameClient();
        game = new Game("Test");
        game.setDefaultWorld(game.newWorld());
        // Some init logic...
        game.getDefaultWorld().fillArea(1, new Vector3d(5,5,5), new Vector3d(5,5,5));
        GameObject player = new GameObject(new Vector3d(10,10,10), "Soldier");
        game.getDefaultWorld().addPlayerObject(player);
        game.loadLocalTextures();
        // Attach game to client
        gameClient.bindGame(game);
        gameClient.bindWorld(game.getDefaultWorld());
        gameClient.setPlayer(player);
        game.loadLocalTextures(); // Test function for now
		temptInit();

        // Game loop
		while(!org.lwjgl.opengl.Display.isCloseRequested()) {
            gameClient.update();
		}

        // Cleanup
        game.close();
	}

}