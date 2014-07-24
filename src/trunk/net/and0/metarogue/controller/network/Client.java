package net.and0.metarogue.controller.network;

import net.and0.metarogue.model.Camera;
import net.and0.metarogue.model.Game;
import net.and0.metarogue.model.gameobjects.GameObject;
import net.and0.metarogue.util.threed.Vector3d;
import net.and0.metarogue.view.GUI.GUIElement;
import net.and0.metarogue.view.OpenGLRenderer;
import net.and0.metarogue.view.TextureList;
import org.newdawn.slick.opengl.Texture;

import java.io.IOException;

public class Client {

    com.esotericsoftware.kryonet.Client client;

    boolean connected = false;
    boolean local = true;

    // Reference to local game
    Game game;

    // OpenGL renderer
    public static OpenGLRenderer renderer;

    // List of textures
    TextureList textureList;
    Texture guiTexture;
    Texture worldTexture;

    // Camera for client
    public static Camera camera = new Camera(10,0,0,0);

    public static GUIElement selectedGUIElement = null;
    public static GUIElement highlightedGUIElement = null;
    public static GameObject selectedGameObject = null;
    public static GameObject highlightedGameObject = null;
    public static GameObject playerGameObject = null;
    public static Vector3d selectedBlock;

    public Client(boolean local) {
        this.local = local;
        if(!local) {
            client = new com.esotericsoftware.kryonet.Client();
            Network.register(client);
        }
    }

    public void start() {
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

}
