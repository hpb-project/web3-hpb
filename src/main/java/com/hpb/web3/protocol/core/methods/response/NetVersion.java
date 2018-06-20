package com.hpb.web3.protocol.core.methods.response;

import com.hpb.web3.protocol.core.Response;


public class NetVersion extends Response<String> {
    public String getNetVersion() {
        return getResult();
    }
}
