package io.metarogue.util.math;

public class Rectangle {

    int x, y, width, height;

    Vector2d topLeft;
    Vector2d bottomRight;

    public Rectangle(Vector2d topLeft, Vector2d bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
        width = bottomRight.getX() - topLeft.getX();
        height = bottomRight.getY() - topLeft.getY();
        x = topLeft.getX() + (int)Math.ceil(width/2);
        y = topLeft.getY() + (int)Math.ceil(height/2);
    }

    public Rectangle(int x, int y, int width, int height) {
        topLeft = new Vector2d(x, y);
        bottomRight = new Vector2d(x+width, y+height);
    }

    public boolean contains(int x, int y) {
        if(x>topLeft.getX() && x <bottomRight.getX() && y<topLeft.getY() && y>bottomRight.getY()) {
            return true;
        } else {
            return false;
        }
    }

    public Vector2d getCenter() {
        return new Vector2d(x, y);
    }

    public int getLeft() {
        return topLeft.getX();
    }
    public int getTop() {
        return topLeft.getY();
    }
    public int getRight() {
        return bottomRight.getX();
    }
    public int getBottom() {
        return bottomRight.getY();
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public Vector2d getTopLeft() {
        return topLeft;
    }
    public Vector2d getBottomRight() {
        return bottomRight;
    }

}
