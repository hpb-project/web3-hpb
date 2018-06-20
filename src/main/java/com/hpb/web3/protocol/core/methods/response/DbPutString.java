package com.hpb.web3.protocol.core.methods.response;

import com.hpb.web3.protocol.core.Response;


public class DbPutString extends Response<Boolean> {

    public boolean valueStored() {
        return getResult();
    }
}
