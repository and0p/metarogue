package io.metarogue.game.gameobjects;

import java.util.HashMap;
import java.util.Map;

import io.metarogue.Main;
import io.metarogue.game.timeline.animation.Displayable;
import io.metarogue.game.timeline.Event;
import io.metarogue.util.math.Vector3d;
import io.metarogue.util.math.Vector4d;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

public class GameObject implements Displayable {

	int id;
	Vector4d position;
	Vector3f displayPosition;
	String type;

    int side;

    public boolean hasChangedChunkArrays = true;
    public boolean hasChangedChunks = true;

    // Boolean indicating if object should have world loaded around it at all times
    boolean active = false;
	
	Map<String, GameVariable> variables;

    public Texture texture;

	public GameObject(String type) {
		this.type = type;
        side = 0;
		variables = new HashMap<String, GameVariable>();
        variables.put("health", new GameVariable(0, 255, 200));
        variables.put("mana", new GameVariable(0, 255, 255));
	}

    public void setWorld(int world) {
        if(position.getWorld() != world) {
            position.setWorld(world);
        }
    }

    public int getWorld() { return position.getWorld(); }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

	public Vector4d getPosition() {
		return position;
	}
    public Vector3d getPosition3d() { return position.getVector3d(); }

	public void setPosition(Vector4d position) { this.position = position; }
    public void setPosition3d(Vector3d position) { this.position.setVector3d(position); }

	// Method for easy relative movement
	public void move(int x, int y, int z) {
        Vector3d newPosition = new Vector3d(position.getVector3d().getX() + x, position.getVector3d().getY() + y, position.getVector3d().getZ() + z);
        // Check if positioned in different chunk
        hasChangedChunkArrays = Vector3d.isDifferentChunkArray(position.getVector3d(), newPosition);
        hasChangedChunks = Vector3d.isDifferentChunk(position.getVector3d(), newPosition);
		position.setVector3d(newPosition);
	}

	public void move(Vector3d amount) {
		// Make copy of position to modify. Need old position to compute chunk changes to load new world.
		Vector3d newPosition = position.getVector3d().copy();
		newPosition.move(amount);
		// Check if positioned in different chunk
		hasChangedChunkArrays = Vector3d.isDifferentChunkArray(position.getVector3d(), newPosition);
		hasChangedChunks = Vector3d.isDifferentChunk(position.getVector3d(), newPosition);
		position.setVector3d(newPosition);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

    public GameVariable getVariableObject(String name) {
        if(variables.containsKey(name.toLowerCase())) return variables.get(name.toLowerCase());
        return null;
    }

    public int getVariable(String name) {
        if(variables.containsKey(name.toLowerCase())) return variables.get(name.toLowerCase()).get();
        return 0;
    }

    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

	public Vector3f getDisplayPosition() { return displayPosition; }
	public void setDisplayPosition(Vector3f displayPosition) { this.displayPosition = displayPosition; }

    public int getSide() {
        return side;
    }

    public void setSide(int side) {
        this.side = side;
    }

}