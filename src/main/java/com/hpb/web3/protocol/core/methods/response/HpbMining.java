package com.hpb.web3.protocol.core.methods.response;

import com.hpb.web3.protocol.core.Response;


public class HpbMining extends Response<Boolean> {
    public boolean isMining() {
        return getResult();
    }
}
