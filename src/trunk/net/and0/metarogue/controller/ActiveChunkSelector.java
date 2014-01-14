package net.and0.metarogue.controller;

import net.and0.metarogue.model.gameworld.World;
import net.and0.metarogue.util.settings.DisplaySettings;
import net.and0.metarogue.util.threed.Box;
import net.and0.metarogue.util.threed.Vector2d;
import net.and0.metarogue.util.threed.Vector3d;

import java.util.ArrayList;

/**
 * MetaRogue class
 * User: andrew
 * Date: 6/11/13
 * Time: 2:47 PM
 */
public class ActiveChunkSelector {

    public ActiveChunkSelector() {
        // Auto-generated constructor
    }

    // Returns chunk arrays within radius of "player" in a world.
    public static ArrayList<Vector2d> getVisibleChunkArrays(World world) {
        ArrayList visibleChunks = new ArrayList<Vector2d>();
        int viewDistance = DisplaySettings.minimumViewDistance;
        Vector3d pos = world.playerObject.getPosition();
        // Get "chunk coordinates" of player's position
        Vector2d chunk = world.getChunkArrayFromAbsolute(pos.getX(), pos.getZ());
        chunk.setX(chunk.getX() - (viewDistance/2)); chunk.setY(chunk.getY() - (viewDistance/2));
        Box box = new Box(chunk, viewDistance, viewDistance);
        for(int x = box.getLeft(); x <= box.getRight(); x++) {
            for(int z = box.getTop(); z <= box.getBottom(); z++) {
                visibleChunks.add(new Vector2d(x, z));
            }
        }
        return visibleChunks;
    }

}
