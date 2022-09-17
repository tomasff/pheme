package com.tomff.pheme;

import com.tomff.pheme.services.PeerSamplingService;
import io.grpc.StatusRuntimeException;

import java.util.Optional;
import java.util.logging.Logger;

public class Gossip implements Runnable {
    private static final Logger logger = Logger.getLogger(Gossip.class.getName());

    private final PeerSamplingService peerSampling;
    private final PhemeState phemeState;

    public Gossip(PeerSamplingService peerSampling, PhemeState phemeState) {
        this.peerSampling = peerSampling;
        this.phemeState = phemeState;
    }

    private void updateStateIfNewer(Value value) {
        Value localValue = phemeState.getValue();

        if (value.getTimestamp() > localValue.getTimestamp()) {
            logger.info("Newer value " + value.getValue() + " with timestamp " + value.getTimestamp() + " received, updating.");
            phemeState.setValue(value);
        }
    }

    @Override
    public void run() {
        Peer targetPeer = peerSampling.sample();
        logger.info("Gossiping with " + targetPeer);

        PhemeClient client = new PhemeClient(targetPeer.addr(), targetPeer.port());
        Optional<Value> receivedValue = client.get();

        logger.info("Received " + receivedValue);

        receivedValue.ifPresent(this::updateStateIfNewer);

        try {
            client.shutdown();
        } catch (InterruptedException e) {
            logger.severe(e.getMessage());
        }
    }
}
