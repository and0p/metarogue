package io.metarogue.game.events;

import io.metarogue.util.Log;
import io.metarogue.util.Timer;

import java.util.ArrayList;

public class Turn extends StoryComposite {

    ArrayList<SubTurn> subTurns;

    public void run() {
        if(Log.logging) {
            Log.log("Running turn " + id + " at " + Timer.getMilliTime());
        }
        super.run();
    }

    public void reverse() {
        if(Log.logging) {
            Log.log("Reversing turn " + id + " at " + Timer.getMilliTime());
        }
        super.reverse();
    }

    public StoryComponent getStoryComponent(int i) {
        if(i >= 0 && i < getSize()) {
            return subTurns.get(i);
        }
        Log.log("ERROR: StoryObject index out of bounds bro~");
        return null;
    }

    public SubTurn getSubTurn(int i) {
        return subTurns.get(i);
    }

    public void addSubTurn(SubTurn s) {
        subTurns.add(s);
    }

    public int getSize() {
        return subTurns.size();
    }

}