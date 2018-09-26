package com.hpb.web3.abi.datatypes.generated;

import java.math.BigInteger;
import com.hpb.web3.abi.datatypes.Uint;


public class Uint184 extends Uint {
    public static final Uint184 DEFAULT = new Uint184(BigInteger.ZERO);

    public Uint184(BigInteger value) {
        super(184, value);
    }

    public Uint184(long value) {
        this(BigInteger.valueOf(value));
    }
}
