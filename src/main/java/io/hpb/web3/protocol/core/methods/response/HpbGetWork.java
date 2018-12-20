package io.hpb.web3.protocol.core.methods.response;

import java.util.List;

import io.hpb.web3.protocol.core.Response;


public class HpbGetWork extends Response<List<String>> {

    public String getCurrentBlockHeaderPowHash() {
        return getResult().get(0);
    }

    public String getSeedHashForDag() {
        return getResult().get(1);
    }

    public String getBoundaryCondition() {
        return getResult().get(2);
    }
}
