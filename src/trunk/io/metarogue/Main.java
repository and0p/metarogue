package io.metarogue;

import java.io.IOException;

import io.metarogue.client.GameClient;
import io.metarogue.client.view.threed.Vector3d;
import io.metarogue.game.gameobjects.GameObject;
import io.metarogue.server.GameServer;
import io.metarogue.game.Game;
import io.metarogue.util.network.Network;
import org.lwjgl.*;

public class Main {

    static enum ProgramState { CLIENT, LISTEN, DEDICATED }
    static enum NetworkState { ONLINE, LOCAL}
    static String IPToConnectTo;
    static String gameToLoad = "Test";

    // Game client and servers
    static GameServer server;
    static GameClient client;

    // Game model
    static Game game;

    static ProgramState programState;
    static NetworkState networkState;

    // Temp init logic
    static void init() {
        // Listen client/server
        if(programState == ProgramState.LISTEN) {
            client = new GameClient();
            server = new GameServer(gameToLoad);
            client.setPlayer(Game.defaultPlayer);
            client.setActiveWorld(game.getDefaultWorld());
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
		while(!org.lwjgl.opengl.Display.isCloseRequested()) {
            if(server != null) {
                server.update();
            }
            if(client != null) {
                client.update();
            }
		}

        // Cleanup
        // game.close();
	}

    static boolean parseArguments(String[] args) {
        // Parse arguments. Return false if the arguments are invalid so program can exit.

        // If there are no arguments, assume unconnected client
        // TODO: For now local is listen server. Eventually client will have its own menu but for now there needs to be a "game"
        if(args.length == 0 || args[0] == "LISTEN") {
            programState = ProgramState.LISTEN;
            networkState = NetworkState.LOCAL;
        }
        if(args.length > 0) {
            // For client
            if(args[0].toUpperCase() == "CLIENT") {
                programState = ProgramState.CLIENT;
                if(args.length < 1) {
                    networkState = NetworkState.ONLINE;
                    IPToConnectTo = args[1];
                }
            }
            // For dedicated server, second argument is game name
            if(args[0].toUpperCase() == "DEDICATED") {
                programState = ProgramState.DEDICATED;
                if(args.length < 1) {
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

    public static ProgramState getProgramState() {
        return programState;
    }

    public static void setProgramState(ProgramState programState) {
        Main.programState = programState;
    }

    public static NetworkState getNetworkState() {
        return networkState;
    }

    public static void setNetworkState(NetworkState networkState) {
        Main.networkState = networkState;
    }

    // Get system time, for sync'ing stuff
    public static long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

}