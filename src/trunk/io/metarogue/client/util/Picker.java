package io.metarogue.client.util;

import io.metarogue.game.Camera;
import io.metarogue.game.gameworld.World;
import io.metarogue.client.view.threed.Ray;
import io.metarogue.client.view.threed.Vector3d;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

/**
 * MetaRogue class
 * User: andrew
 * Date: 5/19/13
 * time: 1:21 AM
 */
public class Picker {

    public Picker() {
        // Auto-generated constructor
    }

    public static Vector3d pickBlock(World world, Camera camera) {

        // Get camera position
        Vector3f cameraVector = camera.position;
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

}