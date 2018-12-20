package io.hpb.web3.abi.datatypes.generated;

import java.math.BigInteger;
import io.hpb.web3.abi.datatypes.Int;


public class Int96 extends Int {
    public static final Int96 DEFAULT = new Int96(BigInteger.ZERO);

    public Int96(BigInteger value) {
        super(96, value);
    }

    public Int96(long value) {
        this(BigInteger.valueOf(value));
    }
}
