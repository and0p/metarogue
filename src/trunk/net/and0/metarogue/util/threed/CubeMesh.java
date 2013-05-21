package net.and0.metarogue.util.threed;

import java.util.ArrayList;
import java.util.List;

public class CubeMesh {
	
	public List<CubeSide> mesh;

	public CubeMesh() {
		mesh = new ArrayList<CubeSide>();
	}
	
	public List<CubeSide> returnMesh(){
		return mesh;
	}

}