package io.hpb.web3.abi.datatypes.generated;

import java.util.List;

import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;


public class StaticArray5<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray5(List<T> values) {
        super(5, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray5(T... values) {
        super(5, values);
    }

    public StaticArray5(Class<T> type, List<T> values) {
        super(type, 5, values);
    }

    @SafeVarargs
    public StaticArray5(Class<T> type, T... values) {
        super(type, 5, values);
    }
}
