package io.metarogue.game.gameworld;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.util.math.Vector2d;
import io.metarogue.util.math.Vector3d;
import io.metarogue.util.math.MortonCurve;

/**
 * World class
 *
 * <p>Hashtable that stores a (algorithmic) two-dimensional array of one-dimensional chunk arrays that contain game data
 * <p>Amongst other things. Good times.
 * <p>Contains methods to access all world data
 *
 * @author and0
 */

public class World {

    public int id;
    public String name;

    public int worldResolution;
    public int worldHeight;
    public static int chunkResolution = 16;
    public static int absoluteResolution;
    public static int absoluteHeight;
    public static int hashAllocation;

    public static int nullBlock = 0;

    public Boolean building; // Boolean as to whether or not this world is undergoing some huge update / loading

    public ConcurrentHashMap<Integer, ChunkArray> worldMap; // Hashtable of world geometry / chunk data, or the "world"
    public HashMap<Integer, GameObject> gameObjects;
    public HashMap<Integer, GameObject> activeObjects;

    public HashSet<Vector3d> updatedChunks;

    public Vector3d spawningPosition = new Vector3d();

    // Temporary gameVariable to let me know to update my currently really unoptimized mesh rendering thingy
    public boolean chunkChanges = true;

    /** Constructor for world with custom size*/
    public World(int id, int resolution, int height, int fill) {

        this.id = id;

        worldResolution = resolution;
        worldHeight = height;
        absoluteResolution = worldResolution * chunkResolution;
        absoluteHeight = worldHeight * chunkResolution;
        building = false;

        spawningPosition.set(40, 1, 20);

        updatedChunks = new HashSet<Vector3d>();

        gameObjects = new HashMap<Integer, GameObject>();
        activeObjects = new HashMap<Integer, GameObject>();

        hashAllocation = getHashAllocation(5);
        worldMap = new ConcurrentHashMap<Integer, ChunkArray>(hashAllocation);
    }

    // Find out how many chunkArrays to allocate with the hashtable. TODO: For smaller worlds this is easier, but for larger hashtables there should be some logic here.
    int getHashAllocation(int res){
        return res * res;
    }

    // Returns morton key from X+Z coordinates
    public static int returnKey(int x, int z) {
        return MortonCurve.getMorton(x, z);
    }

    public static int chunkCeil(int i) {
        return (int) Math.ceil(i / chunkResolution);
    }

    /** Returns chunk coordinates from absolute space coordinates. */
    public static int getChunkArrayKey(int x, int z) {
        // Use division to find which chunk a coordinate falls into
        return returnKey(chunkCeil(x), chunkCeil(z));
    }

    public static int getChunkArrayY(int y){
        return (int) Math.ceil((y / chunkResolution));
    }

    public static Vector2d getChunkArrayFromAbsolute(int x, int z) {
        return new Vector2d(chunkCeil(x), chunkCeil(z));
    }

    // Use modulus operator to find which place in a chunk a coordinate falls into
    int modCoordinates(int num) {
        return num % chunkResolution;
    }

    /**
     * Get the value of a block.
     * @param x (required) X coordinate of block.
     * @param y (required) Y coordinate of block.
     * @param z (required) Z coordinate of block.
     * @return Returns an <code>int</code> of what type of block occupies a particular coordinate
     */
    public int getBlock(int x, int y, int z) {
        // Return the "out of world" block type if the request is out of bounds
        if(x < 0 || x >= absoluteResolution) return nullBlock;
        if(y < 0 || y >= absoluteHeight) return nullBlock;
        if(z < 0 || z >= absoluteResolution) return nullBlock;
        // Check if coordinates are in loaded area. If not, return;
        ChunkArray shallowCopy = worldMap.get(getChunkArrayKey(x, z));
        if(shallowCopy != null) {
            return worldMap.get(getChunkArrayKey(x, z)).chunkArray[getChunkArrayY(y)].getBlock(modCoordinates(x),modCoordinates(y),modCoordinates(z));
        }
        return nullBlock;
    }

    public int getBlock(Vector3d v) {
        int x = v.getX(); int y = v.getY(); int z = v.getZ();
        if(x < 0 || x >= absoluteResolution) return nullBlock;
        if(y < 0 || y >= absoluteHeight) return nullBlock;
        if(z < 0 || z >= absoluteResolution) return nullBlock;
        // Check if coordinates are in loaded area. If not, return;
        ChunkArray shallowCopy = worldMap.get(getChunkArrayKey(x, z));
        if(shallowCopy != null) {
            return worldMap.get(getChunkArrayKey(x, z)).chunkArray[getChunkArrayY(y)].getBlock(modCoordinates(x),modCoordinates(y),modCoordinates(z));
        }
        return nullBlock;
    }

    /**
     * Set the value of a block.
     * @param type (required) What type of block you want to set it to.
     * @param x (required) X coordinate of block.
     * @param y (required) Y coordinate of block.
     * @param z (required) Z coordinate of block.
     */
    public void setBlock(int type, int x, int y, int z) {
        if(x < 0 || x > absoluteResolution) return;
        if(y < 0 || y > absoluteHeight - 1) return;
        if(z < 0 || z > absoluteResolution) return;
        // Check if area is even loaded. If not, simply return.
        ChunkArray shallowCopy = worldMap.get(getChunkArrayKey(x, z));
        if(shallowCopy != null) {
            shallowCopy.chunkArray[getChunkArrayY(y)].setBlock(type, modCoordinates(x),modCoordinates(y),modCoordinates(z));
            if(!building) updatedChunks.add(new Vector3d(chunkCeil(x), getChunkArrayY(y), chunkCeil(z)));
        }
    }

    public void setBlock(int type, Vector3d v) {
        int x = v.getX(); int y = v.getY(); int z = v.getZ();
        // Check if coordinates are in world bounds at all. If not, return;
        if(x < 0 || x > absoluteResolution) return;
        if(y < 0 || y > absoluteHeight - 1) return;
        if(z < 0 || z > absoluteResolution) return;
        // Check if area is even loaded. If not, return.
        ChunkArray shallowCopy = worldMap.get(getChunkArrayKey(x, z));
        if(shallowCopy != null) {
            shallowCopy.chunkArray[getChunkArrayY(y)].setBlock(type, modCoordinates(x),modCoordinates(y),modCoordinates(z));
            if(!building) updatedChunks.add(new Vector3d(chunkCeil(x), getChunkArrayY(y), chunkCeil(z)));
        }
    }

    public void fillArea(int type, Vector3d corner, Vector3d size) {
        for(int x = corner.getX(); x < corner.getX()+size.getX(); x++) {
            for(int y = corner.getY(); y < corner.getY()+size.getY(); y++) {
                for(int z = corner.getZ(); z < corner.getZ()+size.getZ(); z++) {
                    setBlock(type, x, y, z);
                }
            }
        }
    }

    /**
     * Returns <code>ChunkArray</code> from chunk coordinates.
     * @param x (required) X coordinate of chunk.
     * @param z (required) Z coordinate of chunk.
     * @return Returns a full <code>ChunkArray</code>.
     */
    public ChunkArray getChunkArray(int x, int z) {
        return worldMap.get(returnKey(x, z));
    }

    public ChunkArray getChunkArray(int i) {
        return worldMap.get(i);
    }

    /**
     * Returns <code>ChunkArray</code> from block-level world coordinates.
     * @param x (required) X absolute coordinate of chunk.
     * @param z (required) Z absolute coordinate of chunk.
     * @return Returns a full <code>ChunkArray</code>.
     */
    public ChunkArray getChunkArrayAbsolute(int x, int z) {
        return worldMap.get(getChunkArrayKey(x, z));
    }

    /**
     * Returns <code>Chunk</code> from chunk coordinates.
     * @param x (required) X coordinate of chunk.
     * @param y (required) Y coordinate of chunk.
     * @param z (required) Z coordinate of chunk.
     * @return Returns a full <code>Chunk</code>.
     */
    public Chunk getChunk(int x, int y, int z) {
        if(x < 0 || x >= worldResolution) return null;
        if(y < 0 || y >= worldHeight) return null;
        if(z < 0 || z >= worldResolution) return null;
        ChunkArray shallowCopy = worldMap.get(returnKey(x, z));
        if(shallowCopy == null) return null;
        return worldMap.get(returnKey(x, z)).getChunk(y);
    }

    /**
     * Returns <code>Chunk</code> from chunk coordinates.
     * @param x (required) X coordinate of chunk.
     * @param y (required) Y coordinate of chunk.
     * @param z (required) Z coordinate of chunk.
     * @return Returns a full <code>Chunk</code>.
     */
    public Chunk getChunkAbsolute(int x, int y, int z) {
        return worldMap.get(getChunkArrayKey(x, z)).getChunk(y);
    }

    public int[] getAdjacentBlocks(int x,int y,int z){
        // Initialize array, x+-y+-z+-
        int array[] = new int[6];
        for(int i = 0; i < 6; i++) array[i] = 0;

        // Get x +
        array[0] = getBlock(x+1,y,z);
        // Get x -
        array[1] = getBlock(x-1,y,z);
        // Get y +
        array[2] = getBlock(x,y+1,z);
        // Get y -
        array[3] = getBlock(x,y-1,z);
        // Get z +
        array[4] = getBlock(x,y,z+1);
        // Get z -
        array[5] = getBlock(x,y,z-1);

        return array;
    }

    public Enumeration<Integer> getKeys(){
        return worldMap.keys();
    }

    public static void getAbsolutePosition(int chunkX, int chunkY, int chunkZ, int x, int y, int z){
        // Stuff
    }

    public int getAbsoluteResolution() {
        return absoluteResolution;
    }

    public int getAbsoluteHeight() {
        return absoluteHeight;
    }

    public GameObject getObject(int i) {
        return this.gameObjects.get(i);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addGameObject(GameObject o) {
        gameObjects.put(o.getID(), o);
        if(o.isActive()) {
            activeObjects.put(o.getID(), o);
        }
    }

}