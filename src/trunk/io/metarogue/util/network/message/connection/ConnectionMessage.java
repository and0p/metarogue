package io.metarogue.util.network.message.connection;

import io.metarogue.server.ServerNetwork;
import io.metarogue.util.network.message.NetworkMessageImpl;

public class ConnectionMessage extends NetworkMessageImpl {

    String name;
    String password;

    public ConnectionMessage(String name, String password) {
        this.name = name;
        this.password = password; //TODO: Encrypt, etc
    }

    public boolean verify(ServerNetwork server) {
        //check if name exists
        return true;
    }

}