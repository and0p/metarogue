package io.metarogue.game.gameworld;

import io.metarogue.game.gameobjects.GameObject;

import java.util.HashMap;

/**
 * World class
 *
 * <p>This lovely thing just hold GameObjects that are entering worlds, for Worlds that haven't been loaded yet
 *
 * @author and0
 */
public class PartialWorld {

    int id;

    public HashMap<Integer, GameObject> gameObjects;
    public HashMap<Integer, GameObject> activeObjects;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public HashMap<Integer, GameObject> getGameObjects() {
        return gameObjects;
    }

    public void setGameObjects(HashMap<Integer, GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }

    public HashMap<Integer, GameObject> getActiveObjects() {
        return activeObjects;
    }

    public void setActiveObjects(HashMap<Integer, GameObject> activeObjects) {
        this.activeObjects = activeObjects;
    }

    public void addActiveObject(GameObject go) {
        activeObjects.put(go.getID(), go);
    }

}
