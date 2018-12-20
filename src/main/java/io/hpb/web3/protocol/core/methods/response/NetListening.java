package io.hpb.web3.protocol.core.methods.response;

import io.hpb.web3.protocol.core.Response;


public class NetListening extends Response<Boolean> {
    public boolean isListening() {
        return getResult();
    }
}
