package com.hpb.web3.protocol.core.methods.response;

import com.hpb.web3.protocol.core.Response;


public class HpbSign extends Response<String> {
    public String getSignature() {
        return getResult();
    }
}
