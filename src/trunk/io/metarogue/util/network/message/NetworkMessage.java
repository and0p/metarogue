package io.metarogue.util.network.message;

import io.metarogue.server.ServerNetwork;

public interface NetworkMessage {

    public boolean verify();

    public void run();

}