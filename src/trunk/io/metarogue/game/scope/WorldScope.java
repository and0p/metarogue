package io.metarogue.game.scope;

import io.metarogue.game.gameobjects.GameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class WorldScope {

    // X,Z coordinates translated to since integer through MortonCurve
    HashSet<Integer> scope;

    public WorldScope() {
        scope = new HashSet<Integer>();
    }

    public void refresh(ArrayList<GameObject> activeObjects) {
        // Change scope based on all GameObjects passed
    }

    public void refresh(HashMap<Integer, GameObject> activeObjects) {
        // Change scope based on all GameObjects passed
    }

    // Update based on possibly new areas and possibly old areas, estimated from GameObject movement
    public void update(HashSet<Integer> newstuff, HashSet<Integer> oldstuff) {

    }

}