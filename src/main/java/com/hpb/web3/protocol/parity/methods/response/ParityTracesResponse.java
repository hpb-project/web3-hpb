package com.hpb.web3.protocol.parity.methods.response;

import java.util.List;

import com.hpb.web3.protocol.core.Response;


public class ParityTracesResponse extends Response<List<Trace>> {
    public List<Trace> getTraces() {
        return getResult();
    }
}
