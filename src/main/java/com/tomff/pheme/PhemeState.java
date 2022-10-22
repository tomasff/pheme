package com.tomff.pheme;

import java.util.HashSet;
import java.util.Set;

public class PhemeState {
    private Config config;
    private Rumour rumour;

    private View activeView;
    private View passiveView;

    public PhemeState(Config config) {
        this.config = config;

        this.rumour = Rumour.newBuilder()
                .setValue(config.initialValue())
                .setTimestamp(System.currentTimeMillis())
                .build();

        this.activeView = new View(config.activeViewCapacity());
        this.passiveView = new View(config.activeViewCapacity()); // TOOD: change
    }

    public synchronized void setValue(Rumour rumour) {
        this.rumour = rumour;
    }

    public synchronized Rumour getValue() {
        return rumour;
    }

    public synchronized boolean updateIfNewer(Rumour otherRumour) {
        if (otherRumour.getTimestamp() > rumour.getTimestamp()) {
            this.rumour = otherRumour;
            return true;
        }

        return false;
    }

    public View getActiveView() {
        return activeView;
    }
}
