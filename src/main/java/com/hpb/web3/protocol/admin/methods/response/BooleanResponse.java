package com.hpb.web3.protocol.admin.methods.response;

import com.hpb.web3.protocol.core.Response;


public class BooleanResponse extends Response<Boolean> {
    public boolean success() {
        return getResult();
    }
}
