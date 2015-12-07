package io.metarogue.game.scope;

import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.game.scope.modifications.ScopeModification;
import io.metarogue.game.scope.modifications.ScopeModificationCollection;

import java.util.HashMap;

// Scope for the server.
public class ServerScope extends EndpointScope {

    HashMap<Integer, PlayerScope> playerScopes;

    public ServerScope() {
        modificationCollection = new ScopeModificationCollection();
        playerScopes = new HashMap<Integer, PlayerScope>();
        worldScopes = new HashMap<Integer, WorldScope>();
    }

    public boolean contains(int i) {
        return true;
    }

    public void add(ScopeModification sm, GameObject go) {
        // Add to modification collection, and also pass to any Player scopes that are relevant
        modificationCollection.add(sm);
        for(int i : go.getControllingPlayers()) {
            if(playerScopes.containsKey(i)) {
                //TODO: Add add(ScopeModification) to PlayerScope where it assumes GO is relevant. only server will use
                playerScopes.get(i).add(sm, go);
            }
        }
    }

    public boolean verify(GameObject go) {
        if(go.isActive()) return true;
        return false;
    }

    // Parse changes on server and player level
    public void update() {
        worldsToLoad = modificationCollection.addChangesAndReturnNewWorlds(worldScopes);
        for(PlayerScope ps : playerScopes.values()) {
            ps.update();
        }
    }

}