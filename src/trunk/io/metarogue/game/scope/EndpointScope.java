package io.metarogue.game.scope;

import io.metarogue.Main;
import io.metarogue.game.gamemessage.gameobject.AbsoluteMoveMessage;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.game.scope.modifications.ScopeModification;
import io.metarogue.game.scope.modifications.ScopeModificationCollection;
import io.metarogue.util.messagesystem.Listener;
import io.metarogue.util.messagesystem.message.Message;

import java.util.HashMap;
import java.util.HashSet;

// Abstract class with some concrete implementations of listening functionality
public abstract class EndpointScope implements Scope, Listener {

    ScopeModificationCollection modificationCollection;
    HashMap<Integer, WorldScope> worldScopes;
    HashSet<Integer> worldsToLoad;

    public void receive(Message m) {
        // Assume only care about AbsoluteMoveMessage
        if(m instanceof AbsoluteMoveMessage) {
            // Cast it
            AbsoluteMoveMessage mm = (AbsoluteMoveMessage)m;
            // If the message implies an object changing chunks...
            if(mm.objectLeavesChunk()) {
                // ...check if it's an active object and is relevant to this EndpointScope
                GameObject go = Main.getGame().getGameObject((mm.getGameObjectID()));
                if(verify(go)) {
                    // If it's active, create a new scope modification
                    ScopeModification sm = new ScopeModification(mm.getGameObjectID(), mm.getOriginalPosition(), mm.getEndingPosition());
                    add(sm, go);
                }
            }
        }
    }

    public abstract void update();

    abstract boolean verify(GameObject go);
    abstract void add(ScopeModification sm, GameObject go);

}
