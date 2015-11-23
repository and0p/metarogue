package io.metarogue.game.scope.modifications;

import io.metarogue.game.gameworld.World;
import io.metarogue.game.scope.ScopeSquare;
import io.metarogue.util.math.Vector2d;
import io.metarogue.util.math.Vector3d;

import java.util.HashSet;

public class ScopeMove implements ScopeModification {

    int gameObjectID;
    ScopeSquare originalLocation;
    ScopeSquare newLocation;
    int world;

    public ScopeMove(int gameObjectID, Vector3d originalLocation, Vector3d newLocation, int viewDistance, int world) {
        this.gameObjectID = gameObjectID;
        this.originalLocation = new ScopeSquare(new Vector2d(originalLocation), viewDistance*2+1);
        this.newLocation = new ScopeSquare(new Vector2d(newLocation), viewDistance*2+1);
        this.world = world;
    }

    public HashSet<Integer> getNewIndexes() {
        return newLocation.getSubtraction(originalLocation);
    }

    public HashSet<Integer> getOldIndexes() {
        return originalLocation.getSubtraction(newLocation);
    }

    public int getWorld() { return world; }

    public int getGameObjectID() { return gameObjectID; }

}