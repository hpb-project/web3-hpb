package io.hpb.web3.abi.datatypes.generated;

import java.math.BigInteger;

import io.hpb.web3.abi.datatypes.Uint;


public class Uint160 extends Uint {
    public static final Uint160 DEFAULT = new Uint160(BigInteger.ZERO);

    public Uint160(BigInteger value) {
        super(160, value);
    }

    public Uint160(long value) {
        this(BigInteger.valueOf(value));
    }
}
