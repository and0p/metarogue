package io.metarogue;

import java.io.IOException;

import io.metarogue.client.GameClient;
import io.metarogue.server.GameServer;
import io.metarogue.game.Game;
import io.metarogue.util.Log;
import io.metarogue.util.Timer;

public class Main {

    enum ProgramState { CLIENT, LISTEN, DEDICATED }
    enum NetworkState { ONLINE, LOCAL }
    static String IPToConnectTo;
    static String gameToLoad = "Test";

    // Game client and servers
    static GameServer server;
    static GameClient client;

    // Game model
    static Game game;

    static ProgramState programState;
    static NetworkState networkState;

    static boolean closeRequested = false;

    // Temp init logic
    static void init() {
        Log.log("Starting program...");
        // Listen client/server
        if(programState == ProgramState.LISTEN) {
            server = new GameServer(gameToLoad);
            client = new GameClient();
            client.setPlayer(Game.defaultPlayer);
            client.setActiveWorld(game.getDefaultWorld());
        } else if(programState == ProgramState.DEDICATED) {
            server = new GameServer(gameToLoad);
            server.goOnline();
        } else if(programState == ProgramState.CLIENT) {
            client = new GameClient();
            client.connect(IPToConnectTo);
        }
    }

 public static void main(String[] args) throws IOException {
        // Parse arguments passed to program. Close if they're invalid.
        if(!parseArguments(args)) {
            System.exit(1);
        }
        // Initialize game based on arguments
        init();
        // GAME LOOP
		while(!closeRequested) {
            Timer.update();
            if(client != null) {
                client.update();
            }
            if(server != null) {
                server.update();
            }
		}
        // Cleanup
        if(game != null) game.close();
        if(client != null) client.close();
	}

    static boolean parseArguments(String[] args) {
        // Parse arguments. Return false if the arguments are invalid so program can exit.

        // If there are no arguments, assume unconnected client
        // TODO: For now local is listen server. Eventually client will have its own menu but for now there needs to be a "game"
        if(args.length == 0 || args[0].equalsIgnoreCase("listen")) {
            programState = ProgramState.LISTEN;
            networkState = NetworkState.LOCAL;
        }
        if(args.length > 0) {
            // For client
            if(args[0].equalsIgnoreCase("client")) {
                programState = ProgramState.CLIENT;
                if(args.length > 1) {
                    networkState = NetworkState.ONLINE;
                     IPToConnectTo = args[1];
                } else {
                    return false;
                }
            }
            // For dedicated server, second argument is game name
            if(args[0].equalsIgnoreCase("dedicated")) {
                programState = ProgramState.DEDICATED;
                if(args.length <= 1) {
                    return false;
                } else {
                    gameToLoad = args[1];
                }
            }
        }
        return true;
    }

    // Getters and setters yay!
    public static Game getGame() {
        return game;
    }

    public static void setGame(Game game) {
        Main.game = game;
    }

    public static GameServer getServer() {
        return server;
    }

    public static void setServer(GameServer server) {
        Main.server = server;
    }

    public static GameClient getClient() {
        return client;
    }

    public static void setClient(GameClient client) {
        Main.client = client;
    }

    public static void requestClose() {
        closeRequested = true;
    }

}