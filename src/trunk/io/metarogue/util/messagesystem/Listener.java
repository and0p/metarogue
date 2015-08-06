package io.metarogue.util.messagesystem;

import io.metarogue.util.messagesystem.message.Message;

public interface Listener {

        void receive(Message m);

}