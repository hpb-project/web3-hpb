package com.hpb.web3.protocol.admin.methods.response;

import com.hpb.web3.protocol.core.Response;


public class PersonalUnlockAccount extends Response<Boolean> {
    public Boolean accountUnlocked() {
        return getResult();
    }
}