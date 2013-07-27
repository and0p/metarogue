package net.and0.metarogue.view;

import net.and0.metarogue.main.Main;
import net.and0.metarogue.model.gameworld.Chunk;
import net.and0.metarogue.model.gameworld.World;
import net.and0.metarogue.util.threed.CubeMesh;
import net.and0.metarogue.util.threed.CubeSide;
import net.and0.metarogue.util.threed.Vector3d;
import org.lwjgl.util.vector.Vector3f;

import static org.lwjgl.opengl.GL11.*;

/**
 * MetaRogue class
 * User: andrew
 * Date: 7/10/13
 * Time: 6:27 PM
 */
public class DisplayListBuilder {

    public DisplayListBuilder() {
        // Auto-generated constructor
    }

    public static void buildCubeDisplayList(int listNum, World world, int posX, int posY, int posZ) {
        CubeMesh cubemesh = buildMesh(world, world.getChunk(posX, posY, posZ));
        glNewList(listNum, GL_COMPILE);
        glPushMatrix();
        glPushAttrib(GL_CURRENT_BIT);
        glTranslatef(posX*world.chunkResolution, posY*world.chunkResolution, posZ*world.chunkResolution);
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

    public static void buildCubeDisplayList(int listNum, World world, Vector3d vec3) {
        int posX = vec3.getX(); int posY = vec3.getY(); int posZ = vec3.getZ();
        Chunk chunk = world.getChunk(posX, posY, posZ);
        if(chunk == null) return;
        CubeMesh cubemesh;
        cubemesh = buildMesh(world, chunk);
        glNewList(listNum, GL_COMPILE);
        glPushMatrix();
        glPushAttrib(GL_CURRENT_BIT);
        glTranslatef(posX*world.chunkResolution, posY*world.chunkResolution, posZ*world.chunkResolution);
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

    public static CubeMesh buildMesh(World world, Chunk chunk) {
        CubeMesh cubemesh = new CubeMesh();
        int blockarray[] = new int[6];
        Vector3f pos = new Vector3f();
        int blockType = 0;
        int absX = chunk.absolutePosition[0];
        int absY = chunk.absolutePosition[1];
        int absZ = chunk.absolutePosition[2];
        for(int x = 0; x < world.chunkResolution; x++) {
            for(int y = 0; y < world.chunkResolution; y++) {
                for(int z = 0; z < world.chunkResolution; z++) {
                    blockType = Main.world.getBlock(x+absX,y+absY,z+absZ);
                    if(blockType > 0) {
                        blockarray = Main.world.getAdjacentBlocks(x+absX,y+absY,z+absZ);
                        pos.set(x, y, z);
                        for(int i = 0; i < 6; i++) {
                            if(blockarray[i] < 1 || blockarray[i] == 15) {
                                cubemesh.mesh.add(new CubeSide(pos, 1, i));
                            }
                        }
                    }
                }
            }
        }
        return cubemesh;
    }

}
