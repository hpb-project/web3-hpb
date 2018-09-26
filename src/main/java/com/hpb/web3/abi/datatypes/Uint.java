package com.hpb.web3.abi.datatypes;

import java.math.BigInteger;


public class Uint extends IntType {

    public static final String TYPE_NAME = "uint";
    public static final Uint DEFAULT = new Uint(BigInteger.ZERO);

    
    Uint(String typePrefix, int bitSize, BigInteger value) {
        super(typePrefix, bitSize, value);
    }

    protected Uint(int bitSize, BigInteger value) {
        this(TYPE_NAME, bitSize, value);
    }

    public Uint(BigInteger value) {
        // "int" values should be declared as int256 in computing function selectors
        this(MAX_BIT_LENGTH, value);
    }

    @Override
    boolean valid(int bitSize, BigInteger value) {
        return super.valid(bitSize, value)
                && value.signum() != -1;
    }
}
