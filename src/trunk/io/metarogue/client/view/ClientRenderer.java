package io.metarogue.client.view;

import static org.lwjgl.opengl.ARBPointSprite.*;
import static org.lwjgl.opengl.ARBPointParameters.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;

import io.metarogue.Main;
import io.metarogue.client.GameClient;
import io.metarogue.game.Camera;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.game.gameworld.World;
import io.metarogue.client.view.threed.*;
import io.metarogue.util.math.Vector3d;
import io.metarogue.util.settings.DisplaySettings;


import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.lwjgl.util.glu.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;


public class ClientRenderer {
	// Class that will contain display lists, etc, and render everything... hopefully?

    World world = null;

	public int numOfDisplayLists;

	public DisplayListBox dlBox;

    int glOffset;

    boolean worldSmallerThanView = false;

    Texture guiTexture;
    Texture worldTexture;
	
	public ClientRenderer() {

		// Create the display
		try {
			org.lwjgl.opengl.Display.setDisplayMode(new DisplayMode(DisplaySettings.resolutionX, DisplaySettings.resolutionY));
			org.lwjgl.opengl.Display.setTitle("metarogue");
			org.lwjgl.opengl.Display.create();
            loadLocalTextures();
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

        glAlphaFunc ( GL_GREATER, 0.1f );
        glEnable ( GL_ALPHA_TEST );

	    // Lighting, mostly test for now, one sky-light and some ambient

	    glEnable(GL_LIGHTING);
		FloatBuffer lModelAmbient = BufferUtils.createFloatBuffer(4);
		lModelAmbient.put(0.9f).put(0.9f).put(0.9f).put(1.0f).flip();
		FloatBuffer lightPosition = BufferUtils.createFloatBuffer(4);
		lightPosition.put(20f).put(20f).put(20f).put(1.0f).flip();
	    glLightModel(GL_LIGHT_MODEL_AMBIENT, lModelAmbient);
	    glLight(GL_LIGHT0, GL_POSITION, lightPosition);
		glEnable(GL_LIGHT0);
    }

    // Assign a gameworld to the renderer, clean up old world geometry if needed
    public void bindWorld(World w) {
        world = w;
        readyDisplayLists();
    }

    public void render() {
        preRender();
        if(dlBox != null) {
            update();
            renderWorld();
        }
        //renderGUI();
        finishRender();
    }

    public void preRender() {
        // Clear screen and reset transformation stuff
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void finishRender() {
        ready3d();
        readyCamera();
        Display.update();
        Display.sync(60);
    }

    public void updateWindow() {
        // Clear screen and reset transformation stuff
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        Display.update();
        Display.sync(60);
    }

    public void renderWorld() {
        ready3d();
        // BIND WORLD TEXTURE FROM WHEREVER
        bindTextureLoRes(worldTexture);
        // Transform through active camera UPDATE THEEEES
        readyCamera();
        // Start building meshes
        dlBox.buildFutures();
        // See if the meshes are ready to send to the video card yet, if so create the display lists
        dlBox.sendMeshes();

        //TODO: eventually, cull based on bounding boxes of block chunks and create display list index
        GL11.glListBase(glOffset); // need to make this offset of current displaylistbox, since ive decided to store meshses for more than one world if needed
        for(int i = 0; i < numOfDisplayLists; i++) {
            glCallList(i+glOffset);
        }

        readyFacing();

        if(world != null) {
            for(GameObject i : world.gameObjects.values()) {
                if(i.texture != null) bindTextureLoRes(i.texture);
                glBegin(GL_POINTS);
                glVertex3f(i.getDisplayPosition().getX(), i.getDisplayPosition().getY(), i.getDisplayPosition().getZ());
                glEnd();
            }
            GameClient client = Main.getClient();
            if(client.selectedBlock != null) {
                glVertex3f(client.selectedBlock.getX(), client.selectedBlock.getY()+1, client.selectedBlock.getZ());
            }
        }

        glEnd();
    }

    public void renderGUI(){
        ready2d();
        bindTextureLoRes(guiTexture);
        //Main.gui.render();
    }

    void readyDisplayLists() {
        int viewDist = DisplaySettings.minimumViewDistance;
        int dlSize = viewDist*2+1;
        numOfDisplayLists = dlSize*dlSize*dlSize;
        glOffset = GL11.glGenLists(numOfDisplayLists);
        if(dlBox != null) {
            dlBox.close();
        }
        dlBox = new DisplayListBox(world, new Vector3d(Main.getClient().getCurrentCamera().target).toChunkSpace(), viewDist);
    }

	void ready3d() {
	    glViewport(0, 0, Display.getWidth(), Display.getHeight());
	    glMatrixMode(GL_PROJECTION);
	    glLoadIdentity();
	    
		// Set up perspective
		//gluPerspective(70.0f, Display.getWidth() / Display.getHeight(), 0.1f, 1000f);
        gluPerspective(50.0f, 1.6666f, 0.1f, 1000f);

	    glMatrixMode(GL_MODELVIEW);
	    glLoadIdentity();

	    glDepthFunc(GL_LEQUAL);
	    glEnable(GL_DEPTH_TEST);
		glEnable(GL_LIGHTING);
        glEnableClientState(GL_VERTEX_ARRAY);
        glEnableClientState(GL_TEXTURE_COORD_ARRAY);
        glEnableClientState(GL_NORMAL_ARRAY);
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

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	    
	    glMatrixMode(GL_MODELVIEW);
	    glLoadIdentity();
	    
	    glDisable(GL_DEPTH_TEST);
		glDisable(GL_LIGHTING);
	}

    void readyCamera() {
        Camera c = Main.getClient().getCurrentCamera();
        if(c == null) {
            return;
        }
        c.rotateCamera(0, 0);
        GLU.gluLookAt(  c.position.x, c.position.y, c.position.z,
                        c.target.x, c.target.y, c.target.z,
                        c.upVector.getX(), c.upVector.getY(), c.upVector.getZ());
    }

    public static void bindTextureLoRes(Texture t) {
        t.bind();
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    }

    // Update elements within this OpenGL state
    public void update() {
        //dlBox.update(client.getPlayerObject().getPosition().toChunkSpace());
        dlBox.update(Main.getClient().getCurrentCamera().targetV3D().toChunkSpace());
    	world.updatedChunks.clear();
    }

    // Cleanly shut down any executor services used by components within this OpenGL state
    public void close() {
        dlBox.close();
        // Clear game-related textures from memory
        guiTexture.release();
        worldTexture.release();
    }

    void loadLocalTextures() {
        try {
            worldTexture = TextureLoader.getTexture("PNG", new FileInputStream(new File("C:/metarogue/world.png")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        }
        // Load the gui worldTexture file, test for now
        try {
            guiTexture = TextureLoader.getTexture("PNG", new FileInputStream(new File("C:/metarogue/font.png")));
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

}