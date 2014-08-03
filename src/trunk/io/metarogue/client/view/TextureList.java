package io.metarogue.client.view;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

public class TextureList {

    HashMap<String, Texture> list; // The list itself
    Texture unknown;               // Texture to return if no texture is found

    public TextureList() {
        list = new HashMap<String, Texture>();
    }

    // Construct with directory of PNG files to load
    public TextureList(File dir) {
        // Initialize list
        list = new HashMap<String, Texture>();
    }

    public void loadDirectory(File dir) {
        // Get list of files from directory
        File[] listOfFiles = dir.listFiles();
        // String array for eventual split of file name, checking for png files
        String[] filename;
        String[] pathname;
        // Loop through files, loading PNG files as textures
        for(int i = 0; i < listOfFiles.length; i++) {
            // Check if file and not directory
            if(listOfFiles[i].isFile()) {
                // Get name as string array "filename" and "png" ideally
                pathname = listOfFiles[i].getName().split("\\\\");
                filename = pathname[pathname.length-1].split("\\.");
                // Check if file has extension at all
                if(filename.length > 1) {
                    // Check if extension is png
                    if(filename[1].equalsIgnoreCase("png")) {
                        try {
                            // Load the PNG
                            Texture t = TextureLoader.getTexture("PNG", new FileInputStream(listOfFiles[i]));
                            // Add it to the list, using the first part of the filename as the key
                            list.put(filename[0].toLowerCase(), t);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        //TODO: This should be something more permanent, a PNG built into the jar or something
        unknown = list.get("unknown");
    }

    public Texture getTexture(String s) {
        String sl = s.toLowerCase();
        if(list.containsKey(sl)) {
            return list.get(sl);
        }
        return unknown;
    }

    // Release all texture data
    public void close() {
        for(Texture t : list.values()) {
            t.release();
        }
    }

}