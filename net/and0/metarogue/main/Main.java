package net.and0.metarogue.main;

import java.io.IOException;
import java.util.Random;
import org.lwjgl.*;
import org.lwjgl.opengl.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.and0.metarogue.threed.*;
import net.and0.metarogue.GUI.Element;
import net.and0.metarogue.GUI.GUI;
import net.and0.metarogue.gameworld.GameObject;
import net.and0.metarogue.gameworld.World;
import net.and0.metarogue.util.FileUtil;

import org.jruby.embed.LocalContextScope;
import org.jruby.embed.LocalVariableBehavior;
import org.jruby.embed.ScriptingContainer;

public class Main {
	
    public static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
   
    public static OpenGLRenderer renderer;
    public static World world;
    static ScriptingContainer container;
    public static GUI gui;
    static String rubyDir = System.getProperty("user.dir") + "\\rb\\default";

 public static void main(String[] args) throws IOException {
    	
		/////////////////////////
		// Initialization:
		/////////////////////////
    	renderer = new OpenGLRenderer();	// Create create OpenGL context and renderer
		world = new World(4, 2, 16, 0);		// Create gameworld
		world.worldObjects.add(0, new GameObject(new Vector3d(5, 4, 5), "Soldier"));
		
		gui = new GUI();
		gui.elements.add(new Element(0, 200, 500, 200, 10));

		/////////////////////////
		// TEST LOGIC:
		/////////////////////////
		
		// Create a random number generator
		Random randomGenerator = new Random();
		
		container = new ScriptingContainer(LocalContextScope.SINGLETON, LocalVariableBehavior.PERSISTENT);
		container.put("message", System.getProperty("user.dir") + "\\lib\\default");
		container.put("$world", world);
		String script = FileUtil.readFile(rubyDir + "/main.rb");
		Object receiver = container.runScriptlet(script);
		container.callMethod(receiver, "update", Object.class);
		
		// Lets throw some random blocks in there
		world.building = true;
		
		for(int i = 0; i < 500000; i++) {
			world.setBlock(1, 	randomGenerator.nextInt(world.absoluteResolution), 
								randomGenerator.nextInt(3), 
								randomGenerator.nextInt(world.absoluteResolution));
		}
		
		world.building = false;
		
		while(!org.lwjgl.opengl.Display.isCloseRequested()) {
			
			if(Mouse.isButtonDown(1)) {
				renderer.camera.rotateCamera(Mouse.getDX(), -Mouse.getDY());
			}
			renderer.camera.dollyCamera(-Mouse.getDWheel()*.02);
			
            if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                renderer.camera.rotateCamera(-1,0);
                System.out.print( 	renderer.camera.position.x + " " +
                					renderer.camera.position.y + " " + 
               				  		renderer.camera.position.z + " " +
               				  		renderer.camera.rot[0] + " " + renderer.camera.rot[1] + "\n");
            }
            
            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            	container.callMethod(receiver, "update", Object.class);
            }
            
            if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            	gui.elements.add(new Element(randomGenerator.nextInt(800), randomGenerator.nextInt(600), 50, 50, 6));
            }

    		renderer.update(world);
			renderer.render(world);
            
            if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
                Display.destroy();
                System.exit(0);
            }
		}
		
	}

}