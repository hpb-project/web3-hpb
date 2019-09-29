package io.hpb.web3.abi.datatypes;
import java.math.BigInteger;
public class Uint extends IntType {
    public static final String TYPE_NAME = "uint";
    public static final Uint DEFAULT = new Uint(BigInteger.ZERO);
    protected Uint(int bitSize, BigInteger value) {
        super(TYPE_NAME, bitSize, value);
    }
    public Uint(BigInteger value) {
        this(MAX_BIT_LENGTH, value);
    }
    @Override
    protected boolean valid() {
        return super.valid() && 0 <= value.signum();
    }
}
