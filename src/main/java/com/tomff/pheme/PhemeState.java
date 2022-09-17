package com.tomff.pheme;

public class PhemeState {
    private Value value;

    public PhemeState(Value value) {
        this.value = value;
    }

    public synchronized void setValue(Value value) {
        this.value = value;
    }

    public synchronized Value getValue() {
        return value;
    }
}
