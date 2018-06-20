package com.hpb.web3.protocol.core.methods.response;

import com.hpb.web3.protocol.core.Response;


public class HpbCoinbase extends Response<String> {
    public String getAddress() {
        return getResult();
    }
}
