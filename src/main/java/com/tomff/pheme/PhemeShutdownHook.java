package com.tomff.pheme;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class PhemeShutdownHook extends Thread {

    private ExecutorService executor;

    public PhemeShutdownHook(ExecutorService executor) {
        this.executor = executor;
    }

    @Override
    public void run() {
        // Use stderr here since the logger may have been reset by its JVM shutdown hook.
        System.err.println("*** shutting down Pheme since JVM is shutting down");
        executor.shutdown();

        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();

                if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                    System.err.println("Pool did not terminate");
                }
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            e.printStackTrace(System.err);

            Thread.currentThread().interrupt();
        }

        System.err.println("*** Pheme shut down");
    }
}
