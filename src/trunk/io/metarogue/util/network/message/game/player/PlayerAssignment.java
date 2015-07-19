package io.metarogue.util.network.message.game.player;

import io.metarogue.Main;
import io.metarogue.util.network.message.NetworkMessageImpl;

public class PlayerAssignment extends NetworkMessageImpl {

    int playerID;

    public boolean isTCP() {
        return true;
    }

    public void runAsClient() {
        Main.getClient().setPlayer(playerID);
    }

}