package io.metarogue.game.timeline;

// Encapsulated LinkedHashmap<Integer, Turn> that makes sure we delete old history

import io.metarogue.game.timeline.actions.BlankAction;

import java.util.LinkedHashMap;

public class TurnCollection {

    int liveTurn;
    LinkedHashMap<Integer, Turn> turns;
    int sizeLimit;

    public TurnCollection(int sizeLimit, int startingTurn) {
        this.sizeLimit = sizeLimit;
        liveTurn = startingTurn;
        turns = new LinkedHashMap<Integer, Turn>();
    }

    // If this collection is new, a single empty event and action
    public void init() {
        newTurn();
        getTurn(0).newSubTurn();
        turns.get(0).subTurns.get(0).addEvent(new Event(new BlankAction()));
    }

    public Turn newTurn() {
        return newTurn(liveTurn);
    }

    public Turn newTurn(int i) {
        Turn t = new Turn();
        turns.put(i, t);
        // Remove oldest turn if we're already at max history storage
        if(turns.size() > sizeLimit) {
            turns.remove(i-turns.size());
        }
        liveTurn++;
        return t;
    }

    public Turn getTurn(int i) {
        if(containsTurn(i)) {
            return turns.get(i);
        }
        return null;
    }

    // TODO: Doublecheck this logic:
    public int getFirst() {
        return liveTurn - turns.size();
    }

    public int getSize() {
        return turns.size();
    }

    public boolean containsTurn(int i){
        return turns.containsKey(i);
    }

}