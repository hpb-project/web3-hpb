package com.hpb.web3.protocol.parity.methods.response;

import java.util.ArrayList;

import com.hpb.web3.protocol.core.Response;


public class ParityListRecentDapps extends Response<ArrayList<String>> {
    public ArrayList<String> getDappsIds() {
        return getResult();
    }
}
