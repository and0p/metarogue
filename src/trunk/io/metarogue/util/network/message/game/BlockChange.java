package io.metarogue.util.network.message.game;

import io.metarogue.Main;
import io.metarogue.client.view.threed.Vector3d;

public class BlockChange {

    int world;
    Vector3d coordinates;
    int type;

    public BlockChange(int world, Vector3d coordinates, int type) {
        this.world = world;
        this.coordinates = coordinates;
        this.type = type;
    }

    public boolean verify() {
        // Check to see if player has permission to modify world in that area
        return true;
    }

    public void run() {
        Main.getGame().getWorld(world).setBlock(type, coordinates);
    }

}