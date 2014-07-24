package net.and0.metarogue.controller.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import net.and0.metarogue.controller.DBLoader;
import net.and0.metarogue.controller.ruby.RubyContainer;
import net.and0.metarogue.main.Main;
import net.and0.metarogue.model.Game;

import java.io.IOException;

public class Server {

    com.esotericsoftware.kryonet.Server server;

    boolean started = false;
    boolean local = true;

    // Reference to game object
    Game game;

    // Database connection
    public DBLoader dbLoader;
    // Ruby scripting container
    public static RubyContainer rubyContainer;

    public Server() {
        server = new com.esotericsoftware.kryonet.Server();

        // Register Kryo classes, same method for client and server for consistency
        if(!local) {
            Network.register(server);
        }

        // Create database connection
        dbLoader = new DBLoader(game);

        // Add object listeners
        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof TextMessage) {
                    TextMessage request = (TextMessage)object;
                    System.out.println(request.text);
                    Main.networktest = 1;

                    TextMessage response = new TextMessage();
                    response.text = "Thanks";
                    connection.sendTCP(response);
                }
            }
        });
    }

    public void start() {
        if(!started){
            try {
                server.bind(54555, 54777);
            } catch (IOException e) {
                e.printStackTrace();
            }
            server.start();
        }
        started = true;
    }

}