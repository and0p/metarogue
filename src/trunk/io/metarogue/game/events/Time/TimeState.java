package io.metarogue.game.events.Time;

import io.metarogue.game.events.Story;
import io.metarogue.game.events.Turn;
import io.metarogue.game.events.actions.Action;

/**
 * Holds all relevant information in regards to current point in game time, replay time, also if game is
 * playing or paused. Has methods to manipulate this and get time/sync info for animations.
 */

public class TimeState {

    Story story;

    boolean playing;
    boolean tracking = false; // Larger tracking operations should be multithreaded, this boolean is for locking

    float displayActionProgress = 0; // Progress of currently displayed action
    float displayActionStartProgress = 0; // Progress that current animation started playing at
    long displayActionStartTime; // Nanosecond that currently displayed action started playing
    Action displayAction;

    Moment displayMoment; // Objects currently being displayed
    Timestamp displayStamp; // T:S:E:A timestamp of moment being displayed
    Timestamp liveStamp; // T:S:E:A timestamp of whatever the "live" or most up-to-date moment is

    public TimeState(Story story) {
        this.story = story;
    }

    public void play() {
        playing = true;
    }

    public void pause() {
        playing = false;
    }

    public void togglePlay() {
        if(playing) {
            playing = false;
        } else {
            playing = true;
        }
    }

}
