package com.tomff.pheme;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SimpleConvergenceIT {
    private static final int BASE_PORT = 3200;

    private Collection<Peer> generatePeersForOffset(int targetOffset, int basePort, int numPeers) {
        return IntStream.range(0, numPeers)
                .filter(offset -> offset != targetOffset)
                .mapToObj(offset -> new Peer("127.0.0.1", basePort + offset))
                .collect(Collectors.toSet());
    }

    private PhemeMain generatePeerWithOffset(int offset, int basePort, int numPeers, String initialValue) {
        return new PhemeMain(
                new Config(
                    basePort + offset,
                    2,
                    5,
                    1,
                    initialValue,
                    generatePeersForOffset(offset, basePort, numPeers)
                )
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {10})
    public void convergenceHappens(int numPeers) throws IOException, InterruptedException {
        List<PhemeMain> peers = new ArrayList<>();

        for (int i = 0; i < numPeers - 1; i++) {
            PhemeMain peer = generatePeerWithOffset(i, BASE_PORT, numPeers, "not sus");

            peers.add(peer);
            peer.start();
        }

        PhemeMain rumourPeer = generatePeerWithOffset(numPeers - 1, BASE_PORT, numPeers, "sus");
        peers.add(rumourPeer);

        rumourPeer.start();

        // TODO(#1): Estimate time for rumour to spread based on fanout and number of peers
        Thread.sleep(60_000L);

        for (PhemeMain peer : peers) {
            assertEquals(peer.getState().getValue(), rumourPeer.getState().getValue());
        }
    }

}
