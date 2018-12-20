package io.hpb.web3.protocol.admin.methods.response;

import io.hpb.web3.protocol.core.Response;


public class NewAccountIdentifier extends Response<String> {
    public String getAccountId() {
        return getResult();
    }    
}
