package com.tomff.pheme.services;

import com.tomff.pheme.JoinOverlayRequest;
import com.tomff.pheme.JoinOverlayRequestAck;
import com.tomff.pheme.MembershipServiceGrpc;
import com.tomff.pheme.PhemeState;
import io.grpc.stub.StreamObserver;

public class MembershipService extends MembershipServiceGrpc.MembershipServiceImplBase {

    private PhemeState state;

    public MembershipService(PhemeState state) {
        this.state = state;
    }

    @Override
    public void joinOverlay(JoinOverlayRequest request, StreamObserver<JoinOverlayRequestAck> responseObserver) {
        if (state.isActiveViewFull()) {

        }
    }
}
