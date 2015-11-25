package io.metarogue.game.scope.modifications;

import io.metarogue.game.gameworld.World;
import io.metarogue.game.scope.ScopeSquare;
import io.metarogue.util.math.Vector2d;
import io.metarogue.util.math.Vector4d;

import java.util.HashSet;

public class ScopeModification {

    int gameObjectID;
    Vector4d oldLocation;
    Vector4d newLocation;
    ScopeSquare oldLocationSquare;
    ScopeSquare newLocationSquare;

    public ScopeModification(int gameObjectID, Vector4d oldLocation, Vector4d newLocation) {
        this.gameObjectID = gameObjectID;
        this.oldLocation = oldLocation;
        this.newLocation = newLocation;
        oldLocationSquare = new ScopeSquare(new Vector2d(oldLocation.getVector3d().getX(), oldLocation.getVector3d().getZ()));
        newLocationSquare = new ScopeSquare(new Vector2d(newLocation.getVector3d().getX(), newLocation.getVector3d().getZ()));
    }

    int getGameObjectID() {
        return gameObjectID;
    }

    int getOldWorld() {
        return oldLocation.getWorld();
    }

    int getNewWorld() {
        return newLocation.getWorld();
    }

    HashSet<Integer> getNewIndexes() {
        HashSet<Integer> hs = new HashSet<Integer>();
        return hs;
    }

    HashSet<Integer> getOldIndexes() {
        HashSet<Integer> hs = new HashSet<Integer>();
        return hs;
    }

    public boolean isWorldDifferent() {
        if(oldLocation.getWorld() != newLocation.getWorld()) {
            return true;
        } else {
            return false;
        }
    }

}
