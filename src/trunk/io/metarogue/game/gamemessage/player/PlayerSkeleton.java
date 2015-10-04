package io.metarogue.game.gamemessage.player;

import io.metarogue.Main;
import io.metarogue.game.Player;
import io.metarogue.util.messagesystem.message.MessageImpl;

public class PlayerSkeleton extends MessageImpl {

    public int id;
    public String nickname;
    public long timeOfConnection;
    public boolean registered = false;

    public boolean sanitize() {
        //TODO: Sanitize player object
        return true;
    }

    public void run() {
        Main.getGame().getPlayers().put(id, new Player(this));
        }

public boolean isTCP() {
        return true;
        }

        }