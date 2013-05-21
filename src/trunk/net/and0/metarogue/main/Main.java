package net.and0.metarogue.main;

import java.io.IOException;
import java.util.Random;

import net.and0.metarogue.controller.InputParser;
import net.and0.metarogue.controller.Picker;
import net.and0.metarogue.controller.ruby.RubyContainer;
import net.and0.metarogue.util.GLUtilities;
import net.and0.metarogue.view.OpenGLRenderer;
import org.lwjgl.*;

import net.and0.metarogue.util.threed.*;
import net.and0.metarogue.model.GUI.Element;
import net.and0.metarogue.model.GUI.GUI;
import net.and0.metarogue.model.gameworld.GameObject;
import net.and0.metarogue.model.gameworld.World;
import org.lwjgl.input.Mouse;

import static org.lwjgl.opengl.GL11.glVertex3f;

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

    public static void initGameLogic() {

        // Create a random number generator
        randomGenerator = new Random();

        // Lets throw some random blocks in there
        world.building = true;

        for(int i = 0; i < 500000; i++) {
            world.setBlock(1, 	randomGenerator.nextInt(world.absoluteResolution),
                    randomGenerator.nextInt(3),
                    randomGenerator.nextInt(world.absoluteResolution));
        }

        world.building = false;

    }

    public static void runGameLogic() {

    }

 public static void main(String[] args) throws IOException {

		// Initialization:
    	renderer = new OpenGLRenderer(getActiveWorld());	// Create create OpenGL context and renderer
		world = new World(4, 2, 16, 0);		// Create gameworld
		world.worldObjects.add(0, new GameObject(new Vector3d(32, 4, 32), "Soldier"));

		gui = new GUI();
		gui.addElement(new Element(50, 50, 100, 100, 10));
        gui.addElement(new Element(150, 150, 100, 100, 10));
        gui.addElement(new Element(300, 300, 100, 100, 10));
        gui.bullshitAddTest();
        gui.update();

        rubyContainer = new RubyContainer();

        initGameLogic();

		while(!org.lwjgl.opengl.Display.isCloseRequested()) {

            getActiveWorld().selectedBlock = Picker.pickBlock(getActiveWorld());
            InputParser.parseInput();
            renderer.update(world);
			renderer.render(world);

		}

	}

}