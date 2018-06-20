package com.hpb.web3.protocol.parity.methods.response;

import com.hpb.web3.protocol.core.Response;


public class ParityDefaultAddressResponse extends Response<String> {
    public String getAddress() {
        return getResult();
    }
}
