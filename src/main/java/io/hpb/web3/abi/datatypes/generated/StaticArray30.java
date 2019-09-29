package io.hpb.web3.abi.datatypes.generated;
import java.util.List;

import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;
public class StaticArray30<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray30(List<T> values) {
        super(30, values);
    }
    @Deprecated
    @SafeVarargs
    public StaticArray30(T... values) {
        super(30, values);
    }
    public StaticArray30(Class<T> type, List<T> values) {
        super(type, 30, values);
    }
    @SafeVarargs
    public StaticArray30(Class<T> type, T... values) {
        super(type, 30, values);
    }
}
