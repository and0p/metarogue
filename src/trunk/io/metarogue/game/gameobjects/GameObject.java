package io.metarogue.game.gameobjects;

import java.util.HashMap;
import java.util.Map;

import io.metarogue.Main;
import io.metarogue.game.gameworld.GameClass;
import io.metarogue.client.view.threed.Vector3d;
import org.newdawn.slick.opengl.Texture;

public class GameObject {
	
	Vector3d position;
	String type;
    public boolean hasChangedChunkArrays = true;
    public boolean hasChangedChunks = true;
	
	Map<String, GameVariable> variables;
    GameClass gameclass;

    public Texture texture;

	public  GameObject(Vector3d position, String type) {
		this.position = position;
		this.type = type;
		variables = new HashMap<String, GameVariable>();
        variables.put("health", new GameVariable(0, 255, 200));
        variables.put("mana", new GameVariable(0, 255, 255));
        texture = Main.getGame().getTextureList().getTexture(type);
        int a = 0;
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

	public void move(Vector3d amount) {
		Vector3d newPosition = Vector3d.getDelta(position, amount);
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

    public GameVariable getVariableObject(String name) {
        if(variables.containsKey(name.toLowerCase())) return variables.get(name.toLowerCase());
        return null;
    }

    public int getVariable(String name) {
        if(variables.containsKey(name.toLowerCase())) return variables.get(name.toLowerCase()).get();
        return 0;
    }
	
}