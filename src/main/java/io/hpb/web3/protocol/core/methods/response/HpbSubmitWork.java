package io.hpb.web3.protocol.core.methods.response;

import io.hpb.web3.protocol.core.Response;


public class HpbSubmitWork extends Response<Boolean> {

    public boolean solutionValid() {
        return getResult();
    }
}
