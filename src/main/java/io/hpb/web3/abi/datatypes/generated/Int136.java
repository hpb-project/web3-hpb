package io.hpb.web3.abi.datatypes.generated;

import java.math.BigInteger;

import io.hpb.web3.abi.datatypes.Int;


public class Int136 extends Int {
    public static final Int136 DEFAULT = new Int136(BigInteger.ZERO);

    public Int136(BigInteger value) {
        super(136, value);
    }

    public Int136(long value) {
        this(BigInteger.valueOf(value));
    }
}
