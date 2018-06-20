package com.hpb.web3.protocol.admin.methods.response;

import com.hpb.web3.protocol.core.Response;


public class PersonalSign extends Response<String> {
    public String getSignedMessage() {
        return getResult();
    }
}
