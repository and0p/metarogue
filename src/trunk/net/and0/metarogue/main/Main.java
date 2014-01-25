package net.and0.metarogue.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

import net.and0.metarogue.controller.DBLoader;
import net.and0.metarogue.controller.GUI.GUIBuilder;
import net.and0.metarogue.controller.GUI.GUIUpdater;
import net.and0.metarogue.controller.InputParser;
import net.and0.metarogue.controller.Picker;
import net.and0.metarogue.controller.WorldManager;
import net.and0.metarogue.controller.ruby.RubyContainer;
import net.and0.metarogue.model.Game;
import net.and0.metarogue.util.MortonCurve;
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

    public static GUI getActiveGui() { return gui; }
    public static World getActiveWorld() { return world; }
    public static DBLoader getActiveDB() { return db; }
    public static RubyContainer getRubyContainer() { return rubyContainer; }
   
    public static OpenGLRenderer renderer;
    public static World world;
    public static GUI gui;
    public static Random randomGenerator;
    public static RubyContainer rubyContainer;
    public static String database;
    static DBLoader db;

    GUIElement selectedGUIElement = null;
    GameObject selectedGameObject = null;

    // Debug, sample dynamic GUI
    public static GUI dgui;

    public static String everything;


 public static void main(String[] args) throws IOException {

		// Initialization:
		world = new World("buh", 5000, WorldSettings.worldHeight, 1); // Create gameworld
        renderer = new OpenGLRenderer(getActiveWorld());	          // Create create OpenGL context and renderer
        world.playerObject = new GameObject(world.spawningPosition, "Soldier");
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
        db = new DBLoader("testgame");
        db.initWorld(getActiveWorld());

        WorldManager.updateChunks(getActiveWorld());
        renderer.readyDisplayLists(getActiveWorld());

        dgui = new GUI("dgui");

	    gui = new GUI("gui");
        gui.assignGameObject("health", Main.getActiveWorld().playerObject, "health");
        gui.assignGameObject("mana", Main.getActiveWorld().playerObject, "mana");
        gui.getElement("char").setText(everything);

        rubyContainer = new RubyContainer();

        // initGameLogic();
        System.out.print(MortonCurve.getMorton(0, 0) + ", " + MortonCurve.getWorldMorton(new Vector3d(0,0,0), 0) + "\n");
        System.out.print(getActiveWorld().getChunkArray(0, 0).getBytes().get(0) + "\n");

		while(!org.lwjgl.opengl.Display.isCloseRequested()) {

            WorldManager.updateChunks(getActiveWorld());
            getActiveWorld().selectedBlock = Picker.pickBlock(getActiveWorld());
            InputParser.parseInput();
            GUIUpdater.updateGUI(getActiveGui());

            getActiveWorld().playerObject.hasChangedChunks = false;
            getActiveWorld().chunkChanges = false;

            Vector3d newTarget = getActiveWorld().playerObject.getPosition();
            getActiveWorld().getActiveCamera().target.set(newTarget.getX(), newTarget.getY(), newTarget.getZ());
            renderer.update();
            renderer.render();
		}

        renderer.close();
        WorldManager.saveAll(getActiveWorld());

	}

}