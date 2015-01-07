package io.metarogue.game.events;

import io.metarogue.game.events.actions.Action;

import java.util.ArrayList;

public class Event {

    ArrayList<Action> actions;
    int currentAction;
    int queueSize;

    public Event() {
        actions = new ArrayList<Action>();
    }

    public Event(Action a) {
        actions = new ArrayList<Action>();
        actions.add(a);
    }

    public Event(ArrayList<Action> actions) {
        this.actions = actions;
    }

    public void runAll() {
        currentAction = 0;
        queueSize = getSize();
        Action a = getNextAction();
        while(a != null){
            a.run();
            a = getNextAction();
        }
    }

    public Action getNextAction() {
        if(currentAction < queueSize) {
            currentAction++;
            return actions.get(currentAction-1);
        }
        return null;
    }

    public int getSize() {
        return actions.size();
    }

}