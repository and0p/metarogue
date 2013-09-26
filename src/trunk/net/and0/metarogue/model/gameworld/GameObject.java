package net.and0.metarogue.model.gameworld;

import java.util.HashMap;
import java.util.Map;

import net.and0.metarogue.util.threed.Vector3d;

public class GameObject {
	
	Vector3d position;
	String type;
    public boolean hasChangedChunkArrays = true;
    public boolean hasChangedChunks = true;
	
	Map<String, Integer> variables;

	public GameObject(Vector3d position, String type) {
		this.position = position;
		this.type = type;
		variables = new HashMap<String, Integer>();
	}
	
	public Vector3d getPosition() {
		return position;
	}

	public void setPosition(Vector3d position) {
		this.position = position;
	}
	
	// Method for easy relative movement
	public void move(int x, int y, int z) {
        Vector3d newPosition = new Vector3d(position.getX() + x, position.getY() + y, position.getZ() + z);
        // Check if positioned in different chunk
        hasChangedChunkArrays = Vector3d.isDifferentChunkArray(position, newPosition);
        hasChangedChunks = Vector3d.isDifferentChunk(position, newPosition);
		position = newPosition;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}