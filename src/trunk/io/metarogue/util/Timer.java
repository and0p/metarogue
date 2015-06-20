package io.metarogue.util;

import org.lwjgl.Sys;

public class Timer {

    long lastFrameTime;
    long delta;

    public static Timer instance = new Timer();

    public Timer() {
        lastFrameTime = getNanoTime();
        delta = 0;
    }

    public static void update() {
        long currentTime = getNanoTime();
        instance.delta = (int)(currentTime - instance.lastFrameTime);
        instance.lastFrameTime = currentTime;
        //Log.log("" + getFrameRate((int)convertNanosecondsToMilliseconds(getDelta())));
    }

    public static long getDelta() {
        return instance.delta;
    }

    public static long getFrameTime() {
        return instance.lastFrameTime;
    }

    public static long getNanoTime() {
        //return (Sys.getTime() * 1000) / Sys.getTimerResolution();
        return System.nanoTime();
    }

    public static long getMilliTime() {
        return Sys.getTime();
    }

    public static long convertMillisecondsToNanoseconds(long milliseconds) {
        return milliseconds*1000000;
    }

    public static long convertNanosecondsToMilliseconds(long nanoseconds) {
        return nanoseconds/1000000;
    }

    static int getFrameRate(int delta) {
        if(delta != 0) return 1000 / delta;
        return 0;
    }

}