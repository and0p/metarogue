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

    public transient HashMap<Integer, GameObject> gameObjects;
    public transient HashMap<Integer, GameObject> activeObjects;

}
