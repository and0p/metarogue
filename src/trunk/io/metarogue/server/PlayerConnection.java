package io.metarogue.server;

import com.esotericsoftware.kryonet.Connection;
import io.metarogue.game.gameobjects.GameObject;

import java.util.ArrayList;

public class PlayerConnection extends Connection {

    int id;
    String name;

    String ip;
    int port;

    int timeConnected;

    ArrayList<GameObject> playerObjects;

    public PlayerConnection() {
        // Auto-generated constructor
    }

}