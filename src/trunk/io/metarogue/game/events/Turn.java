package io.metarogue.game.events;

import io.metarogue.Main;
import io.metarogue.game.events.actions.Action;
import io.metarogue.game.Side;
import io.metarogue.util.Log;
import io.metarogue.util.Timer;

import java.util.ArrayList;

public class Turn extends StoryComposite {

    ArrayList<SubTurn> subTurns;

    public Turn() {
        subTurns = new ArrayList<SubTurn>();
        for(Side s : Main.getGame().getSides()) {
            subTurns.add(new SubTurn());
        }
    }

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

    public Action getFirstAction() {
        if(getSize() > 0) {
            return getSubTurn(0).getFirstAction();
        }
        return null;
    }

    public SubTurn getSubTurn(int i) {
        if (!subTurns.isEmpty() && i < subTurns.size() && i>= 0) {
            return subTurns.get(i);
        }
        Log.log("ERROR: Subturn index out of bounds bro~");
        return null;
    }

    public void addSubTurn(SubTurn s) {
        subTurns.add(s);
    }

    public int getSize() {
        return subTurns.size();
    }

}