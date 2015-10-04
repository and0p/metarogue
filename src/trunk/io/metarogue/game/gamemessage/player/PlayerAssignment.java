package io.metarogue.game.gamemessage.player;

import io.metarogue.Main;
import io.metarogue.util.messagesystem.message.MessageImpl;

public class PlayerAssignment extends MessageImpl {

    int playerID;

    public PlayerAssignment() {
    }

    public PlayerAssignment(int playerID) {
        this.playerID = playerID;
    }

    public boolean isTCP() {
        return true;
    }

    public void run() {
        Main.getClient().setClientID(playerID);
        Main.getClient().setPlayer(playerID);
        //Main.getClient().setPlayer(playerID);
    }

}