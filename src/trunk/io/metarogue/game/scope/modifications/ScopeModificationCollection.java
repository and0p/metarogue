package io.metarogue.game.scope.modifications;


import io.metarogue.game.scope.Scope;

import java.util.HashMap;

// Collection that ensures no one GameObject has more than one scope modification as that would be unnecessary computation
public class ScopeModificationCollection {

    // Table pairing GameObjects by ID to their respective scope changes this update cycle
    HashMap<Integer, ScopeModification> map;

    public ScopeModificationCollection() {
        map = new HashMap<Integer, ScopeModification>();
    }

    public void add(ScopeModification sm) {
        int gameObjectID = sm.getGameObjectID();
        if(map.containsKey(gameObjectID)) {
            ScopeModification currentModification = map.get(gameObjectID);

        }
    }

}
