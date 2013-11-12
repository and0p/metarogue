package net.and0.metarogue.model.gameworld;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Chunk class
 * 
 * <p>Three dimensional array of integers, representings (typically) types of blocks in 3D space
 * 
 * @author and0
 */

public class Chunk {

	static int chunkResolution = 16;										// dimensions of chunk
	int totalArray;	// size of total array is blockDim^3;
	byte blocks[];	// create array
	public int[] position = new int[3];							// position of this chunk in CHUUUNKSPAAACE
	public int[] absolutePosition = new int[3];					// absolute position of this chunk's 0,0,0
	
	// index for iteration ie display lists?
	
	// field for bounding box

	public Chunk(int xpos, int ypos, int zpos, int type) {
		totalArray = chunkResolution * chunkResolution * chunkResolution;
		blocks = new byte[totalArray];
		for(int i = 0; i < totalArray; i++) {
					blocks[i] = (byte)type;
		}
		position[0] = xpos;
		position[1] = ypos;
		position[2] = zpos;
		absolutePosition[0] = position[0] * chunkResolution;
		absolutePosition[1] = position[1] * chunkResolution;
		absolutePosition[2] = position[2] * chunkResolution;
	}

    public Chunk(int xpos, int ypos, int zpos, byte[] data) {
        totalArray = chunkResolution * chunkResolution * chunkResolution;
        blocks = data;
        position[0] = xpos;
        position[1] = ypos;
        position[2] = zpos;
        absolutePosition[0] = position[0] * chunkResolution;
        absolutePosition[1] = position[1] * chunkResolution;
        absolutePosition[2] = position[2] * chunkResolution;
    }
	
	/** 
	 * Get the value of a block.
	 * @param x (required) X coordinate of block.
	 * @param y (required) Y coordinate of block.
	 * @param z (required) Z coordinate of block.
	 * @return Returns an <code>int</code> of what type of block occupies a particular coordinate
	 */
	public int getBlock(int x, int y, int z) {
		return blocks[x + y*chunkResolution + z*chunkResolution*chunkResolution];
	}
	
	/** 
	 * Set the value of a block.
	 * @param type (required) What type of block you want to set it to.
	 * @param x (required) X coordinate of block.
	 * @param y (required) Y coordinate of block.
	 * @param z (required) Z coordinate of block.
	 */
	public void setBlock(int type, int x, int y, int z) {
        blocks[x + y*chunkResolution + z*chunkResolution*chunkResolution] = (byte)type;
	}
	
	/** 
	 * Get the position of this chunk
	 * @param i (required) X = 0, Y = 1, Z = 2
	 * @return Returns an <code>int</code> of what type of block occupies a particular coordinate
	 */
	public int getPosition(int i) {
		return position[i];
	}

    public ByteBuffer getBytes() {
        ByteBuffer bb = ByteBuffer.allocate(4096);
        for(int i = 0; i < 4096; i++) bb.put(blocks[i]);
        return bb;
    }

}