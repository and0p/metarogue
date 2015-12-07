package io.metarogue.game.scope;

import io.metarogue.Main;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.game.gameworld.World;
import io.metarogue.game.scope.modifications.ScopeModification;
import io.metarogue.util.math.MortonCurve;
import io.metarogue.util.math.Vector2d;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class WorldScope implements Scope {

    // World this scope pertains to
    int worldID;
    // Set of X,Z coordinates translated to single integer via MortonCurve
    HashSet<Integer> currentScope;
    // Stuff to add
    HashSet<Integer> toAdd;
    // Stuff to remove
    HashSet<Integer> toRemove;

    public WorldScope(int worldID) {
        this.worldID = worldID;
        currentScope = new HashSet<Integer>();
        toAdd = new HashSet<Integer>();
        toRemove = new HashSet<Integer>();
    }

    public boolean contains(int i) {
        return true;
    }

    public void addNew(int i) {
        toAdd.add(i);
    }

    public void addNew(HashSet<Integer> hs) {
        toAdd.addAll(hs);
    }

    public void addOld(int i) {
        toRemove.add(i);
    }

    public void addOld(HashSet<Integer> hs) {
        toRemove.addAll(hs);
    }

    // Update based on possibly new areas and possibly old areas, estimated from GameObject movement
    public void update(ScopeConfiguration sc) {
        // Check that there are changes
        if(toAdd.size() > 0 || toRemove.size() > 0) {
            // Delete all possible new additions from possible old as they are redundant.
            toRemove.removeAll(toAdd);
            // Remove all currently loaded elements from the list of possibly new chunks, as they're already loaded
            toAdd.removeAll(currentScope);
            // Also check that there are no parts of the old list still in use by current GameObjects
            // But only if world exists
            World w = Main.getGame().getWorld(worldID);
            if(w != null) {
                // TODO: Technically a memory leak if this updates twice before world is loaded? Old stuff won't get removed.
                removeRelevantChunksFromUnloadQueue(sc, w);
            }
        }
    }

    // See if any z-index location intersects with any GameObject's current field of view, if so remove from unload queue
    void removeRelevantChunksFromUnloadQueue(ScopeConfiguration sc, World world) {
        Vector2d v2d;
        Collection<GameObject> activeGameObjects = world.activeObjects.values();
        int distance = sc.getScopeSize();
        for(int i : toRemove) {
            v2d = MortonCurve.getCoordinates(i);
            for(GameObject go : activeGameObjects) {
                if(go.getPosition3d().isWithinDistance(v2d,distance)) {
                    toRemove.remove(i);
                    break;
                }
            }
        }
    }

    public void clear() {
        toAdd.clear();
        toRemove.clear();
    }

}