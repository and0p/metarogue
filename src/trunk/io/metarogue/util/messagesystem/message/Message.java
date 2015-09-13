package io.metarogue.util.messagesystem.message;

public interface Message {

    void setSender(int i);
    int getSender();

    boolean sanitize();     // Sanitize raw data, for example integers not out of bounds, etc

    boolean verify();       // Verify command within game context, for example player has rights to a unit

    boolean isTCP();        // Check how this should be sent, TCP or UDP. Higher priority messages are TCP.

}