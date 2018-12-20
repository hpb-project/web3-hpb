package io.hpb.web3.protocol.core.methods.response;

import io.hpb.web3.protocol.core.Response;


public class VoidResponse extends Response<Void> {
    public boolean isValid() {
        return !hasError();
    }
}
