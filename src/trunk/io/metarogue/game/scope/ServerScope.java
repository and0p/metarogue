package io.metarogue.game.scope;

import io.metarogue.Main;
import io.metarogue.game.gamemessage.gameobject.GameObjectMessage;
import io.metarogue.game.gamemessage.gameobject.MoveGameObjectRelative;
import io.metarogue.game.scope.modifications.ScopeMove;
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
        if(m instanceof GameObjectMessage) {
            if(Main.getGame().getGameObject(((GameObjectMessage) m).getGameObjectID()).is)
        }
        if(m instanceof MoveGameObjectRelative) {
            MoveGameObjectRelative o = (MoveGameObjectRelative)m;
            ScopeMove sm = new ScopeMove(o.getGameObjectID(), o.getOriginalPosition(), o.getNewPosition(), Main.getGame().getScopeDistance(), o.getWorld());
        }
    }

}