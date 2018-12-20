package io.hpb.web3.abi.datatypes.generated;

import java.math.BigInteger;
import io.hpb.web3.abi.datatypes.Uint;


public class Uint24 extends Uint {
    public static final Uint24 DEFAULT = new Uint24(BigInteger.ZERO);

    public Uint24(BigInteger value) {
        super(24, value);
    }

    public Uint24(long value) {
        this(BigInteger.valueOf(value));
    }
}
