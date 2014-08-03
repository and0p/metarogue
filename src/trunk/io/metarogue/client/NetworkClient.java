package io.metarogue.client;

import io.metarogue.game.Camera;
import io.metarogue.game.Game;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.game.gameworld.World;
import io.metarogue.util.network.Network;
import io.metarogue.util.network.TextMessage;
import io.metarogue.client.view.threed.Vector3d;
import io.metarogue.client.view.GUI.GUIElement;
import io.metarogue.client.view.OpenGLRenderer;
import io.metarogue.client.view.TextureList;
import org.newdawn.slick.opengl.Texture;

import java.io.IOException;

public class NetworkClient {

    com.esotericsoftware.kryonet.Client client;

    boolean connected = false;
    boolean local = true;

    // Reference to local game
    Game game;
    // Reference to active world
    World activeWorld;

    // OpenGL renderer
    public static OpenGLRenderer renderer;

    // List of textures
    TextureList textureList;
    Texture guiTexture;
    Texture worldTexture;

    // Camera for gameClient
    public static Camera camera = new Camera(10,0,0,0);

    public static GUIElement selectedGUIElement = null;
    public static GUIElement highlightedGUIElement = null;
    public static GameObject selectedGameObject = null;
    public static GameObject highlightedGameObject = null;
    public static GameObject playerGameObject = null;
    public static Vector3d selectedBlock;

    public NetworkClient(boolean local) {
        this.local = local;
        renderer = new OpenGLRenderer();    // Create create OpenGL context and renderer
        if(!local) {
            startNetworking();
        }
    }

    public void startNetworking() {
        client = new com.esotericsoftware.kryonet.Client();
        Network.register(client);
        client.start();
    }

    public void connect(String ip, int tcpport, int udpport) {
        try {
            client.connect(5000, ip, tcpport, udpport);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void message(){
        if(!connected) {
            TextMessage request = new TextMessage();
            request.text = "Here is the request";
            client.sendTCP(request);
        }
        connected = true;
    }

    public void assignRenderer(OpenGLRenderer r) {
        this.renderer = r;
    }

}
