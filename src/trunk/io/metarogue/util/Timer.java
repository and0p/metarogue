package io.metarogue.util;

import org.lwjgl.Sys;

public class Timer {

    public static long getNanoTime() {
        //return (Sys.getTime() * 1000) / Sys.getTimerResolution();
        return System.nanoTime();
    }

    public static long getMilliTime() {
        return Sys.getTime();
    }

    // Return delta for nanoseconds
    public static long getDeltaNano(long time) {
        return getNanoTime() - time;
    }

    // Get delta for milliseconds
    public static long getDeltaMilli(long time) {
        return getMilliTime() - time;
    }

    public static long convertMillisecondsToNanoseconds(long milliseconds) {
        return milliseconds*1000000;
    }

    public static long convertNanosecondsToMilliseconds(long nanoseconds) {
        return nanoseconds/1000000;
    }

}