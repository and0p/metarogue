package io.metarogue.util.messagesystem.message.connection;

import io.metarogue.util.Log;
import io.metarogue.util.Timer;
import io.metarogue.util.messagesystem.message.MessageImpl;

/**
 * Created by and0 on 7/5/2015.
 */

public class PingMessage extends MessageImpl {

    long senderTime;
    long receiverTime;

    public PingMessage() {
        senderTime = Timer.getNanoTime();
    }

    public void setSenderTime() {
        senderTime = Timer.getNanoTime();
    }

    public void setReceiverTime() {
        receiverTime = Timer.getNanoTime();
    }

    public void run() {
            long delta = Timer.getNanoTime() - senderTime;
            Log.log("Ping came back after " + delta +  " nanoseconds.");
    }

    public boolean isTCP() {
        return false;
    }


}