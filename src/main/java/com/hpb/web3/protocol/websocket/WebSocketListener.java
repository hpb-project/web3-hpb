package com.hpb.web3.protocol.websocket;

import java.io.IOException;


public interface WebSocketListener {

    
    void onMessage(String message) throws IOException;

    void onError(Exception e);

    void onClose();
}
