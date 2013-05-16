package net.and0.metarogue.main;

import java.io.IOException;
import java.util.Random;

import net.and0.metarogue.controller.InputParser;
import net.and0.metarogue.controller.ruby.RubyContainer;
import net.and0.metarogue.view.OpenGLRenderer;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.and0.metarogue.threed.*;
import net.and0.metarogue.model.GUI.Element;
import net.and0.metarogue.model.GUI.GUI;
import net.and0.metarogue.model.gameworld.GameObject;
import net.and0.metarogue.model.gameworld.World;
import net.and0.metarogue.util.FileUtil;

import org.jruby.embed.LocalContextScope;
import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.ScriptingContainer;

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
    	renderer = new OpenGLRenderer();	// Create create OpenGL context and renderer
		world = new World(4, 2, 16, 0);		// Create gameworld
		world.worldObjects.add(0, new GameObject(new Vector3d(5, 4, 5), "Soldier"));

		gui = new GUI();
		gui.elements.add(new Element(0, 200, 500, 200, 10));

        rubyContainer = new RubyContainer();

        initGameLogic();

		while(!org.lwjgl.opengl.Display.isCloseRequested()) {

            InputParser.parseInput();
            renderer.update(world);
			renderer.render(world);

		}

	}

}