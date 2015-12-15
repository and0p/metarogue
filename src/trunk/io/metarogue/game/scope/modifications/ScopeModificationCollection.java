package io.metarogue.game.scope.modifications;


import io.metarogue.Main;
import io.metarogue.game.gameworld.PartialWorld;
import io.metarogue.game.scope.WorldScope;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

// Collection that ensures no one GameObject has more than one scope modification per update so as not to waste resources
public class ScopeModificationCollection {

    // Table pairing GameObjects by ID to their respective scope changes this update cycle
    HashMap<Integer, ScopeModification> list;

    public ScopeModificationCollection() {
        list = new HashMap<Integer, ScopeModification>();
    }

    // Add a ScopeModification
    public void add(ScopeModification sm) {
        // Get the GameObject's ID
        int gameObjectID = sm.getGameObjectID();
        // Check if we already have a modification for this object this cycle
        if(list.containsKey(gameObjectID)) {
            // Grab that old modification and change final position to the latest one
            ScopeModification currentModification = list.get(gameObjectID);
            currentModification.newLocation = sm.newLocation;
        } else {
            // If there is not modification for this GameObject already, just pop the new modification in
            list.put(sm.getGameObjectID(), sm);
        }
    }

    public void update(HashMap<Integer, WorldScope> worldScopes) {
        // Loop through all modifications, adding additions and removals to appropriate worlds
        // Declare and recycle primitives
        int worldID;
        for(ScopeModification sm : list.values()) {
            // Look at old world in scope modification
            worldID = sm.getOldWorld();
            // If there is no current WorldScope for it, create one and create new PartialWorld for loading
            if(!worldScopes.containsKey(worldID)) {
                worldScopes.put(worldID, new WorldScope(worldID));
                Main.getGame().loadWorld(worldID);
            }
            WorldScope ws = worldScopes.get(worldID);
            // Add removals to this world's scope, from old position
            ws.addOld(sm.getOldIndexes());
            // Add additions to this world's scope, from new position
            ws.addNew(sm.getNewIndexes());
        }
    }

    public HashMap<Integer, ScopeModification> getAll() {
        return list;
    }

    // Clear the list, to be called after an EndpointScope is done with a cycle
    public void clear() {
        list.clear();
    }

}