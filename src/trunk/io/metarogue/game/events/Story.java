package io.metarogue.game.events;

import io.metarogue.game.events.Time.Moment;
import io.metarogue.game.events.Time.TimeState;
import io.metarogue.game.events.Time.Timestamp;
import io.metarogue.game.events.actions.Action;
import io.metarogue.util.Timer;

public class Story extends StoryComposite {

    // Turns passed before "live" turn
    int totalTurns;
    // Number of turns passed before creation. For example if client connects at the display of turn 80 then this is 79
    int startingTurn;

    TimeState timeState;

    // Info as to play status, live turn, and display turn
    boolean playing;
    boolean tracking = false; // Larger tracking operations should be multithreaded, this boolean is for locking
    float displayActionProgress = 0; // Progress of currently displayed action
    float displayActionStartProgress = 0; // Progress that current animation started playing at
    long displayActionStartTime; // Nanosecond that currently displayed action started playing
    Action displayAction;

    Timestamp displayStampInt;
    Moment displayStamp;
    Turn displayTurn;
    Timestamp liveStampInt;
    Turn upcomingTurn; // Turn that players are submitting events for

    TurnCollection turns;

    public Story(int turnsFromBefore) {
        turns= new TurnCollection(1000, turnsFromBefore); //TODO: Make history size configurable
        this.startingTurn = turnsFromBefore;
        displayStampInt = new Timestamp(startingTurn, 0, 0, 0, 0);
        liveStampInt = new Timestamp(startingTurn+1, 0 ,0 ,0, 0);
        displayActionStartTime = Timer.getNanoTime();
        playing = true;
    }

    public void update() {
        if(tracking) {
            // See if tracking is done in other thread, when multi-threading implemented
            return;
        }
        if(playing) {
            // See if we're at the end of available stuff to show.
            if(displayActionProgress >= 1 && getNextAction() != null) {
                return;
            }
            // If not start calculating next state to display
            long systemTime = Timer.getNanoTime();
            // Convert current animation duration to long nanotime
            long displayActionDuration = Timer.convertMillisecondsToNanoseconds(displayAction.getAnimation().getDuration());
            // See if there's anything left in current action to display
            if((displayActionStartTime + displayActionDuration) <= systemTime) {
                // Set progress of current animation(s)
                long progressTime = systemTime - displayActionStartTime;
                displayActionProgress = progressTime / displayActionDuration;
            }
            // Get nanosecond difference and change progress, or action/event if needed
        }
    }

    /**
     * Tracks like a video/movie, moving the game state forward or backwards by finding and running
     * all actions between the currently displayed action and the destination specified.
     * @param destinationTimestamp Int array representing Turn/SubTurn/Event/Action/Progress to "track" to
     */
    public void track(Timestamp destinationTimestamp) {
        // Don't attempt if we're already tracking in another thread.
        if(!tracking) {
            // Don't bother if the timestamp is the same as ours
            if(!displayStampInt.isSame(destinationTimestamp)) {
                // Don't bother if destination turn doesn't exist
                if(turns.containsTurn(destinationTimestamp.getTurn())) {
                    Moment currentMoment = new Moment(this, displayStampInt);
                    Moment destinationMoment = new Moment(this, destinationTimestamp);
                    Timestamp timeDelta = displayStampInt.getDelta(destinationTimestamp);
                    int changeAmount = timeDelta.getAmount();
                    // Set progress, as it doesn't need to change game state only animations
                    displayActionProgress = destinationTimestamp.getProgress();
                    displayActionStartTime = Timer.getNanoTime();
                    // Run or reverse all turns, subturns, events, and actions to that point
                    // TODO: do this on another thread for a frame if the difference is large enough?
                    if(timeDelta.isPositive()) {
                        if(changeAmount > 1) {
                            currentMoment.getEvent().runFrom(displayStampInt.getAction());
                            if(changeAmount > 2) {
                                currentMoment.getSubTurn().runFrom(displayStampInt.getEvent());
                                if(changeAmount > 3) {
                                    run(displayStampInt.getTurn()+1, destinationTimestamp.getTurn()-1);
                                }
                                destinationMoment.getTurn().runTo(destinationTimestamp.getSubturn());
                            }
                            destinationMoment.getSubTurn().runTo(destinationTimestamp.getEvent());
                        }
                        destinationMoment.getEvent().runThrough(destinationTimestamp.getAction());
                    }
                    if(!timeDelta.isPositive()) {
                        if(changeAmount > 1) {
                            currentMoment.getEvent().reverseFrom(displayStampInt.getAction());
                            if(changeAmount > 2) {
                                currentMoment.getSubTurn().reverseBefore(displayStampInt.getEvent());
                                if(changeAmount > 3) {
                                    reverse(displayStampInt.getTurn()-1, destinationTimestamp.getTurn()+1);
                                }
                                destinationMoment.getTurn().reverseTo(destinationTimestamp.getSubturn());
                            }
                            destinationMoment.getSubTurn().reverseTo(destinationTimestamp.getEvent());
                        }
                        destinationMoment.getEvent().reverseTo(destinationTimestamp.getAction());
                    }
                    displayAction = destinationMoment.getAction();
                    displayStamp = destinationMoment;
                    displayStampInt = destinationTimestamp;
                }
            }
        }
    }

    public void addEvent(Event e) {
        turns.newTurn().getSubTurn(0).addEvent(e);
    }

    public Action getAction(Timestamp t) {
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

    public Timestamp getDisplayStamp() {
        return displayStampInt;
    }

}