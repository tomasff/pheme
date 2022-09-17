package com.tomff.pheme.services;

import com.tomff.pheme.Peer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PeerSamplingService {
    private List<Peer> peers;

    public PeerSamplingService(List<Peer> peers) {
        this.peers = peers;
    }

    public Peer sample() {
        return peers.get(ThreadLocalRandom.current().nextInt(0, peers.size()));
    }
}
