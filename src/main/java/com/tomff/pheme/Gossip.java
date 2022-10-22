package com.tomff.pheme;

import com.tomff.pheme.grpc.PhemeClient;

import java.util.Optional;
import java.util.logging.Logger;

public class Gossip implements Runnable {
    private static final Logger logger = Logger.getLogger(Gossip.class.getName());
    private final PhemeState phemeState;

    public Gossip(PhemeState phemeState) {
        this.phemeState = phemeState;
    }

    private void gossipWith(Peer peer) {
        logger.info("Gossiping with " + peer);

        PhemeClient client = new PhemeClient(peer.addr(), peer.port());
        Optional<Rumour> receivedValue = client.get();

        logger.info("Received " + receivedValue);

        receivedValue.map(phemeState::updateIfNewer)
                .ifPresent(isNewer -> logger.info("Updated internal state: " + isNewer));

        try {
            client.shutdown();
        } catch (InterruptedException e) {
            e.printStackTrace(System.err);

            client.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void run() {
//        for (Peer peer : peers) {
//            gossipWith(peer);
//        }
    }
}
