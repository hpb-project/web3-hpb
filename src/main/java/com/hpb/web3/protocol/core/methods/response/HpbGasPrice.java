package com.hpb.web3.protocol.core.methods.response;

import java.math.BigInteger;

import com.hpb.web3.protocol.core.Response;
import com.hpb.web3.utils.Numeric;


public class HpbGasPrice extends Response<String> {
    public BigInteger getGasPrice() {
        return Numeric.decodeQuantity(getResult());
    }
}
