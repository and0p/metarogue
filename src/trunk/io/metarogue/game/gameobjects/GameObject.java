package io.metarogue.game.gameobjects;

import java.util.HashMap;
import java.util.Map;

import io.metarogue.Main;
import io.metarogue.game.timeline.animation.Displayable;
import io.metarogue.game.timeline.Event;
import io.metarogue.util.math.Vector3d;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;

public class GameObject implements Displayable {

	int id;
	Vector3d position;
	Vector3f displayPosition;
	String type;

    int side;

    // World that this object is located in
    int world;

    public boolean hasChangedChunkArrays = true;
    public boolean hasChangedChunks = true;

    // Boolean indicating if object should have world loaded around it at all times
    boolean active = false;
	
	Map<String, GameVariable> variables;

    public Texture texture;

	public GameObject(String type) {
		this.type = type;
		variables = new HashMap<String, GameVariable>();
        variables.put("health", new GameVariable(0, 255, 200));
        variables.put("mana", new GameVariable(0, 255, 255));
	}

    public void setWorld(int world) {
        if(this.world != world) {
            this.world = world;
        }
    }

    public void setActiveStatus(boolean active) {
        this.active = active;
    }

    public boolean getActiveStatus() {
        return active;
    }

	public Vector3d getPosition() {
		return position;
	}

	public void setPosition(Vector3d position) {
        this.position = position;
        displayPosition = position.toFloat();
	}

	// Method for easy relative movement
	public void move(int x, int y, int z) {
        Vector3d newPosition = new Vector3d(position.getX() + x, position.getY() + y, position.getZ() + z);
        // Check if positioned in different chunk
        hasChangedChunkArrays = Vector3d.isDifferentChunkArray(position, newPosition);
        hasChangedChunks = Vector3d.isDifferentChunk(position, newPosition);
		position = newPosition;
	}

	public void move(Vector3d amount) {
		// Make copy of position to modify. Need old position to compute chunk changes to load new world.
		Vector3d newPosition = position.copy();
		newPosition.move(amount);
		// Check if positioned in different chunk
		hasChangedChunkArrays = Vector3d.isDifferentChunkArray(position, newPosition);
		hasChangedChunks = Vector3d.isDifferentChunk(position, newPosition);
		position = newPosition;
	}

	public void AddEvent(Event e) {
        Main.getGame().getStory().addEvent(e);
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