package io.hpb.web3.abi.datatypes.generated;

import java.math.BigInteger;

import io.hpb.web3.abi.datatypes.Uint;


public class Uint208 extends Uint {
    public static final Uint208 DEFAULT = new Uint208(BigInteger.ZERO);

    public Uint208(BigInteger value) {
        super(208, value);
    }

    public Uint208(long value) {
        this(BigInteger.valueOf(value));
    }
}
