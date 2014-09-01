package io.metarogue.game;

import io.metarogue.game.gameworld.World;
import io.metarogue.client.view.TextureList;
import io.metarogue.util.WorldManager;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

// Let's see how this goes...

public class Game {

    // Name of game being played
    String name;

    // Path to game assets
    String path;
    String dbpath;

    // List of worlds
    HashMap<Integer, World> worlds;
    // K/V pair for world names and ID nums, for simplicity I think?
    HashMap<String, Integer> worldIDs;
    // Number of worlds created so far
    int worldsCreated = 0;
    // Default world
    World defaultWorld;

    // List of classes (in-game classes like "warrior" and "flying butt")
    HashMap<String, Object> classes;

    // List of parties or groups of objects
    HashMap<String, Party> parties;

    // List of textures
    TextureList textureList;
    Texture guiTexture;
    Texture worldTexture;

    public Game(String name) {
        this.name = name;
        // Get path of game. TODO: Error check that beast, might not exist
        path = "C:/metarogue/" + name.toLowerCase();
        // Initialize stuff
        worlds = new HashMap<Integer, World>();
        worldIDs = new HashMap<String, Integer>();
        classes = new HashMap<String, Object>();
        parties = new HashMap<String, Party>();
        // Load classes based on file names
        textureList = new TextureList(new File(path + "/textures"));
        // Load world and GUI textures
    }

    public void update() {
        //TODO: Worlds need to store how long it's been since a player was active and unbind when appropriate
        for(World w : worlds.values()) {
            WorldManager.updateChunks(w);
        }
    }

    // Cleanup!
    public void close() {
        // Clear game-related textures from memory
        textureList.close();
        guiTexture.release();
        worldTexture.release();
        // Save all open worlds
        for(World w : worlds.values()) {
            //WorldManager.saveAll(w);
        }
    }

    public int newWorld() {
        int id = UUID.randomUUID().hashCode();
        World w = new World(id, 12, 4, 1);
        worlds.put(id, w);
//        if(name != null && !worldIDs.containsKey(name)) {
//            w.setName(name);
//            worldIDs.put(name, id);
//        }
        if(defaultWorld == null) defaultWorld = w;
        worldsCreated++;
        return id;
    }

    public void loadWorld(String key) {

    }

    public World getWorld(int key) {
        if(worlds.containsKey(key)) {
            return worlds.get(key);
        }
        return null;
    }

    public void setDefaultWorld(int id) {
        defaultWorld = worlds.get(id);
    }

    // Getters and Setters, etc...

    public HashMap<Integer, World> getWorlds() {
        return worlds;
    }

    public HashMap<String, Object> getClasses() {
        return classes;
    }

    public HashMap<String, Party> getParties() {
        return parties;
    }

    public TextureList getTextureList() {
        return textureList;
    }

    public Texture getWorldTexture() {
        return worldTexture;
    }

    public Texture getGuiTexture() {
        return guiTexture;
    }

    public String getPath() { return path; }

    public String getName() { return name; }

    public World getDefaultWorld() { return defaultWorld; }

    public void loadLocalTextures() {
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