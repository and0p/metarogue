package io.metarogue.util;


public class Timer {

    public Timer() {
        // Auto-generated constructor
    }

    // Get system time, for sync'ing stuff
    public static long getTime() {
        //return (Sys.getTime() * 1000) / Sys.getTimerResolution();
        return System.nanoTime();
    }

}
