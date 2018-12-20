package io.hpb.web3.protocol.core.methods.response;

import java.util.List;

import io.hpb.web3.protocol.core.Response;


public class HpbAccounts extends Response<List<String>> {
    public List<String> getAccounts() {
        return getResult();
    }
}
