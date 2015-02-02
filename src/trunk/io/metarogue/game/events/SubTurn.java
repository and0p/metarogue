package io.metarogue.game.events;

import io.metarogue.util.Log;
import io.metarogue.util.Timer;

import java.util.ArrayList;

public class SubTurn {

    int animationTime;
    int id;

    ArrayList<Event> events;

    public SubTurn() {
        events = new ArrayList<Event>();
    }

    public SubTurn(ArrayList<Event> events) {
        this.events = new ArrayList<Event>();
    }

    public int getAnimationTime() {
        //TODO: Add up animations
        return 0;
    }

    public void validate() {
        //TODO: If unit was destroyed in previous subturn, remove action
    }

    public void run() {
        if(Log.logging & Log.logTurns) {
            Log.log("  Running subturn " + id + " at " + Timer.getMilliTime());
        }
        for(Event e : events) {
            e.runAll();
        }
    }

}