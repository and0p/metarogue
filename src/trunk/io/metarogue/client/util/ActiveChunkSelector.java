package io.metarogue.client.util;

import io.metarogue.Main;
import io.metarogue.game.gameworld.World;
import io.metarogue.util.settings.DisplaySettings;
import io.metarogue.client.view.threed.Box;
import io.metarogue.client.view.threed.Vector2d;
import io.metarogue.client.view.threed.Vector3d;

import java.util.ArrayList;

/**
 * MetaRogue class
 * User: andrew
 * Date: 6/11/13
 * time: 2:47 PM
 */
public class ActiveChunkSelector {

    public ActiveChunkSelector() {
        // Auto-generated constructor
    }

    // Returns chunk arrays within radius of "player" in a world.
    public static ArrayList<Vector2d> getVisibleChunkArrays(World world) {
        ArrayList visibleChunks = new ArrayList<Vector2d>();
        int viewDistance = DisplaySettings.minimumViewDistance;
        Vector3d pos = Main.getClient().getPlayerObject().getPosition();
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