package io.hpb.web3.protocol.core.methods.response;

import io.hpb.web3.protocol.core.Response;


public class NetVersion extends Response<String> {
    public String getNetVersion() {
        return getResult();
    }
}
