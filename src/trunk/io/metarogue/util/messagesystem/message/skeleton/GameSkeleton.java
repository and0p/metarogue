package io.metarogue.util.messagesystem.message.skeleton;

import io.metarogue.Main;
import io.metarogue.game.Game;
import io.metarogue.game.timeline.animation.Animation;
import io.metarogue.util.messagesystem.message.MessageImpl;

import java.util.ArrayList;

public class GameSkeleton extends MessageImpl {

    public String name;

    public Animation defaultAnimation;

    public long startTime;

    public ArrayList<String> sides;

    public boolean sanitize() {
        //TODO: Sanitize fields
        return true;
    }

    public void run() {
        if(Main.getGame() == null) {
            Main.setGame(new Game(this));
            Main.getGame().init();
        }
    }

    public boolean isTCP() {
        return true;
    }

}