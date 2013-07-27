package net.and0.metarogue.util.settings;

public class WorldSettings {
	
	public static int worldHeight = 8;
	public static int defaultResolution = 8;
	public static int chunkDimensions = 16;
	
	public static int worldAbsoluteResolution = defaultResolution * chunkDimensions;
	public static int worldAbsoluteHeight = 	worldHeight * chunkDimensions;

    public static int storageDistance = 5;

	public WorldSettings() {
		// TODO Auto-generated constructor stub
	}

}
