package com.tomff.pheme;

import com.tomff.pheme.services.PeerValueService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;

import java.io.IOException;
import java.util.concurrent.Executor;
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
    }
}
