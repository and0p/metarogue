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

    public NetworkClient(boolean local) {
        this.local = local;
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

}
