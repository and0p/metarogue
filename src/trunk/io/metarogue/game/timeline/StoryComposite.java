package io.metarogue.game.timeline;

import io.metarogue.game.gamemessage.GameMessage;

import java.util.ArrayList;

public abstract class StoryComposite implements StoryComponent{

    int id;
    int animationTime;

    // Run all components
    public void run() {
        for(int i = 0; i < getSize()-1; i++) {
            getStoryComponent(i).run();
        }
    }

    // Reverse all components
    public void reverse() {
        for(int i = getSize()-1; i >= 0; i--) {
            getStoryComponent(i).run();
        }
    }

    // Run between two components
    public void run(int x, int y) {
        for(int i = x; i < y; i++) {
            getStoryComponent(i).run();
        }
    }

    // Reverse all components
    public void reverse(int x, int y) {
        for(int i = x; i >= y; i--) {
            getStoryComponent(i).run();
        }
    }
    // See if there is another object after the one passed
    public boolean hasNewer(int i) {
        if(i < getSize()-1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasOlder(int i) {
        if(i > 0) {
            return true;
        } else {
            return false;
        }
    }

    public abstract int getSize();

    public abstract StoryComponent getStoryComponent(int i);

    // Returns all messages at along the bottom of entire tree that a StoryComposite could represent
    public abstract ArrayList<GameMessage> getAllGameMessages(ArrayList<GameMessage> array);

}