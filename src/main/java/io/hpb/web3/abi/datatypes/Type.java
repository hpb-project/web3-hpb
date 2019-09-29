package io.hpb.web3.abi.datatypes;
public interface Type<T> {
    int MAX_BIT_LENGTH = 256;
    int MAX_BYTE_LENGTH = MAX_BIT_LENGTH / 8;
    T getValue();
    String getTypeAsString();
}
