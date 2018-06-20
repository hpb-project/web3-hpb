package com.hpb.web3.protocol.admin.methods.response;

import com.hpb.web3.protocol.core.Response;


public class NewAccountIdentifier extends Response<String> {
    public String getAccountId() {
        return getResult();
    }    
}
