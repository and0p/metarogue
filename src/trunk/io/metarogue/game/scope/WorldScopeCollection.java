package io.metarogue.game.scope;

import io.metarogue.Main;

import java.util.HashMap;

public class WorldScopeCollection {

    HashMap<Integer, WorldScope> scopes;

    public void refresh() {
        for (WorldScope ws : scopes.values()) {
            //ws.refresh(Main.getGame().getWorld(getActiveObjects());
        }
    }

    public WorldScopeCollection() {
        scopes = new HashMap<Integer, WorldScope>();
    }

}