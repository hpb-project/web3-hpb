package io.hpb.web3.protocol.core.methods.response;

import java.math.BigInteger;

import io.hpb.web3.protocol.core.Response;
import io.hpb.web3.utils.Numeric;


public class HpbGasPrice extends Response<String> {
    public BigInteger getGasPrice() {
        return Numeric.decodeQuantity(getResult());
    }
}
