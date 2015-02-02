package io.metarogue.game.events;

import java.util.ArrayList;

public class TurnCollection {

    // Turns passed before "live" turn
    int totalTurns;
    // Number of turns passed before creation.
    int turnsFromBefore;

    // Current "live" turn in real time
    Turn liveTurn;

    ArrayList<Turn> turns;

    public TurnCollection(int turnsFromBefore) {
        turns = new ArrayList<Turn>();
        this.turnsFromBefore = turnsFromBefore;
    }

    public void update() {
        //turns.get()
    }

}