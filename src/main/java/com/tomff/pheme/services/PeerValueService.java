package com.tomff.pheme.services;

import com.tomff.pheme.GetValueRequest;
import com.tomff.pheme.PeerValueGrpc;
import com.tomff.pheme.PhemeState;
import com.tomff.pheme.Value;
import io.grpc.stub.StreamObserver;

public class PeerValueService extends PeerValueGrpc.PeerValueImplBase {
    private final PhemeState state;

    public PeerValueService(PhemeState state) {
        this.state = state;
    }

    @Override
    public void get(GetValueRequest request, StreamObserver<Value> responseObserver) {
        responseObserver.onNext(state.getValue());
        responseObserver.onCompleted();
    }

    @Override
    public void set(Value request, StreamObserver<Value> responseObserver) {
        state.setValue(request);

        responseObserver.onNext(state.getValue());
        responseObserver.onCompleted();
    }
}
