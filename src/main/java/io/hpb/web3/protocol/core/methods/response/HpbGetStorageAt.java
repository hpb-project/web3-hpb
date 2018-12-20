package io.hpb.web3.protocol.core.methods.response;

import io.hpb.web3.protocol.core.Response;


public class HpbGetStorageAt extends Response<String> {
    public String getData() {
        return getResult();
    }
}
