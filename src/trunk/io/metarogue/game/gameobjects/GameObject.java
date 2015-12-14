package io.metarogue.game.gameobjects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import io.metarogue.Main;
import io.metarogue.game.Player;
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

    // Boolean indicating if object should have world loaded around it at all times
    boolean active = false;
	
	Map<String, GameVariable> variables;

    public Texture texture;

    // Owning player, and controlling players
    int owner;
    HashSet<Integer> controllingPlayers;

	public GameObject(String type) {
		this.type = type;
        side = 0;
        owner = 0;
        controllingPlayers = new HashSet<Integer>();
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

    // Relocate this object to coordinates given
	public void setPosition(Vector3d newPosition) {
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

    public HashSet<Integer> getControllingPlayers() {
        return controllingPlayers;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

}