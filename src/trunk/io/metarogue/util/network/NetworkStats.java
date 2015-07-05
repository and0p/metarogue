package io.metarogue.util.network;

import io.metarogue.util.Timer;

/**
 * Created by and0 on 7/5/2015.
 */

public class NetworkStats {

    int ping;
    long lastPing;

    // TODO: Keep a running tally of the last X pings, for a smoother average.
    // TODO: If at all possible within Kryonet, track amount or % of dropped packets / messages

    public NetworkStats() {
        //recentPings = new int[5];
    }

    public void updatePing(int i) {
        lastPing = Timer.getNanoTime();
        ping = i;
    }

}