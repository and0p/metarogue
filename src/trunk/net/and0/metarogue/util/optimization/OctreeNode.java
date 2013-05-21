package net.and0.metarogue.util.optimization;

import net.and0.metarogue.util.threed.Vector3d;

public class OctreeNode {
	
	private int maxDepth = 7;
	
	private int depth;
	private OctreeNode[] nodes;
	private Vector3d center;
	// private Rectangle bounds;
	   
	public OctreeNode(int depth, Vector3d center) {
		this.depth = depth;
		this.center = center;
	}

}