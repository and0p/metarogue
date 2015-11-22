package io.metarogue.game.scope;


import io.metarogue.util.messagesystem.Listener;

public abstract class EndpointScope implements Scope, Listener {

    public boolean contains(int i) {
        return false;
    }

}
