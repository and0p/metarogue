package io.metarogue.client.view.threed;

import io.metarogue.util.math.Vector2d;

/**
 * MetaRogue class
 * User: andrew
 * Date: 5/30/13
 * time: 3:59 PM
 */
public class Box {

    Vector2d[] corners = new Vector2d[] { new Vector2d(0,0), new Vector2d(0,0), new Vector2d(0,0), new Vector2d(0,0) } ;

    public Box() {
    }

    public Box(Vector2d position, int width, int height) {
        corners[0].set(position.getX(), position.getY());
        corners[1].set(position.getX() + width, position.getY());
        corners[2].set(position.getX() + width, position.getY() + height);
        corners[3].set(position.getX(), position.getY() + height);
    }

    public Box(Vector2d position1, Vector2d position2) {
        corners[0].set(position1.getX(), position1.getY());
        corners[1].set(position2.getX() , position1.getY());
        corners[2].set(position2.getX() , position2.getY());
        corners[3].set(position1.getX(), position2.getY());
    }

    public void setCorners(Vector2d position, int width, int height) {
        corners[0].set(position.getX(), position.getY());
        corners[1].set(position.getX() + width, position.getY());
        corners[2].set(position.getX() + width, position.getY() + height);
        corners[3].set(position.getX(), position.getY() + height);
    }

    public void setCorners(int x, int y, int width, int height) {
        corners[0].set(x, y);
        corners[1].set(x + width, y);
        corners[2].set(x + width, y + height);
        corners[3].set(corners[0].getX(), corners[3].getY());
    }

    public void setCorner(int i, Vector2d position) {
        corners[i].set(position.getX(), position.getY());
    }

    public void setCorner(int i, int x, int y) {
        corners[i].set(x, y);
    }

    public Vector2d getCorner(int i) {
        return corners[i];
    }

    public int getRight() {
        return corners[1].getX();
    }

    public int getLeft() {
        return corners[0].getX();
    }

    public int getTop() {
        return corners[0].getY();
    }

    public int getBottom() {
        return corners[2].getY();
    }

    public int getWidth() {
        return corners[1].getX() - corners[0].getX();
    }

    public int getHeight() {
        return corners[0].getY() - corners[2].getY();
    }
}