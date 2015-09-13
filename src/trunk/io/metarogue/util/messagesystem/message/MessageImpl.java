package io.metarogue.util.messagesystem.message;

public abstract class MessageImpl implements Message {

    int sender = -1;
    boolean tcp = false;

    public MessageImpl() {
        // Auto-generated constructor
    }

    public void setSender(int i) {
        sender = i;
    }

    public int getSender() {
        return sender;
    }

    // TODO: These shouldn't be return true in production, make abstract?
    public boolean sanitize() { return true; }
    public boolean verify() { return true; }

    public abstract boolean isTCP();

}