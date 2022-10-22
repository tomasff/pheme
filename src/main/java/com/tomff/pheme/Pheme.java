package com.tomff.pheme;

import com.tomff.pheme.services.Messenger;

public class Pheme {
    private PhemeState state;
    private Messenger messenger;

    public Pheme(PhemeState state, Messenger messenger) {
        this.state = state;
    }

    public void onJoin(Peer peer) {
        View activeView = state.getActiveView();

        if (activeView.isFull()) {
            Peer sampledPeer = activeView.sample();

            disconnect(sampledPeer);
            activeView.remove(sampledPeer);
        }

        for (Peer activePeer : activeView) {
            messenger.forwardJoin(activePeer, peer);
        }

        activeView.add(peer);
    }

    public void onDisconnect(Peer peer) {

    }

    public boolean disconnect(Peer peer) {
        return false;
    }
}
