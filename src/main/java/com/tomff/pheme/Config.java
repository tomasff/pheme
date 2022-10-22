package com.tomff.pheme;

import java.util.Collection;
import java.util.Objects;

public record Config(int port, int threadPoolSize, int gossipDelay, int activeViewCapacity, String initialValue, Collection<Peer> peers) {
    public Config {
        Objects.requireNonNull(peers);
        Objects.requireNonNull(initialValue);
    }
}
