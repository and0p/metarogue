package net.and0.metarogue.model.gameworld;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import net.and0.metarogue.controller.ActiveChunkSelector;
import net.and0.metarogue.model.Camera;
import net.and0.metarogue.util.threed.Vector2d;
import net.and0.metarogue.util.threed.Vector3d;
import net.and0.metarogue.util.MortonCurve;
import net.and0.metarogue.util.settings.WorldSettings;
import org.lwjgl.util.vector.Vector3f;

/**
 * World class
 *
 * <p>Hashtable that stores a (algorithmic) two-dimensional array of one-dimensional chunk arrays that contain game data
 * <p>Contains methods to access all world data
 *
 * @author and0
 */

public class World {

    public int worldResolution;
    public int worldHeight;
    public static int chunkResolution = 16;
    public static int absoluteResolution;
    public static int absoluteHeight;
    public static int hashAllocation;

    public Boolean building; // Boolean as to whether or not this world is undergoing some huge update, functions use to decide whether to rebuild meshes

    public Hashtable<Integer, ChunkArray> worldMap; // Hashtable of world geometry / chunk data, or the "world"
    public List<GameObject> worldObjects;
    public List<GameObject> playerObjects;

    public List<Vector3d> activeChunkArrays3d; // List of active chunk arrays, Y in vector 3d will store morton num
    public List<Vector3d> updatedChunks;

    public Vector3d spawningPosition = new Vector3d();
    public Vector3d center = new Vector3d();

    public Camera camera;
    public Vector3d selectedBlock = null;
    public GameObject playerObject;
    public Vector3d playerPositionInChunkspace;

    // Temporary variable to let me know to update my currently really unoptimized mesh rendering thingy
    public boolean chunkChanges = true;

    /** Constructor for world with custom size*/
    public World(int resolution, int height, int fill) {

        worldResolution = resolution;
        worldHeight = height;
        absoluteResolution = worldResolution * chunkResolution;
        absoluteHeight = worldHeight * chunkResolution;

        spawningPosition.set(20, 4, 20);

//        activeChunkArrays3d = new ArrayList<Vector3d>();
        updatedChunks = new ArrayList<Vector3d>();

        worldObjects = new ArrayList<GameObject>();
        playerObjects = new ArrayList<GameObject>();
        playerObject = new GameObject(spawningPosition, "Soldier");
        playerObjects.add(playerObject);
        //playerPositionInChunkspace = spawningPosition.toChunkSpace();

        hashAllocation = getHashAllocation(5);
        worldMap = new Hashtable<Integer, ChunkArray>(hashAllocation);

        camera = new Camera(10, spawningPosition.getX()-20, spawningPosition.getY(), spawningPosition.getZ()-20);

        // Iterate over x & z dimensions, giving each chunk it's own position
        building = true;
//        for(int x = 0; x < resolution; x++) {
//            for(int z = 0; z < resolution; z++) {
//                worldMap.put(returnKey(x, z), new ChunkArray(x, z, worldHeight, fill));
//                // activeChunkArrays.add(returnKey(x, z));
//                // activeChunkArrays2d.add(new Vector2d(x, z));
//                for(int y = 0; y < worldHeight; y++){
//                    updatedChunks.add(new Vector3d(x, y, z));
//                }
//            }
//        }
//        activeChunkArrays2d = ActiveChunkSelector.getVisibleChunkArrays(this);
//        for(Vector2d v : activeChunkArrays2d) {
//            worldMap.put(returnKey(v.getX(), v.getY()), new ChunkArray(v.getX(), v.getY(), worldHeight, fill));
//            for(int y = 0; y < worldHeight; y++){
//                updatedChunks.add(new Vector3d(v.getX(), y, v.getY()));
//            }
//        }
        building = false;
    }

    // Find out how many chunkArrays to allocate with the hashtable. TODO: For smaller worlds this is easier, but for larger hashtables there should be some logic here.
    int getHashAllocation(int res){
        return res * res;
    }

    /** Returns crappy string I'm using on hashtable ie "x0z0". */
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
        if(x < 0 || x >= absoluteResolution) return 15;
        if(y < 0 || y >= absoluteHeight) return 15;
        if(z < 0 || z >= absoluteResolution) return 15;
        ChunkArray shallowCopy = worldMap.get(getChunkArrayKey(x, z));
        if(shallowCopy != null) {
            return worldMap.get(getChunkArrayKey(x, z)).chunkArray[getChunkArrayY(y)].getBlock(modCoordinates(x),modCoordinates(y),modCoordinates(z));
        }
        return 15;
    }

    public int getBlock(Vector3d v) {
        if(v.getX() < 0 || v.getX() >= absoluteResolution) return 15;
        if(v.getY() < 0 || v.getY() >= absoluteHeight) return 15;
        if(v.getZ() < 0 || v.getZ() >= absoluteResolution) return 15;
        ChunkArray shallowCopy = worldMap.get(getChunkArrayKey(v.getX(), v.getZ()));
        if(shallowCopy == null) return 0;
        return shallowCopy.chunkArray[getChunkArrayY(v.getY())].getBlock(modCoordinates(v.getX()),modCoordinates(v.getY()),modCoordinates(v.getZ()));
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
        ChunkArray shallowCopy = worldMap.get(getChunkArrayKey(x, z));
        if(shallowCopy != null) {
            shallowCopy.chunkArray[getChunkArrayY(y)].setBlock(type, modCoordinates(x),modCoordinates(y),modCoordinates(z));
            if(!building) updatedChunks.add(new Vector3d(chunkCeil(x), getChunkArrayY(y), chunkCeil(z)));
        }
        // worldMap.get(getChunkArrayKey(x, z)).chunkArray[getChunkArrayY(y)].setBlock(type, modCoordinates(x),modCoordinates(y),modCoordinates(z));
        // if(!building) updatedChunks.add(new Vector3d(chunkCeil(x), getChunkArrayY(y), chunkCeil(z)));
    }

    public void setBlock(int type, Vector3d v) {
        if(v.getX() < 0 || v.getX() >= absoluteResolution) return;
        if(v.getY() < 0 || v.getY() >= absoluteHeight) return;
        if(v.getZ() < 0 || v.getZ() >= absoluteResolution) return;
        worldMap.get(getChunkArrayKey(v.getX(), v.getZ())).chunkArray[getChunkArrayY(v.getX())].setBlock(type, modCoordinates(v.getX()),modCoordinates(v.getY()),modCoordinates(v.getZ()));
        if(!building) updatedChunks.add(new Vector3d(chunkCeil(v.getX()), getChunkArrayY(v.getY()), chunkCeil(v.getZ())));
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
        return this.worldObjects.get(i);
    }

    public Camera getActiveCamera() {
        return camera;
    }

}
