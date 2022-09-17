package com.tomff.pheme;

import com.tomff.pheme.services.PeerSamplingService;

import java.util.logging.Logger;

public class Gossip implements Runnable {
    private static final Logger logger = Logger.getLogger(Gossip.class.getName());

    private final PeerSamplingService peerSampling;
    private final PhemeState phemeState;

    public Gossip(PeerSamplingService peerSampling, PhemeState phemeState) {
        this.peerSampling = peerSampling;
        this.phemeState = phemeState;
    }

    @Override
    public void run() {
        Peer targetPeer = peerSampling.sample();
        logger.info("Gossiping with " + targetPeer);

        PhemeClient client = new PhemeClient(targetPeer.addr(), targetPeer.port());

        Value receivedValue = client.get();
        Value localValue = phemeState.getValue();

        logger.info("Received " + receivedValue);

        if (receivedValue.getTimestamp() > localValue.getTimestamp()) {
            logger.info("Newer value " + receivedValue.getValue() + " with timestamp " + receivedValue.getTimestamp() + " received, updating.");
            phemeState.setValue(receivedValue);
        }

        try {
            client.shutdown();
        } catch (InterruptedException e) {
            logger.severe(e.getMessage());
        }
    }
}
