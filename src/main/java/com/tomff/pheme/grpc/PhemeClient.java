package com.tomff.pheme.grpc;

import com.tomff.pheme.GetValueRequest;
import com.tomff.pheme.Rumour;
import com.tomff.pheme.RumourServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PhemeClient {
    private static final Logger logger = Logger.getLogger(PhemeClient.class.getName());

    private final ManagedChannel channel;
    private final RumourServiceGrpc.RumourServiceBlockingStub blockingStub;

    public PhemeClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    public PhemeClient(ManagedChannelBuilder<?> channelBuilder) {
        channel = channelBuilder.build();
        blockingStub = RumourServiceGrpc
                .newBlockingStub(channel);
    }

    public Optional<Rumour> get() {
        GetValueRequest request = GetValueRequest
                .newBuilder()
                .build();

        try {
            return Optional.ofNullable(blockingStub
                    .withDeadlineAfter(10, TimeUnit.SECONDS)
                    .get(request));
        } catch (StatusRuntimeException e) {
            logger.warning("Failed to get peer value: " + e.getStatus());
        }

        return Optional.empty();
    }

    public void shutdown() throws InterruptedException {
        if (channel == null) {
            return;
        }

        channel.shutdown().awaitTermination(30, TimeUnit.SECONDS);
    }

    public void shutdownNow() {
        if (channel == null) {
            return;
        }

        channel.shutdownNow();
    }
}
