package io.hpb.web3.abi.datatypes.generated;

import java.util.List;

import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;


public class StaticArray24<T extends Type> extends StaticArray<T> {
    public StaticArray24(List<T> values) {
        super(24, values);
    }

    @SafeVarargs
    public StaticArray24(T... values) {
        super(24, values);
    }
}
