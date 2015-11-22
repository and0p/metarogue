package io.metarogue.util;

// Thinking this guy will load / unload chunks as needed. Maybe these methods will be consolidated into the class?
// But the World class is already pretty unwieldy..

import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.game.gameworld.ChunkArray;
import io.metarogue.game.gameworld.World;
import io.metarogue.util.math.MortonCurve;
import io.metarogue.util.settings.DisplaySettings;
import io.metarogue.util.settings.WorldSettings;
import io.metarogue.client.view.threed.Box;
import io.metarogue.util.math.Vector2d;
import io.metarogue.util.math.Vector3d;

import java.nio.ByteBuffer;
import java.util.*;

public class WorldManager {

    static ByteBuffer bb = ByteBuffer.allocate(4096 * 12);
    static byte b; // oh god
    static int ibAllocated = 0;

    public WorldManager() {
    }

    static void allocateIB() {
        Random randomGenerator = new Random();
        bb.mark();
        int floor = 4096;
        int ceil = 4096*4;
        //for(int i = 0; i < floor; i++) bb.put((byte)randomGenerator.nextInt(23));
        for(int i = 0; i < floor; i++) bb.put((byte)5);
        b = (byte)0;
        for(int i = floor; i < ceil; i++) {
            bb.put(b);
        }
        bb.reset();
        ibAllocated = 1;
    }

    public static void updateChunks(World world) {
        if(ibAllocated == 0) allocateIB();
        // Create a couple of ArrayLists. One that only holds int of Morton code for any chunk, and another that holds
        // a 3d coordinate. When updating world chunk arrays I think I'll be using the Y to store the morton code,
        // which is a bad idea ignore me etc etc
        Set<Vector3d> visibleChunks = new HashSet<Vector3d>();
        Set<Integer> visibleChunksI = new HashSet<Integer>();
        // Another two for new chunks and chunks to remove
        ArrayList<Vector3d> chunksAdded = new ArrayList<Vector3d>();
        ArrayList<Vector3d> chunksRemoved = new ArrayList<Vector3d>();
        int changesmade = 0;

        // First, see if any player has changed positions at all
        for(GameObject p : world.activeObjects.values()) {
            if(p.hasChangedChunkArrays) {
                changesmade++;
                // Set player as no longer having moved over a border since last check
                p.hasChangedChunkArrays = false;
            }
        }
        // If they have...
        if(changesmade > 0) {
            world.chunkChanges = true;
            // Loop through all "player" objects.
            for(GameObject p : world.activeObjects.values()) {
                // Get radius (or square) around player
                ArrayList<Vector3d> vp = getVisibleChunkArrays(world, p);
                for (Vector3d v3d : vp) {
                    visibleChunksI.add(v3d.getY());
                    visibleChunks.add(v3d);
                }
            }

            // Start adding / removing chunkarrays

            // If chunk is not in world currently, add it.
            for (Vector3d v3d : visibleChunks) {
                if (!world.worldMap.containsKey(v3d.getY())) {
                    ChunkArray ca = new ChunkArray(v3d.getX(), v3d.getZ(), world.worldHeight, bb);
                    //ChunkArray ca = Main.game.dbLoader.loadChunkArray(world, v3d.getX(), v3d.getZ());
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
                    /////////////////////Main.getActiveDB().saveChunkArray(world, i);
                    world.worldMap.remove(i);
                    Vector2d vec2d = MortonCurve.getCoordinates(i);
                    setChunkArrayUpdated(world, vec2d.getX(), vec2d.getZ());
                }
            }
        }
    }

    // Returns chunk arrays within radius of "player" in a world.
    public static ArrayList<Vector3d> getVisibleChunkArrays(World world, GameObject p) {
        ArrayList visibleChunks = new ArrayList<Vector2d>();
        int viewDistance = DisplaySettings.minimumViewDistance+1;
        Vector3d pos = p.getPosition().getVector3d();
        // Get "chunk coordinates" of player's position
        Vector2d chunkPos = world.getChunkArrayFromAbsolute(pos.getX(), pos.getZ());
        chunkPos.setX(chunkPos.getX() - (viewDistance)); chunkPos.setY(chunkPos.getY() - (viewDistance));
        if(chunkPos.getX() < 0) chunkPos.setX(0);
        if(chunkPos.getY() < 0) chunkPos.setY(0);
        Box box = new Box(chunkPos, viewDistance*2, viewDistance*2);
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

    public static void saveAll(World world) {
        for (Enumeration<Integer> e = world.worldMap.keys(); e.hasMoreElements(); ) {
            Integer i = e.nextElement();
            ///////////////Main.getActiveDB().saveChunkArray(world, i);
        }
    }

}