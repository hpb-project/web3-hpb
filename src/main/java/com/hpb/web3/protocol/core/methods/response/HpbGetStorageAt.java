package com.hpb.web3.protocol.core.methods.response;

import com.hpb.web3.protocol.core.Response;


public class HpbGetStorageAt extends Response<String> {
    public String getData() {
        return getResult();
    }
}
