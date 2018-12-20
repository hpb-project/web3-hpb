package io.hpb.web3.protocol.core.methods.response;

import io.hpb.web3.protocol.core.Response;


public class HpbSign extends Response<String> {
    public String getSignature() {
        return getResult();
    }
}
