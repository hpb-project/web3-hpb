package com.hpb.web3.protocol.core.methods.response;

import com.hpb.web3.protocol.core.Response;

public class HpbSubscribe extends Response<String> {
    public String getSubscriptionId() {
        return getResult();
    }
}
