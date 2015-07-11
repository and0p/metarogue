package io.metarogue.util;

public class Timer {

    long lastFrameTime;
    long milliTime;
    long delta;

    public static Timer instance = new Timer();

    public Timer() {
        lastFrameTime = getNanoTime();
        milliTime = System.currentTimeMillis();
        delta = 0;
    }

    public static void update() {
        long currentTime = getNanoTime();
        instance.delta = (int)(currentTime - instance.lastFrameTime);
        instance.lastFrameTime = currentTime;
        instance.milliTime = System.currentTimeMillis();
        //Log.log("" + getFrameRate((int)convertNanosecondsToMilliseconds(getDelta())));
    }

    public static long getDelta() {
        return instance.delta;
    }

    public static long getDeltaToNow(long t) {
        return instance.milliTime - t;
    }

    public static long getFrameTime() {
        return instance.lastFrameTime;
    }

    public static long getNanoTime() {
        //return (Sys.getTime() * 1000) / Sys.getTimerResolution();
        return System.nanoTime();
    }

    public static long getMilliTime() {
        return instance.milliTime;
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