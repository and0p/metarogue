package net.and0.metarogue.view;

import static org.lwjgl.opengl.ARBPointSprite.*;
import static org.lwjgl.opengl.ARBPointParameters.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import net.and0.metarogue.model.gameworld.Chunk;
import net.and0.metarogue.model.gameworld.GameObject;
import net.and0.metarogue.model.gameworld.World;
import net.and0.metarogue.main.Main;
import net.and0.metarogue.util.GLUtilities;
import net.and0.metarogue.util.threed.CubeMesh;
import net.and0.metarogue.util.threed.CubeSide;
import net.and0.metarogue.util.threed.Ray;
import net.and0.metarogue.util.threed.Vector3d;
import net.and0.metarogue.util.MortonCurve;
import net.and0.metarogue.util.settings.DisplaySettings;


import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ARBPointSprite;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.*;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.SpriteSheetFont;

public class OpenGLRenderer {
	// Class that will contain display lists, etc, and render everything... hopefully?

	public int numOfDisplayLists = 0;
	public static List<Integer> activeChunks;
	public static List <Integer> displayLists;
	
	Texture texture = null;
	Texture guitexture = null;
    Texture unittexture = null;
    Texture fontsprite = null;
    SpriteSheet fontsheet = null;
    SpriteSheetFont font = null;
    Image fontspriteimage = null;
	
	public OpenGLRenderer(World world) {
		
		activeChunks = new ArrayList<Integer>();
		displayLists = new ArrayList<Integer>();
		
		// Create the display
		try {
			org.lwjgl.opengl.Display.setDisplayMode(new DisplayMode(DisplaySettings.resolutionX, DisplaySettings.resolutionY));
			org.lwjgl.opengl.Display.setTitle("OpenGL");
			org.lwjgl.opengl.Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(0);
		}

		// Initialize OpenGL
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);	// Set clear color
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		ready3d();
		

        /*
        OPEN GL Settings
         */

        // Culling

		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);

        // Textures / colors
		glEnable(GL_TEXTURE_2D);
	    glColorMaterial ( GL_FRONT, GL_AMBIENT_AND_DIFFUSE );
        glDisable(GL_COLOR_MATERIAL); // Ultimately disabling this, for now.

        // Alpha for textures
	    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	    glEnable(GL_BLEND);

        // Basic point-sprite stuff. TODO: Likely will want to replace this with a more custom facing/billboard code

        float[] dist = new float[]{0.0f, 0f, 0.01f, 0.0f};
        FloatBuffer distBuffer = BufferUtils.createFloatBuffer(dist.length);
        distBuffer.clear();
        distBuffer.put(dist);
        distBuffer.compact();

        glPointParameterfARB(GL_POINT_SIZE_MIN_ARB, 0.0f);
        glPointParameterfARB(GL_POINT_SIZE_MAX_ARB, 500.0f);
        glPointParameterARB(GL_POINT_DISTANCE_ATTENUATION_ARB, distBuffer);
        glTexEnvi(GL_POINT_SPRITE_ARB, GL_COORD_REPLACE_ARB, GL_TRUE);

        glPointSize(100.0f);

        // Alpha blending options for transparent pixels in textures

        glAlphaFunc ( GL_GREATER, 0.1f ) ;
        glEnable ( GL_ALPHA_TEST ) ;

	    // Lighting, mostly test for now, one sky-light and some ambient

	    glEnable(GL_LIGHTING);
		FloatBuffer lModelAmbient = BufferUtils.createFloatBuffer(4);
		lModelAmbient.put(0.9f).put(0.9f).put(0.9f).put(1.0f).flip();
		FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
		lightPosition.put(20f).put(20f).put(20f).put(1.0f).flip();
	    glLightModel(GL_LIGHT_MODEL_AMBIENT, lModelAmbient);
	    glLight(GL_LIGHT0, GL_POSITION, lightPosition);
		glEnable(GL_LIGHT0);

		// Load the block texture file, test for now
		try {
			texture = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/fezfull.png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
		// Load the gui texture file, test for now
		try {
			guitexture = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/window.png")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			Display.destroy();
			System.exit(1);
		}
        // Load the gui texture file, test for now
        try {
            unittexture = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/soldier.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        }
        // Load the font spritesheet, test for now
        try {
            fontsprite = TextureLoader.getTexture("PNG", new FileInputStream(new File("res/font.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        }

        //fontspriteimage = new Image(fontsprite);
        try {
            fontsheet = new SpriteSheet("res/fontflip.png", 18, 24);
        } catch (SlickException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        font = new SpriteSheetFont(fontsheet, ' ');
    }

	public void render(World world){
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		// Clear screen and reset transformation stuff
        ready3d();
		bindTextureLoRes(texture);
        
        // Transform through active camera
        readyCamera(world);

        //TODO: eventually, cull based on bounding boxes of block chunks and create display list index
		
	    for(Integer i : displayLists) {
		    glCallList(i);
		}

        readyFacing();
        bindTextureLoRes(unittexture);
        glBegin(GL_POINTS);
		for(GameObject i : world.worldObjects) {
	    		glVertex3f(i.getPosition().getX(), i.getPosition().getY(), i.getPosition().getZ());
		}
        if(world.selectedBlock != null) {
            glVertex3f(world.selectedBlock.getX(), world.selectedBlock.getY()+1, world.selectedBlock.getZ());
        }
        //glVertex3f(world.floatyThing.getX(), world.floatyThing.getY(), world.floatyThing.getY());
    	glEnd();
    	
    	ready2d();
    	bindTextureLoRes(guitexture);

        GUIRenderer.renderGUI(Main.gui);
        font.drawString(10, 10, "What a wonderful test for this bullshit font! I have AIDS? 1,000,000,000.");

        ready3d();
        readyCamera(world);
    	
        Display.update();
        Display.sync(60);
	}
	
	void ready3d() {
	    //glViewport(0, 0, Display.getWidth(), Display.getHeight());
	    glMatrixMode(GL_PROJECTION);
	    glLoadIdentity();
	    
		// Set up perspective
		gluPerspective(70.0f, Display.getWidth() / Display.getHeight(), 0.1f, 1000f);

	    glMatrixMode(GL_MODELVIEW);
	    glLoadIdentity();

	    glDepthFunc(GL_LEQUAL);
	    glEnable(GL_DEPTH_TEST);
		glEnable(GL_LIGHTING);
	}

    void readyFacing() {
        glEnable(ARBPointSprite.GL_POINT_SPRITE_ARB);
        glEnable(GL_POINT_SPRITE_ARB);
        glDisable(GL_LIGHTING);
    }
	
	void ready2d() {
	    glMatrixMode(GL_PROJECTION);
	    glLoadIdentity();
	    
	    GLU.gluOrtho2D(0, Display.getWidth(), 0, Display.getHeight());
	    
	    glMatrixMode(GL_MODELVIEW);
	    glLoadIdentity();
	    
	    glDisable(GL_DEPTH_TEST);
		glDisable(GL_LIGHTING);
	}

    void readyCamera(World world) {
        GLU.gluLookAt(world.getActiveCamera().position.x, world.getActiveCamera().position.y, world.getActiveCamera().position.z,
                world.getActiveCamera().target.x, world.getActiveCamera().target.y, world.getActiveCamera().target.z,
                world.getActiveCamera().upVector.getX(), world.getActiveCamera().upVector.getY(), world.getActiveCamera().upVector.getZ());
    }

    void bindTextureLoRes(Texture t) {
        t.bind();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
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

//    public static FloatBuffer buildVBO(World world, Chunk chunk) {
//        FloatBuffer fb = BufferUtils.createFloatBuffer(24576);
//        int blockarray[] = new int[6];
//        Vector3f pos = new Vector3f();
//        int blockType = 0;
//        int absX = chunk.absolutePosition[0];
//        int absY = chunk.absolutePosition[1];
//        int absZ = chunk.absolutePosition[2];
//        for(int x = 0; x < world.chunkResolution; x++) {
//            for(int y = 0; y < world.chunkResolution; y++) {
//                for(int z = 0; z < world.chunkResolution; z++) {
//                    blockType = Main.world.getBlock(x+absX,y+absY,z+absZ);
//                    if(blockType > 0) {
//                        blockarray = Main.world.getAdjacentBlocks(x+absX,y+absY,z+absZ);
//                        pos.set(x, y, z);
//                        for(int i = 0; i < 6; i++) {
//                            if(blockarray[i] < 3 || blockarray[i] == 15) {
//                                fb.mesh.add(new CubeSide(pos, 1, i));
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return cubemesh;
//    }
	
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
    
    public static void update(World world) {
//    	activeChunks.clear();
//    	for(Vector2d vector2d : world.activeChunkArrays2d) {
//    		for(int i = 0; i < world.worldHeight; i++) {
//    			activeChunks.add((MortonCurve.getMorton(vector2d.x, vector2d.z)*world.worldHeight)+i);
//    		}
//    	}
    	for(Vector3d vec3 : world.updatedChunks) {
    		int currentDisplayList = (MortonCurve.getMorton(vec3.getX(), vec3.getZ())*world.worldHeight)+vec3.getY()+1;
    		buildCubeDisplayList(currentDisplayList, world, vec3.getX(), vec3.getY(), vec3.getZ());
    		displayLists.add(currentDisplayList);
    	}
    	world.updatedChunks.clear();
    }
}