package io.hpb.web3.protocol.core.methods.response;

import io.hpb.web3.protocol.core.Response;


public class HpbCall extends Response<String> {
    public String getValue() {
        return getResult();
    }
}
