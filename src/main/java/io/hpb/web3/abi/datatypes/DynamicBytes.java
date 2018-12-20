package io.hpb.web3.abi.datatypes;


public class DynamicBytes extends BytesType {

    public static final String TYPE_NAME = "bytes";
    public static final DynamicBytes DEFAULT = new DynamicBytes(new byte[]{});

    public DynamicBytes(byte[] value) {
        super(value, TYPE_NAME);
    }
}
