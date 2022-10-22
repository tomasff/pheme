package com.tomff.pheme.services;

import com.tomff.pheme.*;
import io.grpc.stub.StreamObserver;

public class RumourService extends RumourServiceGrpc.RumourServiceImplBase {
    private final PhemeState state;

    public RumourService(PhemeState state) {
        this.state = state;
    }

    @Override
    public void get(GetValueRequest request, StreamObserver<Rumour> responseObserver) {
        responseObserver.onNext(state.getValue());
        responseObserver.onCompleted();
    }

    @Override
    public void set(Rumour request, StreamObserver<Rumour> responseObserver) {
        state.setValue(request);

        responseObserver.onNext(state.getValue());
        responseObserver.onCompleted();
    }
}
