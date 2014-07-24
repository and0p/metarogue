package net.and0.metarogue.model;

import net.and0.metarogue.controller.DBLoader;
import net.and0.metarogue.controller.WorldManager;
import net.and0.metarogue.model.gameworld.World;
import net.and0.metarogue.view.GUI.GUI;
import net.and0.metarogue.view.TextureList;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

// Let's see how this goes...

public class Game {

    // Name of game being played
    String name;

    // Path to game assets
    String path;
    String dbpath;

    // List of worlds
    HashMap<String, World> worlds;

    // List of classes (in-game classes like "warrior" and "flying butt"
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
        worlds = new HashMap<String, World>();
        classes = new HashMap<String, Object>();
        parties = new HashMap<String, Party>();
        // Load classes based on file names
        textureList = new TextureList(new File(path + "/textures"));
        // Load world and GUI textures
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
        // Create database connection
        dbLoader = new DBLoader(name);
    }

    public void update() {
        // TODO: Worlds need to store how long it's been since a player was active and unbind when appropriate
        for(World w : worlds.values()) {
            WorldManager.updateChunks(w);
        }
    }

    // Cleanup!
    public void close() {
        // Close the database connection
        dbLoader.close();
        // Clear game-related textures from memory
        textureList.close();
        guiTexture.release();
        worldTexture.release();
        // Save all open worlds
        for(World w : worlds.values()) {
            //WorldManager.saveAll(w);
        }
    }

    // Getters and Setters, etc...

    public HashMap<String, World> getWorlds() {
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
}