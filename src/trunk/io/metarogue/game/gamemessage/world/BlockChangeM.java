package io.metarogue.game.gamemessage.world;

import io.metarogue.Main;
import io.metarogue.util.math.Vector3d;
import io.metarogue.game.gameworld.World;
import io.metarogue.util.Log;
import io.metarogue.game.gamemessage.GameMessage;
import io.metarogue.util.math.Vector4d;

public class BlockChangeM extends GameMessage {

    Vector4d position;
    int type;
    int oldType;

    public BlockChangeM(int world, int x, int y, int z, int type) {
        this.position = new Vector4d(world,x,y,z);
        this.type = type;
    }

    public BlockChangeM(Vector4d position, int type) {
        this.position = position;
        this.type = type;
    }

    public void run() {
        World w = Main.getGame().getWorld(position.getWorld());
        if(w != null) {
            oldType = w.getBlock(position.getVector3d());
            w.setBlock(type, position.getVector3d());
            //Log.log("      Running block action, setting block in world " + world + " at " + x + ", " + y + ", " + z + " from type " + oldType + " to " + type);
        }
    }

    public void reverse() {
        World w = Main.getGame().getWorld(position.getWorld());
        if(w != null) {
            w.setBlock(oldType, position.getVector3d());
            //Log.log("      Reversing block action, setting block in world " + world + " at " + x + ", " + y + ", " + z + " from type " + type + " to " + oldType);
        }
    }

    public boolean isTCP() { return true; }

}