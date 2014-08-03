package io.metarogue.client.view.threed;

// Class to hold display list position and worldmorton, possibly

import io.metarogue.game.gameworld.Chunk;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.lwjgl.opengl.GL11.*;

public class DisplayList {

    Vector3d position;
    int displayListNumber;
    DisplayListBox parent;
    Future<CubeMesh> cubeMeshFuture;

    Chunk chunk;

    public DisplayList(Vector3d vector3d) {
        this.position = vector3d;
    }

    public DisplayList(int x, int y, int z, int displayListNumber, DisplayListBox parent) {
        position = new Vector3d(x,y,z);
        this.displayListNumber = displayListNumber;
        this.parent = parent;
    }

    public CubeMesh buildCubeMesh() {
        CubeMesh cubemesh = new CubeMesh(position);
        return cubemesh;
    }

    public void buildFutureCubeMesh() {
        cubeMeshFuture = parent.pool.submit(new CubeMesh(position));
    }

    public void sendListToOpenGL() {
        CubeMesh cubemesh = null;
        try {
            cubemesh = cubeMeshFuture.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return;
        }
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