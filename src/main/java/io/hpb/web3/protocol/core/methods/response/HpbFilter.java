package io.hpb.web3.protocol.core.methods.response;

import java.math.BigInteger;

import io.hpb.web3.protocol.core.Response;
import io.hpb.web3.utils.Numeric;


public class HpbFilter extends Response<String> {
    public BigInteger getFilterId() {
        return Numeric.decodeQuantity(getResult());
    }
}
