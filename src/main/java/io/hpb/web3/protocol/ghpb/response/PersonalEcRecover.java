package io.hpb.web3.protocol.ghpb.response;

import io.hpb.web3.protocol.core.Response;


public class PersonalEcRecover extends Response<String> {
    public String getRecoverAccountId() {
        return getResult();
    }
}
