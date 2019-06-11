package io.hpb.web3.abi.datatypes.generated;

import java.util.List;

import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;


public class StaticArray15<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray15(List<T> values) {
        super(15, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray15(T... values) {
        super(15, values);
    }

    public StaticArray15(Class<T> type, List<T> values) {
        super(type, 15, values);
    }

    @SafeVarargs
    public StaticArray15(Class<T> type, T... values) {
        super(type, 15, values);
    }
}
