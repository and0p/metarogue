package io.metarogue.game.gameworld;

import java.util.ArrayList;
import java.util.List;

import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.client.view.threed.Vector2d;
import io.metarogue.client.view.threed.Vector3d;

public class GameObjectContainer {
	
	public Vector2d pos;
	
	public List<GameObject> units;

	public GameObjectContainer(int x, int z) {
		pos.set(x, z);
		units = new ArrayList<GameObject>();
		units.add(0, new GameObject(new Vector3d(0, 4, 0), "soldier"));
	}

}