package com.hpb.web3.abi.datatypes.generated;

import java.math.BigInteger;
import com.hpb.web3.abi.datatypes.Int;


public class Int160 extends Int {
    public static final Int160 DEFAULT = new Int160(BigInteger.ZERO);

    public Int160(BigInteger value) {
        super(160, value);
    }

    public Int160(long value) {
        this(BigInteger.valueOf(value));
    }
}
