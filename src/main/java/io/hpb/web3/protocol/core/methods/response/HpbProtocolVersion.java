package io.hpb.web3.protocol.core.methods.response;

import io.hpb.web3.protocol.core.Response;


public class HpbProtocolVersion extends Response<String> {
    public String getProtocolVersion() {
        return getResult();
    }
}
