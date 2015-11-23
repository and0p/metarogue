package io.metarogue.game.scope.modifications;

import io.metarogue.game.gameworld.World;

import java.util.HashSet;

public interface ScopeModification {

    int getGameObjectID();
    HashSet<Integer> getNewIndexes();
    HashSet<Integer> getOldIndexes();
    int getWorld();

}
