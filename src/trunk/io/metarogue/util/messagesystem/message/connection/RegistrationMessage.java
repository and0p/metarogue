package io.metarogue.util.messagesystem.message.connection;

import io.metarogue.Main;
import io.metarogue.game.Player;
import io.metarogue.server.user.User;
import io.metarogue.util.messagesystem.message.MessageImpl;
import io.metarogue.util.messagesystem.message.game.player.PlayerAssignment;
import io.metarogue.util.messagesystem.type.MetaMessage;

public class RegistrationMessage extends MessageImpl implements MetaMessage {

    String name;
    String nick;
    String password;

    // Zero-arg constructor for Kryo
    // TODO: Write serializer myself? https://code.google.com/p/kryo/issues/detail?id=5
    public RegistrationMessage() {
    }

    public RegistrationMessage(String name, String password, String nick) {
        this.name = name;
        this.password = password; //TODO: Encrypt, etc
        this.nick = nick;
    }

    public RegistrationMessage(String nick) { this.nick = nick; }

    public boolean verify() {
        //check if name exists, if it does increment and verify() again (which will increment again and so on...)
        return true;
    }

    public boolean isTCP() {
        return true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}