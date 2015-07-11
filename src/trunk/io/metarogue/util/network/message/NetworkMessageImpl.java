package io.metarogue.util.network.message;

public abstract class NetworkMessageImpl implements NetworkMessage {

    int sender = -1;

    public NetworkMessageImpl() {
        // Auto-generated constructor
    }

    public void setSender(int i) {
        sender = i;
    }

    public int getSender() {
        return sender;
    }

    public boolean sanitize() {return true; }

    public boolean verify() {
        return true;
    }

    public void run() {
    }

}