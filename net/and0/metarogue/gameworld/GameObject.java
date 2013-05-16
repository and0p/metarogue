package net.and0.metarogue.gameworld;

import java.util.HashMap;
import java.util.Map;

import net.and0.metarogue.threed.Vector3d;

public class GameObject {
	
	Vector3d position;
	String type;
	
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
		int newX = position.getX() + x;
		int newY = position.getY() + y;
		int newZ = position.getZ() + z;
		position.set(newX, newY, newZ);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}