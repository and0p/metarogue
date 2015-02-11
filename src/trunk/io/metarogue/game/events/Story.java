package io.metarogue.game.events;

import java.util.HashMap;

public class Story extends StoryComposite {

    // Turns passed before "live" turn
    int totalTurns;
    // Number of turns passed before creation. For example if client connects at the display of turn 80 then this is 79
    int turnsFromBefore;

    // Info as to play status, live turn, and display turn
    boolean live;
    boolean playing;
    boolean tracking; // Larger tracking operations should be multithreaded, this boolean is for locking
    TimestampInt liveStampInt;
    TimestampInt displayStampInt;

    float currentActionProgress = 0;

    HashMap<Integer, Turn> turns;

    public Story(int turnsFromBefore) {
        turns = new HashMap<Integer, Turn>();
        this.turnsFromBefore = turnsFromBefore;
    }

    public void update() {
        //turns.get()
    }

    // Change position in time
    public void track(TimestampInt destinationTimestampInt) {
        // Don't attempt if we're already tracking in another thread.
        if(!tracking) {
            // Don't bother if the timestamp is the same as ours
            if(!displayStampInt.compare(destinationTimestampInt)) {
                // TODO: Validate that timestamp we're tracking to
                Timestamp currentStamp = new Timestamp(this, displayStampInt);
                Timestamp destinationStamp = new Timestamp(this, destinationTimestampInt);
                TimestampInt timeDelta = displayStampInt.getDelta(destinationTimestampInt);
                int changeAmount = timeDelta.getAmount();
                // Run or reverse all turns, subturns, events, and actions to that point
                // TODO: do this on another thread for a frame if the difference is large enough?
                // Set progress, as it doesn't need to change game state only animations
                currentActionProgress = destinationTimestampInt.getProgress();
                if(timeDelta.isPositive()) {
                    if(changeAmount > 0) {
                        if(changeAmount > 1) {
                            currentStamp.getEvent().runFrom(displayStampInt.getAction());
                            if(changeAmount > 2) {
                                currentStamp.getSubTurn().runFrom(displayStampInt.getEvent());
                                if(changeAmount > 3) {
                                    
                                }
                            }
                        }
                    }
                    // Check if this change is by a full turn or more.
                    if(timeDelta.getTurn() > 0) {
                        // Fast forward through actions/events/subturns to get to beginning of next turn
                        currentStamp.getEvent().runFrom(displayStampInt.getAction());
                        currentStamp.getSubTurn().runFrom(displayStampInt.getEvent());
                        currentStamp.getTurn().runFrom(displayStampInt.getSubturn());
                        // Run full turns until we reach destination
                        for(int i = displayStampInt.getTurn()+1; i < destinationTimestampInt.getTurn(); i++) {
                            getTurn(i).run();
                        }
                        // Fast forward through subturns/events/actions to get to the exact point we want
                        destinationStamp.getTurn().runTo(destinationTimestampInt.getSubturn());
                        destinationStamp.getSubTurn().runTo(destinationTimestampInt.getEvent());
                        destinationStamp.getEvent().runTo(destinationTimestampInt.getAction());
                    } else {
                        // Fast forward through
                        if(timeDelta.getAction() > currentStamp.getEvent().getSize())
                    }
                }
            }
        }
    }

    public Turn getTurn(int i) {
        if(turns.containsKey(i)) {
            return turns.get(i);
        }
        return null;
    }

    public int getSize() {
        return turns.size();
    }

    public StoryComponent getStoryComponent(int i) {
        return turns.get(i);
    }

}