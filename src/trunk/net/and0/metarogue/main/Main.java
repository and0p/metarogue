package net.and0.metarogue.main;

import java.io.IOException;

import net.and0.metarogue.model.Game;
import net.and0.metarogue.view.OpenGLRenderer;
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
    // For now, storing reference to window / renderer here
    static OpenGLRenderer renderer;

    // Temp init logic
    static void temptInit() {
    }

 public static void main(String[] args) throws IOException {
		// Initialization:
        gameClient = new GameClient();
        game = new Game("Test");
        game.loadLocalTextures(); // Test function for now
		temptInit();

        // Game loop
		while(!org.lwjgl.opengl.Display.isCloseRequested()) {
            gameClient.update();
		}

        // Cleanup
        game.close();
        renderer.close();
	}

}