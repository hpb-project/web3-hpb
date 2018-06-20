package com.hpb.web3.protocol.core.methods.response;

import com.hpb.web3.protocol.core.Response;


public class HpbSendTransaction extends Response<String> {
    public String getTransactionHash() {
        return getResult();
    }
}
