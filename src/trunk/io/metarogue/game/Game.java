package io.metarogue.game;

import io.metarogue.client.view.threed.Vector3d;
import io.metarogue.game.events.animation.Animation;
import io.metarogue.game.events.Story;
import io.metarogue.game.events.time.Timestamp;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.game.gameworld.World;
import io.metarogue.client.view.TextureList;
import io.metarogue.util.Timer;
import io.metarogue.util.WorldManager;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

// Let's see how this goes...

public class Game {

    // Name of game being played
    String name;

    // Path to game assets
    String path;
    String dbpath;

    // Start time of game in milliseconds
    long startTime;

    // Enums for game options
    static enum TurnType { SUBTURN, EVENT }
    // Maximum amount of time to wait for a turn. If wait percentage is 0 this is a static x millisecond turn time
    int maxTurnWait;
    // Amount of time to wait after a certain percent of users are ready
    int turnThresholdWait;
    // Percent of people who need to be ready before forcing threshold wait (1 = 100%, .5 = 50%)
    float turnThreshold;

    // Turn container
    Story story;
    // Default animation, so save space on animation calls
    Animation defaultAnimation;

    // List of worlds
    HashMap<Integer, World> worlds;
    // K/V pair for world names and ID nums, for simplicity I think?
    HashMap<String, Integer> worldIDs;
    // Number of worlds created so far
    int worldsCreated = 0;
    // Default world to spawn in
    World defaultWorld;
    //TODO: get rid of this guy vvv
    public static GameObject defaultPlayer;

    // List of in-game objects
    ArrayList<GameObject> gameObjects;
    // List of classes (in-game classes like "warrior" and "flying butt")
    HashMap<String, Object> classes;
    // List of sides
    ArrayList<Side> sides;
    // List of parties or groups of objects
    HashMap<Integer, Party> parties;

    // List of textures
    TextureList textureList;
    Texture guiTexture;
    Texture worldTexture;

    public Game(String name) {
        this.name = name;
    }

    public void init() {
        //TODO: Make some kind of method to check full directory of game to make sure it's legit, otherwise return false?
        // Get path of game.
        path = "C:/metarogue/" + name.toLowerCase();
        // Initialize stuff
        worlds = new HashMap<Integer, World>();
        worldIDs = new HashMap<String, Integer>();
        classes = new HashMap<String, Object>();
        sides = new ArrayList<Side>();
        parties = new HashMap<Integer, Party>();
        textureList = new TextureList(new File(path + "/textures"));
        getSides().add(new Side(0, "Players"));
        getSides().add(new Side(1, "Enemies"));
        gameObjects = new ArrayList<GameObject>();
        story = new Story(sides.size());
        // Load classes based on file names
        // Load world and GUI textures
        // Set start time for future reference
        startTime = Timer.getMilliTime();
        defaultAnimation = new Animation();
    }

    public void update() {
        //TODO: Worlds need to store how long it's been since a player was active and unbind when appropriate
        story.update();
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
        World w = new World(worldsCreated, 12, 4, 1);
        worlds.put(worldsCreated, w);
        if(defaultWorld == null) defaultWorld = w;
        worldsCreated++;
        return worldsCreated-1;
    }

    public void loadWorld(String key) {

    }

    public World getWorld(int key) {
        if(worlds.containsKey(key)) {
            return worlds.get(key);
        }
        return null;
    }

    public void addGameObject(GameObject go, int side) {
        gameObjects.add(go);
        sides.get(side).gameObjects.add(go);
        worlds.get(0).addObject(go);
    }

    // Getters and Setters, etc...

    public Story getStory() {
        return story;
    }

    public void setDefaultWorld(int id) {
        defaultWorld = worlds.get(id);
    }

    public void setDefaultWorld(World world) {
        defaultWorld = world;
    }

    public HashMap<Integer, World> getWorlds() {
        return worlds;
    }

    public HashMap<String, Object> getClasses() {
        return classes;
    }

    public HashMap<Integer, Party> getParties() {
        return parties;
    }

    public ArrayList<Side> getSides() { return sides; }

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

    public Animation getDefaultAnimation() { return defaultAnimation; }

    public void setDefaultAnimation(Animation defaultAnimation) { this.defaultAnimation = defaultAnimation; }

    public ArrayList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public Side getSide(int i) {
        if(i < sides.size() && i >= 0) {
            return sides.get(i);
        }
        return null;
    }

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