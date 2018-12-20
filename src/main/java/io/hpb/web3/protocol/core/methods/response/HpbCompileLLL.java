package io.hpb.web3.protocol.core.methods.response;

import io.hpb.web3.protocol.core.Response;


public class HpbCompileLLL extends Response<String> {
    public String getCompiledSourceCode() {
        return getResult();
    }
}
