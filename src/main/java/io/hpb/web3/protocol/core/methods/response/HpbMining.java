package io.hpb.web3.protocol.core.methods.response;

import io.hpb.web3.protocol.core.Response;


public class HpbMining extends Response<Boolean> {
    public boolean isMining() {
        return getResult();
    }
}
