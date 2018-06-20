package com.hpb.web3.protocol.parity.methods.response;

import com.hpb.web3.protocol.core.Response;


public class ParityTraceGet extends Response<Trace> {
    public Trace getTrace() {
        return getResult();
    }
}
