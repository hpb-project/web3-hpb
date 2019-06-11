package io.hpb.web3.abi.datatypes;

import java.math.BigInteger;


public class Int extends IntType {

    public static final String TYPE_NAME = "int";
    public static final Int DEFAULT = new Int(BigInteger.ZERO);

    public Int(BigInteger value) {
        
        this(MAX_BIT_LENGTH, value);
    }

    protected Int(int bitSize, BigInteger value) {
        super(TYPE_NAME, bitSize, value);
    }
}
