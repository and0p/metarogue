package io.metarogue.game.events;

// Wrapped series of integers that represent a point in the game's time.

public class TimestampInt {

    int turn;
    int subturn;
    int event;
    int action;
    float progress;

    public TimestampInt(int turn, int subturn, int event, int action, float progress) {
        this.turn = turn;
        this.subturn = subturn;
        this.event = event;
        this.action = action;
        this.progress = progress;
    }

    public TimestampInt(int turn, int subturn, int event, int action) {
        this.turn = turn;
        this.subturn = subturn;
        this.event = event;
        this.action = action;
        progress = 0;
    }

    public TimestampInt(int turn, int subturn, int event) {
        this.turn = turn;
        this.subturn = subturn;
        this.event = event;
        action = 0;
        progress = 0;
    }

    public TimestampInt(int turn, int subturn) {
        this.turn = turn;
        this.subturn = subturn;
        this.event = 0;
        this.action = 0;
        progress = 0;
    }

    public TimestampInt(int turn) {
        this.turn = turn;
        this.subturn = 0;
        this.event = 0;
        this.action = 0;
        progress = 0;
    }

    /**
     * See if passed TimestampInt is identical to this one
     * @param t TimestampInt to isSame
     * @return True if they are identical
     */
    public boolean isSame(TimestampInt t) {
        if(this.turn == t.getTurn() && this.subturn == t.getSubturn() && this.event == t.getEvent() && this.action == t.getAction() && this.progress == t.getProgress()) return true;
        return false;
    }

    /** Return difference between current timestamp and passed timestamp
     * NOTICE: Because there is no set number of any composite, this isn't like a decimal system i.e. if you went
     * from 1.0.0.5.0 to 10.0.0.1.0 then the "action" change would be -4 despite the whole thing being forward in time.
     * Basically this is only useful for seeing the amount / precision of change in other methods.
     * @param t Timestamp to get delta of
     * @return New TimestampInt as result
     */
    public TimestampInt getDelta(TimestampInt t) {
        return new TimestampInt(t.getTurn()-turn, t.getSubturn()-subturn, t.getEvent()-event, t.getAction()-action, t.getProgress()-progress);
    }

    /**
     * Gives int representing how precise a timestamp (probably a delta) is
     * @return 0-4 for if precision is at turn, subturn, event, action, or progress level
     */
    public int getPrecision() {
        if(progress != 0) {
            return 4;
        }
        if(action != 0) {
            return 3;
        }
        if(event != 0) {
            return 2;
        }
        if(subturn != 0) {
            return 1;
        }
        return 0;
    }

    /**
     * Get the amount of change represented by a timestamp (which should represent a "delta" or change in time)
     * @return 0-4 depending on change being only progress or a full action/event/subturn/ and finally turn.
     */
    public int getAmount() {
        if(turn != 0) {
            return 4;
        }
        if(subturn != 0) {
            return 3;
        }
        if(event != 0) {
            return 2;
        }
        if(action != 0) {
            return 1;
        }
        return 0;
    }

    /**
     * Check if any change amount in time is ultimately forward or backwards in time
     * @return Returns true if time delta is positive or "forward" in time
     */
    public boolean isPositive() {
        if(turn > 0) {
            return true;
        }
        if(subturn > 0) {
            return true;
        }
        if(event > 0) {
            return true;
        }
        if(action > 0) {
            return true;
        }
        return false;
    }

    public TimestampInt copy() {
        return new TimestampInt(turn, subturn, event, action, progress);
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public int getSubturn() {
        return subturn;
    }

    public void setSubturn(int subturn) {
        this.subturn = subturn;
    }

    public int getEvent() {
        return event;
    }

    public void setEvent(int event) {
        this.event = event;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public float getProgress() {return progress; }

    public void setProgress(float progress) { this.progress = progress; }

}
