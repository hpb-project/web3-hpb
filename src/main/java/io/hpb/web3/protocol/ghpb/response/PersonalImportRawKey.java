package io.hpb.web3.protocol.ghpb.response;

import io.hpb.web3.protocol.core.Response;


public class PersonalImportRawKey extends Response<String> {
    public String getAccountId() {
        return getResult();
    }
}
