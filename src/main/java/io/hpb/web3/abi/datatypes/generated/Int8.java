package io.hpb.web3.abi.datatypes.generated;

import java.math.BigInteger;
import io.hpb.web3.abi.datatypes.Int;


public class Int8 extends Int {
    public static final Int8 DEFAULT = new Int8(BigInteger.ZERO);

    public Int8(BigInteger value) {
        super(8, value);
    }

    public Int8(long value) {
        this(BigInteger.valueOf(value));
    }
}
