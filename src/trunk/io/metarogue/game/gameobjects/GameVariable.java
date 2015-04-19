package io.metarogue.game.gameobjects;

/**
 * MetaRogue class
 * User: andrew
 * Date: 12/27/13
 * time: 2:19 PM
 */

public class GameVariable {

    // Boundaries
    int min;
    int max;
    // Current value
    int cur;

    public GameVariable(int min, int max, int cur) {
        this.min = min;
        this.max = max;
        this.cur = cur;
    }

    public GameVariable(int cur) {
        this.min = Integer.MIN_VALUE;
        this.max = Integer.MAX_VALUE;
        this.cur = cur;
    }

    public int getMin() { return min; }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int get() {
        return cur;
    }

    public void set(int cur) {
        this.cur = cur;
        //if(cur < min) cur = min;
        //if(cur > max) cur = max;
    }

    public void change(int amount) {
        cur += amount;
    }

    // Return how "full" this gameVariable is, between min and max values
    public float getPercentage() {
        return (float)cur/max;
        //return (float)((cur - min) / (max - min));
    }

}