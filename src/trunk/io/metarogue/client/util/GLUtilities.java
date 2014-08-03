package io.metarogue.client.util;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * MetaRogue class
 * User: andrew
 * Date: 5/19/13
 * Time: 12:53 AM
 */
public class GLUtilities {

    public GLUtilities() {
        // Auto-generated constructor
    }

    public static Vector3f getCoorFromMouse(int mouseX, int mouseY) {
        IntBuffer viewport = BufferUtils.createIntBuffer(16);
        FloatBuffer modelview = BufferUtils.createFloatBuffer(16);
        FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        FloatBuffer winZ = BufferUtils.createFloatBuffer(1);
        FloatBuffer position = BufferUtils.createFloatBuffer(3);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
        GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
        GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);

        GL11.glReadPixels(mouseX, mouseY, 1, 1, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, winZ);
        GLU.gluUnProject((float)mouseX, (float)mouseY, 1f, modelview, projection, viewport, position);

        return new Vector3f(position.get(0), position.get(1), position.get(2));
    }

}