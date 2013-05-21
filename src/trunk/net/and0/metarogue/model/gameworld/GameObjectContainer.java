package net.and0.metarogue.model.gameworld;

import java.util.ArrayList;
import java.util.List;

import net.and0.metarogue.util.threed.Vector2d;
import net.and0.metarogue.util.threed.Vector3d;

public class GameObjectContainer {
	
	public Vector2d pos;
	
	public List<GameObject> units;

	public GameObjectContainer(int x, int z) {
		pos.set(x, z);
		units = new ArrayList<GameObject>();
		units.add(0, new GameObject(new Vector3d(0, 4, 0), "soldier"));
	}

}