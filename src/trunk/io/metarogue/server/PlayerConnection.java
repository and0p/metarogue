package io.metarogue.server;

import com.esotericsoftware.kryonet.Connection;
import io.metarogue.game.gameobjects.GameObject;
import org.lwjgl.Sys;

import java.util.ArrayList;
import java.util.Date;

public class PlayerConnection extends Connection {

    int id;
    String name;
    Date timeOfConnection;

    // If player is registered with the server with a proper user/pass
    boolean registered = false;

    int timeConnected;

    // Number of possibly exploitive messages?
    int badMessages = 0;

    ArrayList<GameObject> playerOwnedObjects;
    ArrayList<GameObject> playerControlledObjects;

    public PlayerConnection() {
        timeOfConnection = new Date();
        timeOfConnection.setTime(Sys.getTime());
    }

    public void setName(String name) {
        this.name = name;
    }

}