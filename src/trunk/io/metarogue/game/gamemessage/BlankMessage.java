package io.metarogue.game.gamemessage;

import io.metarogue.game.gamemessage.GameMessage;

/**
 * Effectively a null action so I don't have to worry about checking for action != null higher up the chain.
 * Return this instead of null if Event is pinged for an action that is out of bounds.
 * Curious method will get this and try to run() or reverse() with no effect.
 * Useful in-case I try to "track" ahead of or before actual history.
 */

public class BlankMessage extends GameMessage {

    public boolean isTCP() { return false; }

    public BlankMessage() {
    }

    public void run() {
    }

    public void reverse() {
    }

}
