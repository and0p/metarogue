package io.metarogue.client;

import io.metarogue.client.util.InputParser;
import io.metarogue.game.Camera;
import io.metarogue.game.Game;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.game.gameworld.World;
import io.metarogue.client.view.threed.Vector3d;
import io.metarogue.client.view.GUI.GUIElement;
import io.metarogue.client.view.OpenGLRenderer;

public class GameClient {

    NetworkClient networkClient;
    boolean connected = false;
    boolean local = true;

    // Reference to local game
    Game game;
    // Reference to active world
    World activeWorld;

    // OpenGL renderer
    public static OpenGLRenderer renderer;
    // Current camera
    Camera currentCamera;

    // Input
    InputParser inputParser;

    public static GUIElement selectedGUIElement = null;
    public static GUIElement highlightedGUIElement = null;
    public static GameObject selectedGameObject = null;
    public static GameObject highlightedGameObject = null;
    public static GameObject playerGameObject = null;
    public static Vector3d selectedBlock;

    public GameClient() {
        renderer = new OpenGLRenderer();    // Create create OpenGL context and renderer
        inputParser = new InputParser();
    }

    public void bindGame(Game g) {
        game = g;
    }

    public World getActiveWorld() { return activeWorld; }

    public void bindRenderer(OpenGLRenderer r) {
        this.renderer = r;
    }

    public void update() {
        if (game != null) {
            InputParser.parseInput();
            if(playerGameObject != null) {
                game.camera.setTargetAndUpdate(playerGameObject.getPosition().toFloat());
            }
            //getActiveWorld().playerObject.hasChangedChunks = false;
            getActiveWorld().chunkChanges = false;
            // Start rendering
            renderer.preRender();
            if(activeWorld != null) {
                renderer.update();
                renderer.renderWorld();
            }
            renderer.renderGUI();
        }
    }

    public Camera getCurrentCamera() {
        return currentCamera;
    }
    public OpenGLRenderer getRenderer() { return renderer; }
    public Game getGame() { return game; }
    public static GameObject getPlayer() { return playerGameObject; }

}