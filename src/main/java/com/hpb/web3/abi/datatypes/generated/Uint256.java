package com.hpb.web3.abi.datatypes.generated;

import java.math.BigInteger;
import com.hpb.web3.abi.datatypes.Uint;


public class Uint256 extends Uint {
    public static final Uint256 DEFAULT = new Uint256(BigInteger.ZERO);

    public Uint256(BigInteger value) {
        super(256, value);
    }

    public Uint256(long value) {
        this(BigInteger.valueOf(value));
    }
}
