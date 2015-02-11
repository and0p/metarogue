package io.metarogue.game.events;

public abstract class StoryComposite implements StoryComponent{

    int id;
    int animationTime;

    public StoryComposite() {
        id = 0;
        animationTime = 0;
    }

    public StoryComposite(int id) {
        this.id = id;
        animationTime = 0;
    }

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
        for(int i = y; i >= x; i--) {
            getStoryComponent(i).run();
        }
    }

    // Run all objects until object x
    public void runTo(int x) {
        if(x > getSize()) {
            return;
        } else {
            for(int i = 0; i < x; i++) {
                getStoryComponent(i).run();
            }
        }
    }

    // Reverse from final object to object x
    public void reverseTo(int x) {
        if(x > getSize() || x < 0) {
            return;
        } else {
            for(int i = getSize()-1; i >= 0; i--) {
                getStoryComponent(i).reverse();
            }
        }
    }

    // Run all object starting at point x
    public void runFrom(int x) {
        if(x > getSize()) {
            return;
        } else {
            for(int i = x; i < getSize()-1; i++) {
                getStoryComponent(i).run();
            }
        }
    }

    // Reverse all object from point x backwards
    public void reverseFrom(int x) {
        if(x > getSize()) {
            return;
        } else {
            for(int i = x; i >= 0; i--) {
                getStoryComponent(i).reverse();
            }
        }
    }

    public abstract int getSize();

    public abstract StoryComponent getStoryComponent(int i);

    public int getAnimationTime() {
        return 0;
    }

}
