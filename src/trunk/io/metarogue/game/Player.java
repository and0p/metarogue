package io.metarogue.game;

import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.game.gamemessage.player.PlayerSkeleton;

import java.util.ArrayList;

public class Player {

    int id;
    String nickname;

    long timeOfConnection;

    // If player is registered with the server with a proper user/pass
    boolean registered = false;

    int ping;

    ArrayList<GameObject> playerOwnedObjects;
    ArrayList<GameObject> playerControlledObjects;

    public Player(int id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public Player(PlayerSkeleton s) {
        new Player(s.id, s.nickname);
        registered = s.registered;
        timeOfConnection = s.timeOfConnection;
    }

    public void setNickName(String n) {
        nickname = n;
    }
    public String getNickName() {
        return nickname;
    }
    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public PlayerSkeleton getSkeleton() {
        PlayerSkeleton s = new PlayerSkeleton();
        s.id = id;
        s.nickname = nickname;
        s.timeOfConnection = timeOfConnection;
        return s;
    }

}