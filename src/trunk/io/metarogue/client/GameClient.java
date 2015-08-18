package io.metarogue.client;

import io.metarogue.Main;
import io.metarogue.client.listener.ClientListener;
import io.metarogue.client.listener.ClientNetworkListener;
import io.metarogue.client.util.InputParser;
import io.metarogue.game.Camera;
import io.metarogue.game.Game;
import io.metarogue.game.Player;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.game.gameworld.World;
import io.metarogue.client.view.threed.Vector3d;
import io.metarogue.client.view.GUI.GUIElement;
import io.metarogue.client.view.ClientRenderer;
import io.metarogue.util.Log;
import io.metarogue.util.messagesystem.MessagePump;
import io.metarogue.util.messagesystem.message.chat.ChatMessage;
import io.metarogue.util.network.Network;
import io.metarogue.util.messagesystem.message.Message;

import java.util.ArrayList;

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

    // Player that client owns / is associated to
    Player player;
    // Client id, server is always ID 0 so 0 can be treated as null in client context
    int clientID = 0;
    // Messages sent that require server confirmation (and therefore a unique id)
    int confirmableMessages = 0;

    // Message system
    // ArrayList<Message> remoteMessages;
    MessagePump messagePump;

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
        messagePump = new MessagePump();
        messagePump.register(new ClientListener());
        messagePump.register(new ClientNetworkListener());
        // remoteMessages = new ArrayList<Message>();
    }

    public void connect(String ip) {
        // TODO: Make this mess all included with the connection object
        connection = new ClientNetwork(54555, 53777);
        Network.register(connection);
        connected = connection.connect(ip);
        Log.log("Connected to " + ip);
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
            connection.update();
            connection.sendMessagesToClient();
        }
        inputParser.parseInput();
        messagePump.dispatch();
        if (Main.getGame() != null) {
            checkWorld();
            if(activeWorld != null) {
                activeWorld.chunkChanges = false;
            }
            if(playerGameObject != null) {
                camera.setTargetAndUpdate(playerGameObject.getDisplayPosition());
            }
            renderer.render();
        } else {
            renderer.updateWindow();
        }
        if(connected) {
            connection.sendAll();
        }
        if(org.lwjgl.opengl.Display.isCloseRequested()) {
            Main.requestClose();
        }
    }

    public void addMessage(Message m) {
        m.setSender(clientID);
        messagePump.addMessage(m);
    }

//    public void addRemoteMessage(Message m) {
//        remoteMessages.add(m);
//    }

    public void close() {
        renderer.close();
    }

    // Debug message sender
    public void sendMessage(Message m) {
        connection.addMessage(m);
    }

    public void setClientID(int i) {
        clientID = i;
    }
    public int getClientID() {
        return clientID;
    }

    public Camera getCurrentCamera() {
        return currentCamera;
    }
    public ClientRenderer getRenderer() { return renderer; }
    public static void setPlayer(GameObject o) { playerGameObject = o; }
    public static GameObject getPlayerObject() { return playerGameObject; }
    public World getActiveWorld() { return activeWorld; }
    public void setActiveWorld(World activeWorld) { this.activeWorld = activeWorld; }
    public void setPlayer(int id) {
        player = Main.getGame().getPlayers().get(id);
    }
    public Player getPlayer() {
        return player;
    }

}