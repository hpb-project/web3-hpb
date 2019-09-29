package io.hpb.web3.abi.datatypes.generated;
import java.util.List;

import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;
public class StaticArray6<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray6(List<T> values) {
        super(6, values);
    }
    @Deprecated
    @SafeVarargs
    public StaticArray6(T... values) {
        super(6, values);
    }
    public StaticArray6(Class<T> type, List<T> values) {
        super(type, 6, values);
    }
    @SafeVarargs
    public StaticArray6(Class<T> type, T... values) {
        super(type, 6, values);
    }
}
