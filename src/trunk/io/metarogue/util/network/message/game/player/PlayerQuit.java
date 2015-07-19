package io.metarogue.util.network.message.game.player;

import io.metarogue.Main;
import io.metarogue.game.Player;
import io.metarogue.util.network.message.NetworkMessageImpl;

public class PlayerQuit extends NetworkMessageImpl {

    int playerID;

    public boolean sanitize() {
        //TODO: Sanitize player object
        return true;
    }

    public void run() {
        Main.getGame().getPlayers().remove(playerID);
    }

    public boolean isTCP() {
        return true;
    }

}