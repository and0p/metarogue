package io.metarogue.game.events.actions;

import io.metarogue.Main;
import io.metarogue.game.gameworld.World;

public class BlockAction extends Action {
    int x;
    int y;
    int z;
    int type;
    int world;

    public BlockAction(int world, int x, int y, int z, int type) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
    }

    public void run() {
        World w = Main.getGame().getWorld(world);
        if(w != null) {
            w.setBlock(type, x, y, z);
        }
    }

    public void reverse() {

    }

}