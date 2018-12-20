package io.hpb.web3.protocol.core.methods.response;

import io.hpb.web3.protocol.core.Response;


public class HpbSendTransaction extends Response<String> {
    public String getTransactionHash() {
        return getResult();
    }
}
