package io.metarogue.util.network.message.skeleton;

import io.metarogue.Main;
import io.metarogue.game.Game;
import io.metarogue.game.events.animation.Animation;
import io.metarogue.util.network.message.NetworkMessageImpl;

import java.util.ArrayList;

/**
 * Created by and0 on 7/8/2015.
 */
public class GameSkeleton extends NetworkMessageImpl {

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