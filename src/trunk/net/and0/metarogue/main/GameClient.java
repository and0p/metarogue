package net.and0.metarogue.main;

import net.and0.metarogue.controller.InputParser;
import net.and0.metarogue.controller.network.NetworkClient;
import net.and0.metarogue.model.Camera;
import net.and0.metarogue.model.Game;
import net.and0.metarogue.model.gameobjects.GameObject;
import net.and0.metarogue.model.gameworld.World;
import net.and0.metarogue.util.threed.Vector3d;
import net.and0.metarogue.view.GUI.GUIElement;
import net.and0.metarogue.view.OpenGLRenderer;

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