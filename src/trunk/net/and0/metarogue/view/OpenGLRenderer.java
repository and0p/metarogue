package net.and0.metarogue.view;

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
import net.and0.metarogue.model.Camera;
import net.and0.metarogue.threed.CubeMesh;
import net.and0.metarogue.threed.CubeSide;
import net.and0.metarogue.threed.Vector3d;
import net.and0.metarogue.util.MortonCurve;
import net.and0.metarogue.util.settings.DisplaySettings;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.*;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class OpenGLRenderer {
	// Class that will contain display lists, etc, and render everything... hopefully?

	public Camera camera = new Camera(4, 9, 3, 6);
	public int numOfDisplayLists = 0;
	public static List<Integer> activeChunks;
	public static List <Integer> displayLists;
	
	Texture texture = null;
	Texture guitexture = null;
	
	public OpenGLRenderer() {
		
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
		
    	// Enable certain OpenGL functions / settings
		glFrontFace(GL_CW);
		glCullFace(GL_BACK);
		glEnable(GL_CULL_FACE);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glEnable(GL_TEXTURE_2D);
	    glColorMaterial ( GL_FRONT, GL_AMBIENT_AND_DIFFUSE );
	    glPointSize(5); // TODO probably dont need this
	    
	    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	    glEnable(GL_BLEND);
	    glDisable(GL_COLOR_MATERIAL);
	    
	    // Create some lighting, test stuff for now
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
	}

	public void render(World world){
		
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		// Clear screen and reset transformation stuff
        ready3d();
		texture.bind();
		// Set texture filtering parameters
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        
        // Transform through active camera
		GLU.gluLookAt(camera.position.x, camera.position.y, camera.position.z, 
		camera.target.x, camera.target.y, camera.target.z, 
				0, 1, 0);
	    
		//glBindTexture(GL_TEXTURE_2D, texture.getTextureID());
	    
        //TODO: eventually, cull based on bounding boxes of block chunks and create display list index
		
	    for(Integer i : displayLists) {
		    glCallList(i);
		}
		
        glBegin(GL_POINTS);
		for(GameObject i : world.worldObjects) {
	    		glVertex3f(i.getPosition().getX(), i.getPosition().getY(), i.getPosition().getZ());
		}
    	glEnd();
    	
    	ready2d();
    	guitexture.bind();
		// Set texture filtering parameters
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
	    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    	
        GUIRenderer.renderGUI(Main.gui);
    	
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
	
	void ready2d() {
	    glMatrixMode(GL_PROJECTION);
	    glLoadIdentity();
	    
	    GLU.gluOrtho2D( 0, Display.getWidth(), 0, Display.getHeight());
	    
	    glMatrixMode(GL_MODELVIEW);
	    glLoadIdentity();
	    
	    glDisable(GL_DEPTH_TEST);
		glDisable(GL_LIGHTING);
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