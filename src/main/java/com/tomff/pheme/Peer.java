package com.tomff.pheme;

import java.util.Objects;

public record Peer(String addr, int port) {
    public Peer {
        Objects.requireNonNull(addr);
    }
}
