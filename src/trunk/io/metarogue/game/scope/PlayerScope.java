package io.metarogue.game.scope;

import io.metarogue.Main;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.game.scope.modifications.ScopeModification;

public class PlayerScope extends EndpointScope {

    public boolean contains(int i) {
        return true;
    }

    // Verify that our client controls this GameObject, and that it is active
    public boolean verify(GameObject go) {
        int clientID = Main.getClient().getClientID();
        if (go.isActive() && go.getControllingPlayers().contains(clientID)) {
            return true;
        } else {
            return false;
        }
    }

    public void add(ScopeModification sm, GameObject go) {
        modificationCollection.add(sm);
    }

    public void update() {
        modificationCollection.update(worldScopes);
    }

}