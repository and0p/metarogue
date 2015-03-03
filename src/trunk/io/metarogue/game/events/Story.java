package io.metarogue.game.events;

import io.metarogue.game.events.actions.Action;
import io.metarogue.game.gameobjects.GameObject;

public class Story extends StoryComposite {

    // Turns passed before "live" turn
    int totalTurns;
    // Number of turns passed before creation. For example if client connects at the display of turn 80 then this is 79
    int startingTurn;

    // Info as to play status, live turn, and display turn
    boolean playing = false;
    boolean tracking = false; // Larger tracking operations should be multithreaded, this boolean is for locking

    TimestampInt displayStampInt;
    Timestamp displayStamp;
    Turn displayTurn;
    TimestampInt liveStampInt;
    Turn upcomingTurn; // Turn that players are submitting events for

    float currentActionProgress = 0;

    TurnCollection turns;

    public Story(int turnsFromBefore) {
        turns= new TurnCollection(1000, turnsFromBefore); //TODO: Make history size configurable
        this.startingTurn = turnsFromBefore;
        displayStampInt = new TimestampInt(startingTurn, 0, 0, 0, 0);
        liveStampInt = new TimestampInt(startingTurn+1, 0 ,0 ,0, 0);
    }

    public void update() {
        if(tracking) {
            // Update multithreading mess
            return;
        }
        if(playing) {
            // See if we're at the end of available stuff to show.
            if(true) {

            }
            // Get nanosecond difference and change progress, or action/event if needed
        }
    }

    /**
     * Tracks like a video/movie, moving the game state forward or backwards by finding and running
     * all actions between the currently displayed action and the destination specified.
     * @param destinationTimestampInt Int array representing Turn/SubTurn/Event/Action/Progress to "track" to
     */
    public void track(TimestampInt destinationTimestampInt) {
        // Don't attempt if we're already tracking in another thread.
        if(!tracking) {
            // Don't bother if the timestamp is the same as ours
            if(!displayStampInt.isSame(destinationTimestampInt)) {
                // Don't bother if destination turn doesn't exist
                if(turns.containsTurn(destinationTimestampInt.getTurn())) {
                    Timestamp currentStamp = new Timestamp(this, displayStampInt);
                    Timestamp destinationStamp = new Timestamp(this, destinationTimestampInt);
                    TimestampInt timeDelta = displayStampInt.getDelta(destinationTimestampInt);
                    int changeAmount = timeDelta.getAmount();
                    // Set progress, as it doesn't need to change game state only animations
                    currentActionProgress = destinationTimestampInt.getProgress();
                    // Run or reverse all turns, subturns, events, and actions to that point
                    // TODO: do this on another thread for a frame if the difference is large enough?
                    if(timeDelta.isPositive()) {
                        if(changeAmount > 1) {
                            currentStamp.getEvent().runFrom(displayStampInt.getAction());
                            if(changeAmount > 2) {
                                currentStamp.getSubTurn().runFrom(displayStampInt.getEvent());
                                if(changeAmount > 3) {
                                    run(displayStampInt.getTurn()+1, destinationTimestampInt.getTurn()-1);
                                }
                                destinationStamp.getTurn().runTo(destinationTimestampInt.getSubturn());
                            }
                            destinationStamp.getSubTurn().runTo(destinationTimestampInt.getEvent());
                        }
                        destinationStamp.getEvent().runThrough(destinationTimestampInt.getAction());
                    }
                    if(!timeDelta.isPositive()) {
                        if(changeAmount > 1) {
                            currentStamp.getEvent().reverseFrom(displayStampInt.getAction());
                            if(changeAmount > 2) {
                                currentStamp.getSubTurn().reverseBefore(displayStampInt.getEvent());
                                if(changeAmount > 3) {
                                    reverse(displayStampInt.getTurn()-1, destinationTimestampInt.getTurn()+1);
                                }
                                destinationStamp.getTurn().reverseTo(destinationTimestampInt.getSubturn());
                            }
                            destinationStamp.getSubTurn().reverseTo(destinationTimestampInt.getEvent());
                        }
                        destinationStamp.getEvent().reverseTo(destinationTimestampInt.getAction());
                    }
                    displayStamp = destinationStamp;
                    displayStampInt = destinationTimestampInt;
                }
            }
        }
    }

    public void addEvent(Event e) {
        turns.newTurn().getSubTurn(0).addEvent(e);
    }

    public Action getAction(TimestampInt t) {
        Turn turn = getTurn(t.getTurn());
        if(turn != null) {
            SubTurn subTurn = turn.getSubTurn(t.getSubturn());
            if(subTurn != null) {
                Event event = subTurn.getEvent(t.getEvent());
                if(event != null) {
                    return event.getAction(t.getAction());
                }
            }
        }
        return null;
    }

    public Action getNextAction() {
        // See if there is another action
        if(displayStamp.getEvent().hasMore(displayStampInt.getAction())) {
            return displayStamp.getEvent().getAction(displayStampInt.getAction()+1);
        } else if(displayStamp.getSubTurn().hasMore(displayStampInt.getEvent())) {
            return displayStamp.getSubTurn().getFirstAction();
        } else if(displayStamp.getTurn().hasMore(displayStampInt.getSubturn())) {
            return displayStamp.getTurn().getFirstAction();
        } else if(hasMore(displayStampInt.getTurn())) {
            return getTurn(displayStampInt.getTurn()+1).getFirstAction();
        } else {
            return null;
        }
    }

    public Action getFirstAction() {
        if(getSize() > 0) {
            return getTurn(0).getFirstAction();
        }
        return null;
    }

    public Turn getTurn(int i) {
        return turns.getTurn(i);
    }

    public int getSize() {
        return turns.getSize();
    }

    public StoryComponent getStoryComponent(int i) {
        return turns.getTurn(i);
    }

    public TimestampInt getDisplayStamp() {
        return displayStampInt;
    }

}