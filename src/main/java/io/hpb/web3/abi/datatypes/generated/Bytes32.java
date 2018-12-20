package io.hpb.web3.abi.datatypes.generated;

import io.hpb.web3.abi.datatypes.Bytes;


public class Bytes32 extends Bytes {
    public static final Bytes32 DEFAULT = new Bytes32(new byte[32]);

    public Bytes32(byte[] value) {
        super(32, value);
    }
}
