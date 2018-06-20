package com.hpb.web3.protocol.core.methods.response;

import com.hpb.web3.protocol.core.Response;


public class HpbProtocolVersion extends Response<String> {
    public String getProtocolVersion() {
        return getResult();
    }
}
