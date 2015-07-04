package io.metarogue.game;

import io.metarogue.game.events.animation.Animation;
import io.metarogue.game.events.Story;
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
    // List of in-game objects
    HashMap<Integer, GameObject> gameObjects;
    // Keep track of created game objects, increment for IDs
    int createdObjects;

    // Number of worlds created so far
    int worldsCreated = 0;
    // Default world to spawn in
    World defaultWorld;
    //TODO: get rid of this guy vvv
    public static GameObject defaultPlayer;

    // List of sides
    ArrayList<Side> sides;

    // Files for textures
    File guiTexture;
    File worldTexture;

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
        sides = new ArrayList<Side>();
        getSides().add(new Side(0, "Players"));
        getSides().add(new Side(1, "Enemies"));
        gameObjects = new HashMap<Integer, GameObject>();
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

    public int addGameObject(GameObject go, int side) {
        go.setID(createdObjects);
        gameObjects.put(go.getID(), go);
        createdObjects++;
        sides.get(side).gameObjects.add(go);
        worlds.get(0).addObject(go);
        return createdObjects-1;
    }

    // Getters and Setters, etc...

    public GameObject getGameObject(int id) {
        return gameObjects.get(id);
    }

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
    public ArrayList<Side> getSides() { return sides; }

    public String getPath() { return path; }

    public String getName() { return name; }

    public World getDefaultWorld() { return defaultWorld; }

    public Animation getDefaultAnimation() { return defaultAnimation; }

    public void setDefaultAnimation(Animation defaultAnimation) { this.defaultAnimation = defaultAnimation; }

    public HashMap<Integer, GameObject> getGameObjects() {
        return gameObjects;
    }

    public void setGUITextureFile(File f) {
        guiTexture = f;
    }

    public void setWorldTexture(File f) {
        worldTexture = f;
    }

    public Side getSide(int i) {
        if(i < sides.size() && i >= 0) {
            return sides.get(i);
        }
        return null;
    }

}