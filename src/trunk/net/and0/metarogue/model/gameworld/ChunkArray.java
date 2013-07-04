package net.and0.metarogue.model.gameworld;

public class ChunkArray {

	int worldHeight;
	int chunkResolution;
	Chunk[] chunkArray;
	int[] position = new int[2];

	public ChunkArray(int x, int z, int height, int type) {
		position[0] = x;
		position[1] = z;
		worldHeight = height;
		chunkArray = new Chunk[worldHeight];
		for (int i = 0; i < worldHeight; i++) {
			chunkArray[i] = new Chunk(position[0], i, position[1], type);
		}
	}

	// Public method to return the block type at a given coordinate
	public int getBlock(int x, int y, int z) {
		int ceilY = (int) Math.ceil(y / chunkResolution);	// Get which chunk the Y falls into
		int modY = y % chunkResolution;	// Get local y for that chunk
		return chunkArray[ceilY].getBlock(x, modY, z);
	}

	/** 
	 * Set the value of a block.
	 * @param type (required) What type of block you want to set it to.
	 * @param x (required) X coordinate of block.
	 * @param y (required) Y coordinate of block.
	 * @param z (required) Z coordinate of block.
	 * @param rebuild (required) Whether or not to rebuild mesh on pass
	 */
	public void setBlock(int type, int x, int y, int z) {
		int ceilY = (int) Math.ceil((y / chunkResolution));	// Get which chunk the Y falls into
		int modY = y % chunkResolution;	// Get local y for that chunk
		chunkArray[ceilY].setBlock(type, x, modY, z);
	}
	
	/**
	 * Return position 0 or 1 (X or Z)
	 * @param i (required) 0 or 1 (X or Z)
	 * @return Returns <code>int</code> of chunk array's coordinates in the hilariously-dubbed "chunkspace"
	 */
	public Chunk getChunk(int i) {
		return chunkArray[i];
	}

	/**
	 * Return position 0 or 1 (X or Z)
	 * @param i (required) 0 or 1 (X or Z)
	 * @return Returns <code>int</code> of chunk array's coordinates in the hilariously-dubbed "chunkspace"
	 */
	public int getPosition(int i) {
		return position[i];
	}
}