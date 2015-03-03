package io.metarogue.game.events.actions;

/**
 * Effectively a null action so I don't have to worry about checking for action != null higher up the chain.
 * Return this instead of null if Event is pinged for an action that is out of bounds.
 * Curious method will get this and try to run() or reverse() with no effect.
 * Useful in-case I try to "track" ahead of or before actual history.
 */

public class BlankAction extends Action {

    final static BlankAction blankAction = new BlankAction();

    public BlankAction() {
    }

    public static BlankAction getInstance() {
        return blankAction;
    }

    public void run() {
    }

    public void reverse() {
    }

}