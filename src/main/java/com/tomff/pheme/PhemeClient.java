package com.tomff.pheme;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;

public class PhemeClient {
    private final ManagedChannel channel;
    private final PeerValueGrpc.PeerValueBlockingStub blockingStub;

    public PhemeClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    public PhemeClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        blockingStub = PeerValueGrpc
                .newBlockingStub(channel);
    }

    public Value get() {
        GetValueRequest request = GetValueRequest
                .newBuilder()
                .build();

        return blockingStub.get(request);
    }

    public void shutdown() throws InterruptedException {
        if (channel == null) {
            return;
        }

        channel.shutdown().awaitTermination(30, TimeUnit.SECONDS);
    }
}
