package net.and0.metarogue.controller;

// Thinking this guy will load / unload chunks as needed. Maybe these methods will be consolidated into the class?
// But the World class is already pretty unwieldy..

import net.and0.metarogue.model.gameworld.ChunkArray;
import net.and0.metarogue.model.gameworld.GameObject;
import net.and0.metarogue.model.gameworld.World;
import net.and0.metarogue.util.MortonCurve;
import net.and0.metarogue.util.settings.DisplaySettings;
import net.and0.metarogue.util.settings.WorldSettings;
import net.and0.metarogue.util.threed.Box;
import net.and0.metarogue.util.threed.Vector2d;
import net.and0.metarogue.util.threed.Vector3d;

import java.util.ArrayList;
import java.util.Enumeration;

public class WorldManager {

    public WorldManager() {
        // Auto-generated constructor
    }

    public static void updateChunks(World world) {
        // Create a couple of ArrayLists. One that only holds int of Morton code for any chunk, and another that holds
        // a 3d coordinate. When updating world chunk arrays I think I'll be using the Y to store the morton code,
        // which is a bad idea ignore me etc etc
        ArrayList<Vector3d> visibleChunks = new ArrayList<Vector3d>();
        ArrayList<Integer> visibleChunksI = new ArrayList<Integer>();
        // Another two for new chunks and chunks to remove
        ArrayList<Vector3d> chunksAdded = new ArrayList<Vector3d>();
        ArrayList<Vector3d> chunksRemoved = new ArrayList<Vector3d>();
        int changesmade = 0;

        // First, see if any player has changed positions at all
        for(GameObject p : world.playerObjects) {
            if(p.hasChangedChunks) {
                changesmade++;
                // Set player as no longer having moved over a border since last check
                p.hasChangedChunks = false;
            }
        }
        // If they have...
        if(changesmade > 0) {
            // Loop through all "player" objects.
            for(GameObject p : world.playerObjects) {
                // Get radius (or square) around player
                ArrayList<Vector3d> vp = getVisibleChunkArrays(world, p);
                for (Vector3d v3d : vp) {
                    visibleChunksI.add(v3d.getY());
                    visibleChunks.add(v3d);
                }
            }

            // Start adding / removing chunkarrays

            // If chunk is not in world currently, add it. Right now just creating blank one
            for (Vector3d v3d : visibleChunks) {
                if (!world.worldMap.containsKey(v3d.getY())) {
                    world.worldMap.put(v3d.getY(), new ChunkArray(v3d.getX(), v3d.getZ(), world.worldHeight, 1));
                    setChunkArrayUpdated(world, v3d.getX(), v3d.getZ());
                }
            }
            // Remove chunks that are no longer "visible"
            for (Enumeration<Integer> e = world.worldMap.keys(); e.hasMoreElements(); ) {
                Integer i = e.nextElement();
                if(!visibleChunksI.contains(i)) {
                   world.worldMap.remove(i);
                   Vector2d vec2d = MortonCurve.getCoordinates(i);
                   setChunkArrayUpdated(world, vec2d.getX(), vec2d.getZ());
                }
            world.chunkChanges = true;
            }
        }
    }

    // Returns chunk arrays within radius of "player" in a world.
    public static ArrayList<Vector3d> getVisibleChunkArrays(World world, GameObject p) {
        ArrayList visibleChunks = new ArrayList<Vector2d>();
        int viewDistance = WorldSettings.storageDistance;
        Vector3d pos = p.getPosition();
        // Get "chunk coordinates" of player's position
        Vector2d chunk = world.getChunkArrayFromAbsolute(pos.getX(), pos.getZ());
        chunk.setX(chunk.getX() - (viewDistance)); chunk.setY(chunk.getY() - (viewDistance));
        if(chunk.getX() < 0) chunk.setX(0);
        if(chunk.getY() < 0) chunk.setY(0);
        Box box = new Box(chunk, viewDistance, viewDistance);
        for(int x = box.getLeft(); x <= box.getRight(); x++) {
            for(int z = box.getTop(); z <= box.getBottom(); z++) {
                visibleChunks.add(new Vector3d(x, MortonCurve.getMorton(x,z), z));
            }
        }
        return visibleChunks;
    }

    public static void setChunkArrayUpdated(World world, int x, int z) {
        for(int y = 0; y < WorldSettings.worldHeight; y++){
            world.updatedChunks.add(new Vector3d(x, y, z));
        }
    }

}