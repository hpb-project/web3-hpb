package com.hpb.web3.protocol.parity.methods.response;

import java.util.ArrayList;

import com.hpb.web3.protocol.core.Response;


public class ParityAddressesResponse extends Response<ArrayList<String>> {
    public ArrayList<String> getAddresses() {
        return getResult();
    }
}
