package com.hpb.web3.protocol.parity.methods.response;

import com.hpb.web3.protocol.core.Response;


public class ParityFullTraceResponse extends Response<FullTraceInfo> {
    public FullTraceInfo getFullTraceInfo() {
        return getResult();
    }
}
