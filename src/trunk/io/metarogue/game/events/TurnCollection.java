package io.metarogue.game.events;

// Encapsulated Hashmap<Integer, Turn> that makes sure we delete old history

import java.util.LinkedHashMap;

public class TurnCollection {

    int liveTurn;
    LinkedHashMap<Integer, Turn> turns;
    int sizeLimit;

    public TurnCollection(int sizeLimit, int startingTurn) {
        this.sizeLimit = sizeLimit;
        liveTurn = startingTurn;
        turns = new LinkedHashMap<Integer, Turn>();
        if(startingTurn != 0) {
            // Load last turn to get initial gamestate? This is assuming there is a history to load.
            newTurn(startingTurn + 1);
        } else {
            newTurn(startingTurn);
        }
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

    public int getSize() {
        return turns.size();
    }

    public boolean containsTurn(int i){
        return turns.containsKey(i);
    }

}