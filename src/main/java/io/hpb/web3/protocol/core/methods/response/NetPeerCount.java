package io.hpb.web3.protocol.core.methods.response;

import java.math.BigInteger;

import io.hpb.web3.protocol.core.Response;
import io.hpb.web3.utils.Numeric;


public class NetPeerCount extends Response<String> {

    public BigInteger getQuantity() {
        return Numeric.decodeQuantity(getResult());
    }
}
