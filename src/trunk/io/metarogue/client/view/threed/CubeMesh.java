package io.metarogue.client.view.threed;

import io.metarogue.Main;
import io.metarogue.util.math.Vector3d;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class CubeMesh implements Callable<CubeMesh> {
	
	public List<CubeSide> mesh;
    public Vector3d position;

	public CubeMesh(Vector3d position) {
        this.position = position;
    }

    public CubeMesh() {
    }

    @Override
    public CubeMesh call() {
        mesh = new ArrayList<CubeSide>();
        int blockarray[] = new int[6];
        Vector3f pos = new Vector3f();
        int blockType = 0;
        int absX = position.getX()*16;
        int absY = position.getY()*16;
        int absZ = position.getZ()*16;
        for(int x = 0; x < 16; x++) {
            for(int y = 0; y < 16; y++) {
                for(int z = 0; z < 16; z++) {
                    blockType = Main.getClient().getActiveWorld().getBlock(x + absX, y + absY, z + absZ);
                    if(blockType > 0) {
                        blockarray = Main.getClient().getActiveWorld().getAdjacentBlocks(x + absX, y + absY, z + absZ);
                        pos.set(x, y, z);
                        for(int i = 0; i < 6; i++) {
                            if(blockarray[i] < 1 || blockarray[i] == 255) {
                                mesh.add(new CubeSide(pos, 1, i, blockType));
                            }
                        }
                    }
                }
            }
        }
        return this;
    }
	
	public List<CubeSide> returnMesh(){
		return mesh;
	}

}