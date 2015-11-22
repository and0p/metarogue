package io.metarogue.game.scope;

import io.metarogue.game.gamemessage.gameobject.MoveGameObjectRelative;
import io.metarogue.game.scope.modifications.ScopeModification;
import io.metarogue.util.messagesystem.Listener;
import io.metarogue.util.messagesystem.message.Message;

import java.util.HashMap;

public class ServerScope implements Scope, Listener {

    HashMap<Integer, PlayerScope> playerScopes;
    HashMap<Integer, Scope> worldScopes;

    public boolean contains(int i) {
        return true;
    }

    public void receive(Message m) {
        if(m instanceof MoveGameObjectRelative) {

        }
    }

}