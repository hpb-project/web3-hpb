package com.hpb.web3.abi.datatypes.generated;

import java.math.BigInteger;
import com.hpb.web3.abi.datatypes.Uint;


public class Uint80 extends Uint {
    public static final Uint80 DEFAULT = new Uint80(BigInteger.ZERO);

    public Uint80(BigInteger value) {
        super(80, value);
    }

    public Uint80(long value) {
        this(BigInteger.valueOf(value));
    }
}
