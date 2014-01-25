package net.and0.metarogue.model;

import net.and0.metarogue.model.gameobjects.GameObject;

import java.util.ArrayList;

/**
 * MetaRogue class
 * User: andrew
 * Date: 1/24/14
 * Time: 7:24 PM
 */
public class Party {

    String name;
    ArrayList<GameObject> objects;

    public Party() {
        objects = new ArrayList<GameObject>();
    }

    public Party(String name) {
        objects = new ArrayList<GameObject>();
        this.name = name;
    }

    public void addObject(GameObject o) {
        objects.add(o);
    }

}
