package net.and0.metarogue.model;

import net.and0.metarogue.model.gameworld.World;
import net.and0.metarogue.view.TextureList;

import java.util.HashMap;

/**
 * MetaRogue class
 * User: andrew
 * Date: 1/21/14
 * Time: 8:51 PM
 */

// Let's see how this goes...

public class Game {

    // Name of game being played
    String name;

    // List of worlds
    HashMap<String, World> worlds;

    // List of classes (in-game classes like "warrior" and "flying butt"
    HashMap<String, Object> classes;

    // List of parties or groups of objects
    HashMap<String, Party> parties;

    TextureList textureList;

    public Game(String name) {
        this.name = name;
        worlds = new HashMap<String, World>();
        classes = new HashMap<String, Object>();
        parties = new HashMap<String, Party>();
        // Load classes based on file names
        // Load textures based on file names
    }

}
