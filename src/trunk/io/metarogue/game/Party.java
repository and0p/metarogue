package io.metarogue.game;

import io.metarogue.game.gameobjects.GameObject;

import java.util.ArrayList;

/**
 * MetaRogue class
 * User: andrew
 * Date: 1/24/14
 * time: 7:24 PM
 */
public class Party {

    String name;
    ArrayList<GameObject> objects;

    public Party() {
        objects = new ArrayList<GameObject>();
    }

    public Party(String name) {
        this.name = name;
        objects = new ArrayList<GameObject>();
    }

    public void addObject(GameObject o) {
        objects.add(o);
    }

}
