package net.and0.metarogue.main;

import java.io.IOException;
import java.util.Random;

import net.and0.metarogue.controller.GUI.GUIBuilder;
import net.and0.metarogue.controller.GUI.GUIUpdater;
import net.and0.metarogue.controller.InputParser;
import net.and0.metarogue.controller.Picker;
import net.and0.metarogue.controller.WorldManager;
import net.and0.metarogue.controller.ruby.RubyContainer;
import net.and0.metarogue.view.OpenGLRenderer;
import org.lwjgl.*;


import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.SpriteSheetFont;

import net.and0.metarogue.util.threed.*;
import net.and0.metarogue.model.GUI.GUIElement;
import net.and0.metarogue.model.GUI.GUI;
import net.and0.metarogue.model.gameworld.GameObject;
import net.and0.metarogue.model.gameworld.World;

public class Main {
	
    public static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    public static GUI getActiveGui() { return gui; }
    public static World getActiveWorld() { return world; }
    public static RubyContainer getRubyContainer() { return rubyContainer; }
   
    public static OpenGLRenderer renderer;
    public static World world;
    public static GUI gui;
    public static Random randomGenerator;
    public static RubyContainer rubyContainer;
    public static int i = 0;

    //SpriteSheet spritesheet = new SpriteSheet();

    public static void initGameLogic() {

        // Create a random number generator
        randomGenerator = new Random();

        // Lets throw some random blocks in there
        world.building = true;

        for(int i = 0; i < 200; i++) {
            world.setBlock(1, 	randomGenerator.nextInt(world.absoluteResolution),
                    randomGenerator.nextInt(world.absoluteHeight),
                    randomGenerator.nextInt(world.absoluteResolution));
        }

        world.building = false;

    }

    public static void runGameLogic() {

    }

 public static void main(String[] args) throws IOException {

		// Initialization:
    	renderer = new OpenGLRenderer(getActiveWorld());	// Create create OpenGL context and renderer
		world = new World(8, 8, 0);		                // Create gameworld
		world.worldObjects.add(0, new GameObject(new Vector3d(32, 4, 32), "Soldier"));
        for(int i = 1; i < 9; i++) {
            //world.worldObjects.add(i, new GameObject(new Vector3d(randomGenerator.nextInt(world.absoluteResolution), 4, randomGenerator.nextInt(world.absoluteResolution)), "Soldier"));
            world.worldObjects.add(i, new GameObject(new Vector3d(32, 4, 32), "Soldier"));
        }

		gui = new GUI();
        gui = GUIBuilder.buildGUI();
		// gui.addElement(new GUIElement(0, 0, 500, 300, 10, true));
        // gui.bullshitAddTest();

        rubyContainer = new RubyContainer();

        initGameLogic();

		while(!org.lwjgl.opengl.Display.isCloseRequested()) {

            // WorldManager.updateChunks(getActiveWorld());
            getActiveWorld().selectedBlock = Picker.pickBlock(getActiveWorld());
            InputParser.parseInput();
            GUIUpdater.updateGUI(getActiveGui());
            renderer.updatevbos(world);
			renderer.render(world);

		}

	}

}