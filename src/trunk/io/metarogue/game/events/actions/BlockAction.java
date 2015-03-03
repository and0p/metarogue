package io.metarogue.game.events.actions;

import io.metarogue.Main;
import io.metarogue.client.view.threed.Vector3d;
import io.metarogue.game.gameworld.World;
import io.metarogue.util.Log;

public class BlockAction extends Action {
    int x;
    int y;
    int z;
    int type;
    int oldType;
    int world;

    public BlockAction(int world, int x, int y, int z, int type) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
    }

    public BlockAction(int world, Vector3d position, int type) {
        this.world = world;
        this.x = position.getX();
        this.y = position.getY();
        this.z = position.getZ();
        this.type = type;
    }

    public void run() {
        World w = Main.getGame().getWorld(world);
        if(w != null) {
            oldType = w.getBlock(x, y, z);
            w.setBlock(type, x, y, z);
            Log.log("      Running block action, setting block in world " + world + " at " + x + ", " + y + ", " + z + " from type " + oldType + " to " + type);
        }
    }

    public void reverse() {
        World w = Main.getGame().getWorld(world);
        if(w != null) {
            w.setBlock(oldType, x, y, z);
            Log.log("      Reversing block action, setting block in world " + world + " at " + x + ", " + y + ", " + z + " from type " + type + " to " + oldType);
        }
    }

}