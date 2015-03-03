package io.metarogue.game.events;

import io.metarogue.game.events.actions.Action;

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

    // Run all objects until object x
    public void runTo(int x) {
        if(x >= getSize()) {
            x = getSize()-1;
        } else if(x < 0) {
            return;
        }
        for(int i = 0; i < x; i++) {
            getStoryComponent(i).run();
        }
    }

    // Run all objects up to and including object x
    public void runThrough(int x) {
        if(x >= getSize()) {
            x = getSize()-1;
        } else if(x < 0) {
            return;
        }
        for(int i = 0; i <= x; i++) {
            getStoryComponent(i).run();
        }
    }


    // Reverse from final object to object x
    public void reverseTo(int x) {
        if(x > getSize()) {
            return;
        } else if(x < 0) {
            x = 0;
        }
        for(int i = getSize()-1; i > x; i--) {
            getStoryComponent(i).reverse();
        }
    }

    // Reverse from final object to and including object x
    public void reverseThrough(int x) {
        if(x > getSize()) {
            return;
        } else if(x < 0) {
            x = 0;
        }
        for(int i = getSize()-1; i >= x; i--) {
            getStoryComponent(i).reverse();
        }
    }

    // Run all objects starting after point x
    public void runAfter(int x) {
        if(x+1 >= getSize()) {
            return;
        } else if(x < 0) {
            x = 0;
        }
        for(int i = x+1; i < getSize()-1; i++) {
            getStoryComponent(i).run();
        }
    }

    // Run all objects starting with point x
    public void runFrom(int x) {
        if(x+1 >= getSize()) {
            return;
        } else if(x < 0) {
            x = 0;
        }
        for(int i = x; i < getSize()-1; i++) {
            getStoryComponent(i).run();
        }
    }

    // Reverse all object from point x backwards
    public void reverseFrom(int x) {
        if(x > getSize()) {
            x = getSize()-1;
        } else if(x < 0) {
            return;
        }
        for(int i = x; i >= 0; i--) {
            getStoryComponent(i).reverse();
        }
    }

    // Reverse all object from point x backwards
    public void reverseBefore(int x) {
        if(x > getSize()) {
            x = getSize()-1;
        } else if(x < 0) {
            return;
        }
        for(int i = x-1; i >= 0; i--) {
            getStoryComponent(i).reverse();
        }
    }

    public boolean hasMore(int i) {
        if(i < getSize()-1) {
            return true;
        }
        return false;
    }

    public abstract Action getFirstAction();

    public abstract int getSize();

    public abstract StoryComponent getStoryComponent(int i);

    public int getAnimationTime() {
        return 0;
    }

}