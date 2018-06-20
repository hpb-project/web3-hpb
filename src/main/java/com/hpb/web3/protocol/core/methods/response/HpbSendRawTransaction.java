package com.hpb.web3.protocol.core.methods.response;

import com.hpb.web3.protocol.core.Response;


public class HpbSendRawTransaction extends Response<String> {
    public String getTransactionHash() {
        return getResult();
    }
}
