package io.metarogue.util;

public class Log {

    public static boolean logging = true;

    public static boolean logNetwork = true;
    public static boolean logConnections = true;
    public static boolean logWorldLoading = true;

    public static boolean logTurns = true;
    public static boolean logEvents = true;

    public Log() {
        // Auto-generated constructor
    }

    public static void log(String s) {
        System.out.println(s);
    }

}