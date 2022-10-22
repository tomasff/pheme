package com.tomff.pheme;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.toml.TomlMapper;
import com.tomff.pheme.grpc.PhemeServer;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PhemeMain {
    private static final ObjectMapper objectMapper = new TomlMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

    private final Config config;
    private final PhemeState state;
    private final PhemeServer server;

    private final ScheduledExecutorService executor;

    public PhemeMain(Config config) {
      this.config = config;

      executor = Executors.newScheduledThreadPool(config.threadPoolSize());

      state = new PhemeState(config);

      server = new PhemeServer(config.port(), state, executor);
    }

    public void start() throws IOException {
        server.start();

        executor.scheduleAtFixedRate(
                new Gossip(state),
                0,
                config.gossipDelay(),
                TimeUnit.SECONDS
        );

        Runtime.getRuntime().addShutdownHook(new PhemeShutdownHook(executor));
    }

    public PhemeState getState() {
        return state;
    }

    private static String getConfigPath(String[] args) throws IOException {
        if (args.length == 0) {
            throw new IOException("Invalid config path.");
        }

        return args[0];
    }

    private static Config loadConfig(String path) throws IOException {
        return objectMapper.readValue(new File(path), Config.class);
    }

    public static void main(String[] args) throws IOException {
        String configPath = getConfigPath(args);
        Config config = loadConfig(configPath);

        PhemeMain pheme = new PhemeMain(config);
        pheme.start();
    }
}
