package io.hpb.web3.abi.datatypes.generated;

import java.math.BigInteger;
import io.hpb.web3.abi.datatypes.Uint;


public class Uint168 extends Uint {
    public static final Uint168 DEFAULT = new Uint168(BigInteger.ZERO);

    public Uint168(BigInteger value) {
        super(168, value);
    }

    public Uint168(long value) {
        this(BigInteger.valueOf(value));
    }
}
