package net.and0.metarogue.controller.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import net.and0.metarogue.model.Game;

import java.io.IOException;

/**
 * MetaRogue class
 * User: andrew
 * Date: 7/20/14
 * Time: 11:14 PM
 */
public class Server {

    com.esotericsoftware.kryonet.Server server;

    Game game;

    public Server() {
        server = new com.esotericsoftware.kryonet.Server();
        server.start();
        try {
            server.bind(54555, 54777);
        } catch (IOException e) {
            e.printStackTrace();
        }

        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof TextMessage) {
                    TextMessage request = (TextMessage)object;
                    System.out.println(request.text);

                    TextMessage response = new TextMessage();
                    response.text = "Thanks";
                    connection.sendTCP(response);
                }
            }
        });
    }

}