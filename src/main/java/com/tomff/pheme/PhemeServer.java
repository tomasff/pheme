package com.tomff.pheme;

import com.tomff.pheme.services.PeerValueService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class PhemeServer {
    private static final Logger logger = Logger.getLogger(PhemeServer.class.getName());

    private final Server server;
    private final int port;

    public PhemeServer(int port, PhemeState state, Executor executor) {
        this(ServerBuilder.forPort(port), state, port, executor);
    }

    public PhemeServer(ServerBuilder<?> serverBuilder, PhemeState state, int port, Executor executor) {
        this.port = port;
        this.server = serverBuilder
                .addService(new PeerValueService(state))
                .addService(ProtoReflectionService.newInstance()/* only for testing */)
                .executor(executor)
                .build();
    }

    public void start() throws IOException {
        if (server == null) {
            return;
        }

        server.start();

        logger.info("Starting Pheme server on port " + port);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");

            try {
                PhemeServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }

            System.err.println("*** server shut down");
        }));
    }

    public void stop() throws InterruptedException {
        if (server == null) {
            return;
        }

        server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
    }

    void blockUntilShutdown() throws InterruptedException {
        if (server == null) {
            return;
        }

        server.awaitTermination();
    }
}
