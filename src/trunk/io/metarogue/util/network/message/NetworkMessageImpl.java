package io.metarogue.util.network.message;

public abstract class NetworkMessageImpl implements NetworkMessage {

    int sender = -1;
    boolean tcp = false;

    public NetworkMessageImpl() {
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

    public void runAsClient() {
        run();
    }

    public void runAsServer() {
        run();
    }

    // TODO: Make abstract?
    public void run() {}

    public abstract boolean isTCP();

}