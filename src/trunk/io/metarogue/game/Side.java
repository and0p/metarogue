package io.metarogue.game;

/**
 * Side for parties and objects to be on. Most simply in a single-player roguelike the player is on side 0 and
 * all enemies are on side 1. If the player got a pet it would be side 0. Each side gets a subturn.
 * Currently assuming all sides will be declared at game beginning and cannot be modified.
 */

public class Side {

    int id;
    String name;

    public Side(int id, String name) {
        this.id = id;
        this.name = name;
    }

}
