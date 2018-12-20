package io.hpb.web3.protocol.core.methods.response;

import io.hpb.web3.protocol.core.Response;


public class DbPutHex extends Response<Boolean> {

    public boolean valueStored() {
        return getResult();
    }
}
