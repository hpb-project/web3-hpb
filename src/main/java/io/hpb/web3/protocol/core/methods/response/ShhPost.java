package io.hpb.web3.protocol.core.methods.response;

import io.hpb.web3.protocol.core.Response;


public class ShhPost extends Response<Boolean> {

    public boolean messageSent() {
        return getResult();
    }
}
