package com.hpb.web3.protocol.core.methods.response;

import com.hpb.web3.protocol.core.Response;


public class ShhPost extends Response<Boolean> {

    public boolean messageSent() {
        return getResult();
    }
}
