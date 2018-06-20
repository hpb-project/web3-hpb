package com.hpb.web3.protocol.ghpb.response;

import com.hpb.web3.protocol.core.Response;


public class PersonalImportRawKey extends Response<String> {
    public String getAccountId() {
        return getResult();
    }
}
