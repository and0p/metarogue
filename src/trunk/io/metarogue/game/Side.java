package io.metarogue.game;

import io.metarogue.game.gameobjects.GameObject;

import java.util.ArrayList;

/**
 * Side for parties and objects to be on. Most simply in a single-player roguelike the player is on side 0 and
 * all enemies are on side 1. If the player got a pet it would be side 0. Each side gets a subturn.
 * Currently assuming all sides will be declared at game beginning and cannot be modified.
 */

public class Side {

    int id;
    String name;

    ArrayList<GameObject> gameObjects;
    ArrayList<Party> parties;

    public Side(int id, String name) {
        this.id = id;
        this.name = name;
        gameObjects = new ArrayList<GameObject>();
        parties = new ArrayList<Party>();
    }

    public void addObject(GameObject go) {

    }

    public GameObject getObject(int i) {
        if(i >= 0 && i < gameObjects.size()) {
            GameObject go = gameObjects.get(i);
            if(go != null) return go;
        }
        return null;
    }

    public ArrayList<GameObject> getObjects() {
        return gameObjects;
    }

}