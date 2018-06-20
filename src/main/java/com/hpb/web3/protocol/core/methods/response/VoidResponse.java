package com.hpb.web3.protocol.core.methods.response;

import com.hpb.web3.protocol.core.Response;


public class VoidResponse extends Response<Void> {
    public boolean isValid() {
        return !hasError();
    }
}
