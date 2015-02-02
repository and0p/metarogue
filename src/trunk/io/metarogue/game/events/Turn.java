package io.metarogue.game.events;

import io.metarogue.util.Log;
import io.metarogue.util.Timer;

import java.util.ArrayList;

public class Turn {

    // Static counter for all turns
    static int turnsTaken = 0;
    // ID of turn
    int id;
    // Predicted time for turn, int of combined milliseconds for all animations that make up subturns
    int animationTime;

    // List of subturns
    ArrayList<SubTurn> subTurns;

    public Turn() {
        id = turnsTaken;
        turnsTaken++;
        animationTime = 0;
        subTurns = new ArrayList<SubTurn>();
    }

    public Turn(ArrayList<SubTurn> subTurns) {
        this.subTurns = subTurns;
    }

    public int getAnimationTime() {
        return 0;
    }

    // Run all events, return animation time.
    public void run() {
        if(Log.logging) {
            Log.log("Running turn " + id + " at " + Timer.getMilliTime());
        }
        for(SubTurn s : subTurns) {
            s.run();
        }
    }

}