package net.and0.metarogue.controller;

// Thinking this guy will load / unload chunks as needed. Maybe these methods will be consolidated into the class?
// But the World class is already pretty unwieldy..

import net.and0.metarogue.main.Main;
import net.and0.metarogue.model.gameworld.ChunkArray;
import net.and0.metarogue.model.gameworld.GameObject;
import net.and0.metarogue.model.gameworld.World;
import net.and0.metarogue.util.MortonCurve;
import net.and0.metarogue.util.settings.DisplaySettings;
import net.and0.metarogue.util.settings.WorldSettings;
import net.and0.metarogue.util.threed.Box;
import net.and0.metarogue.util.threed.Vector2d;
import net.and0.metarogue.util.threed.Vector3d;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Enumeration;

public class WorldManager {

    static ByteBuffer bb = ByteBuffer.allocate(4096 * 4);
    static byte swizitch = 1; // oh god
    static int ibAllocated = 0;

    public WorldManager() {
    }

    static void allocateIB() {
        bb.mark();
//        int floor = 4096; int ceil = 4096*4;
//        for(int i = 0; i < floor; i++) bb.put(swizitch);
//        swizitch = 0;
        for(int i = 0; i < 4096*4; i++) bb.put(swizitch);
//        for(int i = 0; i < 4096*4; i++) {
//            bb.put(swizitch);
//            swizitch++;
//            if(swizitch > 2) swizitch = 0;
//        }
        bb.reset();
        ibAllocated = 1;
    }

    public static void updateChunks(World world) {
        if(ibAllocated == 0) allocateIB();
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
            if(p.hasChangedChunkArrays) {
                changesmade++;
                // Set player as no longer having moved over a border since last check
                p.hasChangedChunkArrays = false;
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
                    ChunkArray ca = Main.getActiveDB().loadChunkArray(world, v3d.getX(), v3d.getY());
                    if(ca != null) {
                        world.worldMap.put(v3d.getY(), ca);
                    } else {
                        bb.mark();
                        world.worldMap.put(v3d.getY(), new ChunkArray(v3d.getX(), v3d.getZ(), world.worldHeight, bb));
                        bb.reset();
                    }

                    setChunkArrayUpdated(world, v3d.getX(), v3d.getZ());
                }
            }
            // Remove chunks that are no longer "visible"
            for (Enumeration<Integer> e = world.worldMap.keys(); e.hasMoreElements(); ) {
                Integer i = e.nextElement();
                if(!visibleChunksI.contains(i)) {
                    Main.getActiveDB().saveChunkArray(world, i);
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
        ArrayList visibleChunks = new ArrayList<Vector3d>();
        int viewDistance = DisplaySettings.minimumViewDistance;
        Vector3d pos = p.getPosition();
        // Get "chunk coordinates" of player's position
        Vector2d chunkPos = world.getChunkArrayFromAbsolute(pos.getX(), pos.getZ());
        // Set the most negative corner of the "box" around the player, push it up if it hits the edge of the world
        chunkPos.setX(chunkPos.getX() - (viewDistance)); chunkPos.setY(chunkPos.getY() - (viewDistance));
        if(chunkPos.getX() < 0) chunkPos.setX(0);
        if(chunkPos.getY() < 0) chunkPos.setY(0);
        Box box = new Box(chunkPos, viewDistance*2+1, viewDistance*+1);
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