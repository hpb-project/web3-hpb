package io.hpb.web3.protocol.core.methods.response;

import io.hpb.web3.protocol.core.Response;


public class ShhVersion extends Response<String> {

    public String getVersion() {
        return getResult();
    }
}
