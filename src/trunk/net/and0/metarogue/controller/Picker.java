package net.and0.metarogue.controller;

import net.and0.metarogue.model.gameworld.World;
import net.and0.metarogue.util.GLUtilities;
import net.and0.metarogue.util.threed.Ray;
import net.and0.metarogue.util.threed.Vector3d;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * MetaRogue class
 * User: andrew
 * Date: 5/19/13
 * Time: 1:21 AM
 */
public class Picker {

    public Picker() {
        // Auto-generated constructor
    }

    public static Vector3d pickBlock(World world) {

        // Get camera position
        Vector3f cameraVector = world.getActiveCamera().position;
        // Unproject a target position from mouse coordinates into object space
        Vector3f target = GLUtilities.getCoorFromMouse(Mouse.getX(), Mouse.getY());

        // Some variables
        Vector3f currentCheckf;
        Vector3d currentCheckd = new Vector3d();
        Vector3f precisionCheckf;
        Vector3d precisionCheckd = new Vector3d();

        // Create the ray
        Ray r = new Ray(cameraVector, target);

        // Check to a certain depth
        for(float i = 0; i <= 100; i++) {
            // Get ray position
            currentCheckf = Ray.positionAlongRay(r, i);
            // Convert to int, add .5f to compensate for how surfaces are expanding from their int when building meshes
            currentCheckd.set((int)(currentCheckf.getX()+.5f), (int)(currentCheckf.getY()+.5f), (int)(currentCheckf.getZ()+.5f));
            // Check a block every 1 full step.
            if(world.getBlock(currentCheckd) > 0 && world.getBlock(currentCheckd) != 15) {
                // If we get a solid hit, step back 1 and check every .2 for another, to check against corner cases.
                for(float a = i-1; a < i; a += .2f) {
                        precisionCheckf = Ray.positionAlongRay(r, a);
                        precisionCheckd.set((int)(precisionCheckf.getX()+.5f), (int)(precisionCheckf.getY()+.5f), (int)(precisionCheckf.getZ()+.5f));
                        // See if the result is different from the less accurate anyway, before asking world for a block
                        if(precisionCheckd != currentCheckd && world.getBlock(precisionCheckd) > 0 && world.getBlock(precisionCheckd) != 15) {
                            return precisionCheckd;
                    }
                }
                // Otherwise return correct result
                return currentCheckd;
            }
        }

        return null;
    }

    public static Vector3d pickBlock2(World world) {

        Vector3f cameraVector = world.getActiveCamera().position;
        Vector3f target = GLUtilities.getCoorFromMouse(Mouse.getX(), Mouse.getY());
        Vector3f currentCheckf;
        Vector3d currentCheckd = new Vector3d();

        Ray r = new Ray(cameraVector, target);

        Vector3f divs = new Vector3f(r.normal.getX() / 1, r.normal.getY() / 1, r.normal.getZ() / 1);
        Vector3f subs = new Vector3f(r.origin.getX() - (int)r.origin.getX(), r.origin.getY() - (int)r.origin.getY(), r.origin.getZ() - (int)r.origin.getZ());
        System.out.print(subs.getX() + ", " + subs.getY() + ", " + subs.getZ() + ".\n");

        for(float i = 0; i <= 100; i++) {
            currentCheckf = Ray.positionAlongRay(r, (float)i*.05f);
            currentCheckd.set((int)(currentCheckf.getX()+.5f), (int)(currentCheckf.getY()+.5f), (int)(currentCheckf.getZ()+.5f));
            if(world.getBlock(currentCheckd.getX(), currentCheckd.getY(), currentCheckd.getZ()) > 0 &&
                    world.getBlock(currentCheckd.getX(), currentCheckd.getY(), currentCheckd.getZ()) != 15) return currentCheckd;
        }

        return null;
    }

}