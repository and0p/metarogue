package io.metarogue.game.timeline.actions;

import io.metarogue.Main;
import io.metarogue.util.math.Vector3d;
import io.metarogue.game.gameworld.World;
import io.metarogue.util.Log;
import io.metarogue.game.gamemessage.GameMessage;

public class BlockChangeMessage extends GameMessage {
    int x;
    int y;
    int z;
    int type;
    int oldType;
    int world;

    public BlockChangeMessage(int world, int x, int y, int z, int type) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.type = type;
    }

    public BlockChangeMessage(int world, Vector3d position, int type) {
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

    public boolean isTCP() { return true; }

}