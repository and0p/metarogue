package io.metarogue.game.events.actions;

public class BlockAction extends Action {

    int x;
    int y;
    int z;
    int type;

    public BlockAction(int x, int y, int z, int type) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
    }

}