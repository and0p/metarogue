package io.metarogue.client;

import io.metarogue.Main;
import io.metarogue.client.util.InputParser;
import io.metarogue.game.Camera;
import io.metarogue.game.Game;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.game.gameworld.World;
import io.metarogue.client.view.threed.Vector3d;
import io.metarogue.client.view.GUI.GUIElement;
import io.metarogue.client.view.ClientRenderer;
import io.metarogue.util.Log;
import io.metarogue.util.network.Network;
import io.metarogue.util.network.message.NetworkMessage;

import java.io.IOException;

public class GameClient {

    ClientNetwork connection;
    boolean connected = false;

    // Reference to local game
    Game game;

    // Reference to active world
    World activeWorld;
    World previousWorld;

    // OpenGL renderer
    public ClientRenderer renderer;
    // Camera
    public Camera camera;
    // Current camera
    Camera currentCamera;

    // Input
    InputParser inputParser;

    // Player name
    String playerName;

    public static GUIElement selectedGUIElement;
    public static GUIElement highlightedGUIElement;
    public static GameObject selectedGameObject;
    public static GameObject highlightedGameObject;
    public static GameObject playerGameObject;
    public static Vector3d selectedBlock;

    public GameClient() {
        renderer = new ClientRenderer();    // Create create OpenGL context and renderer
        camera = new Camera(10,0,0,0);
        currentCamera = camera;
        inputParser = new InputParser();
        playerName = "and0";
    }

    public void connect(String ip) {
        // TODO: Make this mess all included with the connection object
        connection = new ClientNetwork(54555, 53777);
        Network.register(connection);
        try {
            connection.connect(5000, ip, 54555, 53777);
            connected = true;
            Log.log("Connected to " + ip);
        } catch (IOException e) {
            e.printStackTrace();
            Log.log("Connection failed");
        }
    }

    // Return true if active world is different
    public boolean checkWorld() {
        if(activeWorld != previousWorld) {
            previousWorld = activeWorld;
            if(activeWorld != null) {
                renderer.bindWorld(activeWorld);
            }
            return true;
        }
        return false;
    }

    public void update() {
        if(connected) {
            connection.sendAll();
        }
        if (Main.getGame() != null) {
            InputParser.parseInput();
            checkWorld();
            if(activeWorld != null) {
                activeWorld.chunkChanges = false;
            }
            if(playerGameObject != null) {
                camera.setTargetAndUpdate(playerGameObject.getDisplayPosition());
            }
            renderer.render();
        } else {
            inputParser.parseInput();
            renderer.updateWindow();
        }
        if(org.lwjgl.opengl.Display.isCloseRequested()) {
            Main.requestClose();
        }
    }

    public void close() {
        renderer.close();
    }

    // Debug message sender
    public void sendMessage(NetworkMessage m) {
        connection.addMessage(m);
    }

    public Camera getCurrentCamera() {
        return currentCamera;
    }
    public ClientRenderer getRenderer() { return renderer; }
    public static void setPlayer(GameObject o) { playerGameObject = o; }
    public static GameObject getPlayer() { return playerGameObject; }
    public World getActiveWorld() { return activeWorld; }
    public void setActiveWorld(World activeWorld) { this.activeWorld = activeWorld; }

}