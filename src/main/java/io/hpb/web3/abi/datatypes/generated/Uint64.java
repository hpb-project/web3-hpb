package io.hpb.web3.abi.datatypes.generated;

import java.math.BigInteger;
import io.hpb.web3.abi.datatypes.Uint;


public class Uint64 extends Uint {
    public static final Uint64 DEFAULT = new Uint64(BigInteger.ZERO);

    public Uint64(BigInteger value) {
        super(64, value);
    }

    public Uint64(long value) {
        this(BigInteger.valueOf(value));
    }
}
