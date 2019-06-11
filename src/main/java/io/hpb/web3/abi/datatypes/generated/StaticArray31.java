package io.hpb.web3.abi.datatypes.generated;

import java.util.List;

import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;


public class StaticArray31<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray31(List<T> values) {
        super(31, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray31(T... values) {
        super(31, values);
    }

    public StaticArray31(Class<T> type, List<T> values) {
        super(type, 31, values);
    }

    @SafeVarargs
    public StaticArray31(Class<T> type, T... values) {
        super(type, 31, values);
    }
}
