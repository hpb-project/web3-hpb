package io.hpb.web3.abi.datatypes.generated;

import java.util.List;

import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;


public class StaticArray26<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray26(List<T> values) {
        super(26, values);
    }

    @Deprecated
    @SafeVarargs
    public StaticArray26(T... values) {
        super(26, values);
    }

    public StaticArray26(Class<T> type, List<T> values) {
        super(type, 26, values);
    }

    @SafeVarargs
    public StaticArray26(Class<T> type, T... values) {
        super(type, 26, values);
    }
}
