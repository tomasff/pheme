package com.tomff.pheme;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class View implements Iterable<Peer> {
    private final int capacity;
    private final List<Peer> peers;
    private final Map<Peer, Integer> peerIndex;

    public View(int capacity) {
        this.capacity = capacity;

        this.peers = new ArrayList<>(capacity);
        this.peerIndex = new HashMap<>();
    }

    public void add(Peer peer) {
        if (isFull()) {
            return;
        }

        if (peerIndex.containsKey(peer)) {
            return;
        }

        peerIndex.put(peer, peers.size());
        peers.add(peer);
    }

    public void remove(Peer peer) {
        if (!peerIndex.containsKey(peer)) {
            return;
        }

        int position = peerIndex.get(peer);
        Peer lastPeer = peers.remove(peers.size() - 1);

        peers.set(position, lastPeer);
        peerIndex.remove(peer);
        peerIndex.put(lastPeer, position);
    }

    public Peer sample() {
        return peers.get(ThreadLocalRandom.current().nextInt(0, peers.size()));
    }

    public boolean isFull() {
        return peers.size() >= capacity;
    }

    @Override
    public Iterator<Peer> iterator() {
        return peers.iterator();
    }
}
