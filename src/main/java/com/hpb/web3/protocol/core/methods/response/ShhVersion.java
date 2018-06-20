package com.hpb.web3.protocol.core.methods.response;

import com.hpb.web3.protocol.core.Response;


public class ShhVersion extends Response<String> {

    public String getVersion() {
        return getResult();
    }
}
