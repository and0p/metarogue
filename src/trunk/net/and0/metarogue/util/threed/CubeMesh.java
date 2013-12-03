package net.and0.metarogue.util.threed;

import net.and0.metarogue.main.Main;
import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import static org.lwjgl.opengl.GL11.*;

public class CubeMesh implements Callable<CubeMesh> {
	
	public List<CubeSide> mesh;
    public Vector3d position;

	public CubeMesh(Vector3d position) {
        this.position = position;
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
                    blockType = Main.getActiveWorld().getBlock(x + absX, y + absY, z + absZ);
                    if(blockType > 0) {
                        blockarray = Main.getActiveWorld().getAdjacentBlocks(x + absX, y + absY, z + absZ);
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
	}

    public CubeMesh() {
    }

    public CubeMesh call() {
        return this;
    }
	
	public List<CubeSide> returnMesh(){
		return mesh;
	}

    public void sendListToOpenGL(int displayListNumber) {
        glNewList(displayListNumber, GL_COMPILE);
        glPushMatrix();
        glPushAttrib(GL_CURRENT_BIT);
        glTranslatef(position.getX()*16, position.getY()*16, position.getZ()*16);
        glBegin(GL_QUADS);
        for (CubeSide cubeside : cubemesh.mesh) {
            glNormal3f(cubeside.normal.x, cubeside.normal.y, cubeside.normal.z);
            glTexCoord2f(cubeside.textureCoord[0].x,cubeside.textureCoord[0].y);
            glVertex3f(cubeside.corners[0].x, cubeside.corners[0].y, cubeside.corners[0].z);
            glTexCoord2f(cubeside.textureCoord[1].x,cubeside.textureCoord[1].y);
            glVertex3f(cubeside.corners[1].x, cubeside.corners[1].y, cubeside.corners[1].z);
            glTexCoord2f(cubeside.textureCoord[2].x,cubeside.textureCoord[2].y);
            glVertex3f(cubeside.corners[2].x, cubeside.corners[2].y, cubeside.corners[2].z);
            glTexCoord2f(cubeside.textureCoord[3].x,cubeside.textureCoord[3].y);
            glVertex3f(cubeside.corners[3].x, cubeside.corners[3].y, cubeside.corners[3].z);
        }
        glEnd();
        glPopAttrib();
        glPopMatrix();
        glEndList();
    }

}