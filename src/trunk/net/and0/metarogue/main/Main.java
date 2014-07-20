package net.and0.metarogue.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;

import net.and0.metarogue.controller.DBLoader;
import net.and0.metarogue.controller.GUI.GUIUpdater;
import net.and0.metarogue.controller.InputParser;
import net.and0.metarogue.controller.Picker;
import net.and0.metarogue.controller.WorldManager;
import net.and0.metarogue.controller.ruby.RubyContainer;
import net.and0.metarogue.model.Camera;
import net.and0.metarogue.model.Game;
import net.and0.metarogue.util.settings.WorldSettings;
import net.and0.metarogue.view.GUI.GUIElement;
import net.and0.metarogue.view.OpenGLRenderer;
import org.lwjgl.*;


import net.and0.metarogue.util.threed.*;
import net.and0.metarogue.view.GUI.GUI;
import net.and0.metarogue.model.gameobjects.GameObject;
import net.and0.metarogue.model.gameworld.World;

public class Main {
	
    public static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    public static Game game;
    public static World activeWorld;

    public static GUI getBaseGUI() { return gui; }
    public static GUI getDialogGUI() {return dialogGui; }
    public static World getActiveWorld() { return activeWorld; }
    public static DBLoader getActiveDB() { return game.dbLoader; }
    public static RubyContainer getRubyContainer() { return rubyContainer; }
   
    public static OpenGLRenderer renderer;
    public static World world;
    public static GUI gui;
    public static GUI dialogGui;
    public static RubyContainer rubyContainer;

    public static Camera camera = new Camera(10,0,0,0);
    public static GUIElement selectedGUIElement = null;
    public static GUIElement highlightedGUIElement = null;
    public static GameObject selectedGameObject = null;
    public static GameObject highlightedGameObject = null;
    public static GameObject playerGameObject = null;
    public static Vector3d selectedBlock;

    // Debug, sample dynamic GUI
    public static GUI dgui;
    HashMap<String, GUI> guis;

    public static String everything;


 public static void main(String[] args) throws IOException {
		// Initialization:
        renderer = new OpenGLRenderer();    // Create create OpenGL context and renderer
        game = new Game("Test");
		tempInit();
        renderer.bindWorld(activeWorld);

        // Game loop
		while(!org.lwjgl.opengl.Display.isCloseRequested()) {
            game.update();
            InputParser.parseInput();
            // Update camera position
            camera.setTargetAndUpdate(playerGameObject.getPosition().toFloat());
            // Get selections
            selectedBlock = Picker.pickBlock(getActiveWorld(), camera);
            // Update GUI
            GUIUpdater.updateGUI(getBaseGUI());

            getActiveWorld().playerObject.hasChangedChunks = false;
            getActiveWorld().chunkChanges = false;
            renderer.update();
            renderer.render();
		}

        // Cleanup
        game.close();
        renderer.close();
        WorldManager.saveAll(getActiveWorld());
	}

    // Bullshit temporary initialization logic, before game mods are properly loaded
    public static void tempInit() throws IOException {
        world = new World("test", 5000, WorldSettings.worldHeight, 1); // Create gameworld
        game.getWorlds().put("test", world);
        activeWorld = world;
        world.playerObject = new GameObject(world.spawningPosition, "Soldier");
        playerGameObject = world.playerObject;
        world.playerObjects.add(world.playerObject);
        for(int i = 0; i < 9; i++) {
            world.worldObjects.add(i, new GameObject(new Vector3d(32, 1, 32), "Skeleton"));
        }
        for(int i = 9; i < 20; i++) {
            world.worldObjects.add(i, new GameObject(new Vector3d(31, 1, 32), "Soldier"));
        }
        world.worldObjects.add(world.playerObject);

        BufferedReader br = new BufferedReader(new FileReader("c:/db/file.txt"));
        //try {
        StringBuilder sb = new StringBuilder();
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append('\n');
            line = br.readLine();
        }
        everything = sb.toString();
        //}finally {
        br.close();
        //}

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        WorldManager.updateChunks(getActiveWorld());

        dgui = new GUI("dgui");
        gui = new GUI("gui");
        gui.getElement("char").setText(everything);

        rubyContainer = new RubyContainer();
    }

}