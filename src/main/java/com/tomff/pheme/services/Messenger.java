package com.tomff.pheme.services;

import com.tomff.pheme.Peer;

public interface Messenger {

    void forwardJoin(Peer peer, Peer joinedPeer);

}
