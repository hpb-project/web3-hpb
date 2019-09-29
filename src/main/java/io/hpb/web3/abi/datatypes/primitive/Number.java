package io.hpb.web3.abi.datatypes.primitive;
import io.hpb.web3.abi.datatypes.NumericType;
public abstract class Number<T extends java.lang.Number & Comparable<T>> extends PrimitiveType<T> {
    Number(T value) {
        super(value);
    }
    @Override
    public abstract NumericType toSolidityType();
}
