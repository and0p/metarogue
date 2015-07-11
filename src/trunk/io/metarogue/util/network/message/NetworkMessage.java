package io.metarogue.util.network.message;

import io.metarogue.server.ServerNetwork;

public interface NetworkMessage {

    void setSender(int i);
    int getSender();

    boolean sanitize();  // Sanitize raw data, for example integers not out of bounds, etc

    boolean verify();    // Verify command within game context, for example player has rights to a unit

    void run();          // Run message

}