package io.hpb.web3.protocol.admin.methods.response;

import java.util.List;

import io.hpb.web3.protocol.core.Response;


public class PersonalListAccounts extends Response<List<String>> {
    public List<String> getAccountIds() {
        return getResult();
    }
}
