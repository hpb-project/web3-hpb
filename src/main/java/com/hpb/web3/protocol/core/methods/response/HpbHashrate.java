package com.hpb.web3.protocol.core.methods.response;

import java.math.BigInteger;

import com.hpb.web3.protocol.core.Response;
import com.hpb.web3.utils.Numeric;


public class HpbHashrate extends Response<String> {
    public BigInteger getHashrate() {
        return Numeric.decodeQuantity(getResult());
    }
}
