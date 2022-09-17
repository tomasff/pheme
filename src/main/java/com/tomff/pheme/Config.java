package com.tomff.pheme;

import java.util.List;
import java.util.Objects;

public record Config(int port, int gossipDelay, String initialValue, List<Peer> peers) {
    public Config {
        Objects.requireNonNull(peers);
        Objects.requireNonNull(initialValue);
    }
}
