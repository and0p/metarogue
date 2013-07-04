package net.and0.metarogue.controller;

import net.and0.metarogue.main.Main;
import net.and0.metarogue.model.gameworld.Chunk;
import net.and0.metarogue.model.gameworld.World;
import net.and0.metarogue.util.threed.CubeSide;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;

public class WorldVBOBuilder {

    public WorldVBOBuilder() {
        // Auto-generated constructor
    }

    public static FloatBuffer buildChunkVBO(World world, Chunk chunk) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(100000);
        int blockarray[] = new int[6];
        Vector3f pos = new Vector3f();
        int blockType = 0;
        int absX = chunk.absolutePosition[0];
        int absY = chunk.absolutePosition[1];
        int absZ = chunk.absolutePosition[2];
        for(int x = 0; x < 16; x++) {
            for(int y = 0; y < 16; y++) {
                for(int z = 0; z < 16; z++) {
                    blockType = world.getBlock(x+absX,y+absY,z+absZ);
                    if(blockType > 0) {
                        blockarray = world.getAdjacentBlocks(x+absX,y+absY,z+absZ);
                        pos.set(x, y, z);
                        for(int i = 0; i < 6; i++) {
                            if(blockarray[i] < 1 || blockarray[i] == 15) {
                                //addSide(pos, i, blockType, fb);
                                fb.put(addSidef(pos, i, blockType)); //TODO: passing floatbuffer by reference should be faster, see if that works
                            }
                        }
                    }
                }
            }
        }
        System.out.print(fb.position() +  "\n");
        fb.flip();
        System.out.print(fb.position() + "\n");
        return fb;
    }

    public static FloatBuffer addSidef(Vector3f pos, int direction, int type) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(48);
        float halfsize = 0.5f;
        float basex = 0;
        float basey = .25f;
        // clockwise from top left, 0-3
        // directions still x +-y+-y+-
        if(direction == 0) { //x +
            fb.put(pos.x + halfsize).put(pos.y + halfsize).put(pos.z + halfsize);
            fb.put(0).put(.25f);
            fb.put(1).put(0).put(0);
            fb.put(pos.x + halfsize).put(pos.y + halfsize).put(pos.z - halfsize);
            fb.put(.25f).put(.25f);
            fb.put(1).put(0).put(0);
            fb.put(pos.x + halfsize).put(pos.y - halfsize).put(pos.z - halfsize);
            fb.put(.25f).put(.5f);
            fb.put(1).put(0).put(0);
            fb.put(pos.x + halfsize).put(pos.y - halfsize).put(pos.z - halfsize);
            fb.put(.25f).put(.5f);
            fb.put(1).put(0).put(0);
            fb.put(pos.x + halfsize).put(pos.y - halfsize).put(pos.z + halfsize);
            fb.put(0).put(.5f);
            fb.put(1).put(0).put(0);
            fb.put(pos.x + halfsize).put(pos.y + halfsize).put(pos.z + halfsize);
            fb.put(0).put(.25f);
            fb.put(1).put(0).put(0);
        }
        if(direction == 1) { //x -
            fb.put(pos.x - halfsize).put(pos.y + halfsize).put(pos.z - halfsize);
            fb.put(0).put(.25f);
            fb.put(-1).put(0).put(0);
            fb.put(pos.x - halfsize).put(pos.y + halfsize).put(pos.z + halfsize);
            fb.put(.25f).put(.25f);
            fb.put(-1).put(0).put(0);
            fb.put(pos.x - halfsize).put(pos.y - halfsize).put(pos.z + halfsize);
            fb.put(.25f).put(.5f);
            fb.put(-1).put(0).put(0);
            fb.put(pos.x - halfsize).put(pos.y - halfsize).put(pos.z + halfsize);
            fb.put(.25f).put(.5f);
            fb.put(-1).put(0).put(0);
            fb.put(pos.x - halfsize).put(pos.y - halfsize).put(pos.z - halfsize);
            fb.put(0).put(.5f);
            fb.put(-1).put(0).put(0);
            fb.put(pos.x - halfsize).put(pos.y + halfsize).put(pos.z - halfsize);
            fb.put(0).put(.25f);
            fb.put(-1).put(0).put(0);
        }
        if(direction == 2) { //y+
            fb.put(pos.x - halfsize).put(pos.y + halfsize).put(pos.z - halfsize);
            fb.put(0).put(0);
            fb.put(0).put(1).put(0);
            fb.put(pos.x + halfsize).put(pos.y + halfsize).put(pos.z - halfsize);
            fb.put(.25f).put(0);
            fb.put(0).put(1).put(0);
            fb.put(pos.x + halfsize).put(pos.y + halfsize).put(pos.z + halfsize);
            fb.put(.25f).put(.25f);
            fb.put(0).put(1).put(0);
            fb.put(pos.x + halfsize).put(pos.y + halfsize).put(pos.z + halfsize);
            fb.put(.25f).put(.25f);
            fb.put(0).put(1).put(0);
            fb.put(pos.x - halfsize).put(pos.y + halfsize).put(pos.z + halfsize);
            fb.put(0).put(.25f);
            fb.put(0).put(1).put(0);
            fb.put(pos.x - halfsize).put(pos.y + halfsize).put(pos.z - halfsize);
            fb.put(0).put(0);
            fb.put(0).put(1).put(0);
        }
        if(direction == 3) {  //y-
            fb.put(pos.x - halfsize).put(pos.y - halfsize).put(pos.z + halfsize);
            fb.put(0).put(.5f);
            fb.put(0).put(-1).put(0);
            fb.put(pos.x + halfsize).put(pos.y - halfsize).put(pos.z + halfsize);
            fb.put(.25f).put(.5f);
            fb.put(0).put(-1).put(0);
            fb.put(pos.x + halfsize).put(pos.y - halfsize).put(pos.z - halfsize);
            fb.put(.25f).put(.75f);
            fb.put(0).put(-1).put(0);
            fb.put(pos.x + halfsize).put(pos.y - halfsize).put(pos.z - halfsize);
            fb.put(.25f).put(.75f);
            fb.put(0).put(-1).put(0);
            fb.put(pos.x - halfsize).put(pos.y - halfsize).put(pos.z - halfsize);
            fb.put(0).put(.75f);
            fb.put(0).put(-1).put(0);
            fb.put(pos.x - halfsize).put(pos.y - halfsize).put(pos.z + halfsize);
            fb.put(0).put(.5f);
            fb.put(0).put(-1).put(0);
        }
        if(direction == 4) { //z+
            fb.put(pos.x - halfsize).put(pos.y + halfsize).put(pos.z + halfsize);
            fb.put(0).put(.25f);
            fb.put(0).put(0).put(1);
            fb.put(pos.x + halfsize).put(pos.y + halfsize).put(pos.z + halfsize);
            fb.put(.25f).put(.25f);
            fb.put(0).put(0).put(1);
            fb.put(pos.x + halfsize).put(pos.y - halfsize).put(pos.z + halfsize);
            fb.put(.25f).put(.5f);
            fb.put(0).put(0).put(1);
            fb.put(pos.x + halfsize).put(pos.y - halfsize).put(pos.z + halfsize);
            fb.put(.25f).put(.5f);
            fb.put(0).put(0).put(1);
            fb.put(pos.x - halfsize).put(pos.y - halfsize).put(pos.z + halfsize);
            fb.put(0).put(.5f);
            fb.put(0).put(0).put(1);
            fb.put(pos.x - halfsize).put(pos.y + halfsize).put(pos.z + halfsize);
            fb.put(0).put(.25f);
            fb.put(0).put(0).put(1);
        }
        if(direction == 5) { //z-
            fb.put(pos.x + halfsize).put(pos.y + halfsize).put(pos.z - halfsize);
            fb.put(0).put(.25f);
            fb.put(0).put(0).put(-1);
            fb.put(pos.x - halfsize).put(pos.y + halfsize).put(pos.z - halfsize);
            fb.put(.25f).put(.25f);
            fb.put(0).put(0).put(-1);
            fb.put(pos.x - halfsize).put(pos.y - halfsize).put(pos.z - halfsize);
            fb.put(.25f).put(.5f);
            fb.put(0).put(0).put(-1);
            fb.put(pos.x - halfsize).put(pos.y - halfsize).put(pos.z - halfsize);
            fb.put(.25f).put(.5f);
            fb.put(0).put(0).put(-1);
            fb.put(pos.x + halfsize).put(pos.y - halfsize).put(pos.z - halfsize);
            fb.put(0).put(.5f);
            fb.put(0).put(0).put(-1);
            fb.put(pos.x + halfsize).put(pos.y + halfsize).put(pos.z - halfsize);
            fb.put(0).put(.25f);
            fb.put(0).put(0).put(-1);
        }
        fb.flip();
        return fb;
    }


    public static void addSide(Vector3f pos, int direction, int type, FloatBuffer fb) {
        float halfsize = 0.5f;
        float basex = 0;
        float basey = .25f;
        // clockwise from top left, 0-3
        // directions still x +-y+-y+-
        if(direction == 0) { //x +
            fb.put(pos.x + halfsize).put(pos.y + halfsize).put(pos.z + halfsize);
            fb.put(pos.x + halfsize).put(pos.y + halfsize).put(pos.z - halfsize);
            fb.put(pos.x + halfsize).put(pos.y - halfsize).put(pos.z - halfsize);
            fb.put(pos.x + halfsize).put(pos.y - halfsize).put(pos.z + halfsize);
            fb.put(0).put(.25f);
            fb.put(.25f).put(.25f);
            fb.put(.25f).put(.5f);
            fb.put(0).put(.5f);
            fb.put(1).put(0).put(0);
            fb.put(1).put(0).put(0);
            fb.put(1).put(0).put(0);
            fb.put(1).put(0).put(0);
        }
        if(direction == 1) { //x -
            fb.put(pos.x - halfsize).put(pos.y + halfsize).put(pos.z - halfsize);
            fb.put(pos.x - halfsize).put(pos.y + halfsize).put(pos.z + halfsize);
            fb.put(pos.x - halfsize).put(pos.y - halfsize).put(pos.z + halfsize);
            fb.put(pos.x - halfsize).put(pos.y - halfsize).put(pos.z - halfsize);
            fb.put(0).put(.25f);
            fb.put(.25f).put(.25f);
            fb.put(.25f).put(.5f);
            fb.put(0).put(.5f);
            fb.put(-1).put(0).put(0);
            fb.put(-1).put(0).put(0);
            fb.put(-1).put(0).put(0);
            fb.put(-1).put(0).put(0);
        }
        if(direction == 2) { //y+
            fb.put(pos.x - halfsize).put(pos.y + halfsize).put(pos.z - halfsize);
            fb.put(pos.x + halfsize).put(pos.y + halfsize).put(pos.z - halfsize);
            fb.put(pos.x + halfsize).put(pos.y + halfsize).put(pos.z + halfsize);
            fb.put(pos.x - halfsize).put(pos.y + halfsize).put(pos.z + halfsize);
            fb.put(0).put(0);
            fb.put(.25f).put(0);
            fb.put(.25f).put(.25f);
            fb.put(0).put(.25f);
            fb.put(0).put(1).put(0);
            fb.put(0).put(1).put(0);
            fb.put(0).put(1).put(0);
            fb.put(0).put(1).put(0);
        }
        if(direction == 3) {  //y-
            fb.put(pos.x - halfsize).put(pos.y - halfsize).put(pos.z + halfsize);
            fb.put(pos.x + halfsize).put(pos.y - halfsize).put(pos.z + halfsize);
            fb.put(pos.x + halfsize).put(pos.y - halfsize).put(pos.z - halfsize);
            fb.put(pos.x - halfsize).put(pos.y - halfsize).put(pos.z - halfsize);
            fb.put(0).put(.5f);
            fb.put(.25f).put(.5f);
            fb.put(.25f).put(.75f);
            fb.put(0).put(.75f);
            fb.put(0).put(-1).put(0);
            fb.put(0).put(-1).put(0);
            fb.put(0).put(-1).put(0);
            fb.put(0).put(-1).put(0);
        }
        if(direction == 4) { //z+
            fb.put(pos.x - halfsize).put(pos.y + halfsize).put(pos.z + halfsize);
            fb.put(pos.x + halfsize).put(pos.y + halfsize).put(pos.z + halfsize);
            fb.put(pos.x + halfsize).put(pos.y - halfsize).put(pos.z + halfsize);
            fb.put(pos.x - halfsize).put(pos.y - halfsize).put(pos.z + halfsize);
            fb.put(0).put(.25f);
            fb.put(.25f).put(.25f);
            fb.put(.25f).put(.5f);
            fb.put(0).put(.5f);
            fb.put(0).put(0).put(1);
            fb.put(0).put(0).put(1);
            fb.put(0).put(0).put(1);
            fb.put(0).put(0).put(1);
        }
        if(direction == 5) { //z-
            fb.put(pos.x + halfsize).put(pos.y + halfsize).put(pos.z - halfsize);
            fb.put(pos.x - halfsize).put(pos.y + halfsize).put(pos.z - halfsize);
            fb.put(pos.x - halfsize).put(pos.y - halfsize).put(pos.z - halfsize);
            fb.put(pos.x + halfsize).put(pos.y - halfsize).put(pos.z - halfsize);
            fb.put(0).put(.25f);
            fb.put(.25f).put(.25f);
            fb.put(.25f).put(.5f);
            fb.put(0).put(.5f);
            fb.put(0).put(0).put(-1);
            fb.put(0).put(0).put(-1);
            fb.put(0).put(0).put(-1);
            fb.put(0).put(0).put(-1);
        }
    }

}
