package io.hpb.web3.protocol.core.methods.response;

import io.hpb.web3.protocol.core.Response;


public class ShhNewIdentity extends Response<String> {

    public String getAddress() {
        return getResult();
    }
}
