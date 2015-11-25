package io.metarogue.game.scope;

import io.metarogue.game.scope.modifications.ScopeModification;

import java.util.HashMap;
import java.util.HashSet;

public class WorldScope implements Scope {

    // Set of X,Z coordinates translated to single integer via MortonCurve
    HashSet<Integer> currentScope;
    // Final list of changes to scope (if any) at end of frame
    HashMap<Integer, ScopeModification> changes;
    // Stuff to add
    HashSet<Integer> toAdd;
    // Stuff to remove
    HashSet<Integer> toRemove;

    public WorldScope() {
        currentScope = new HashSet<Integer>();
    }

    public boolean contains(int i) {
        return true;
    }

    // Update based on possibly new areas and possibly old areas, estimated from GameObject movement
    public void update() {
        for(ScopeModification sm : changes.values()) {
            toAdd.addAll(sm.getNewIndexes());
            toRemove.addAll(sm.getOldIndexes());
        }
    }

}