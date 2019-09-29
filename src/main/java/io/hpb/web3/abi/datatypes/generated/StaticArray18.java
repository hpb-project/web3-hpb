package io.hpb.web3.abi.datatypes.generated;
import java.util.List;

import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;
public class StaticArray18<T extends Type> extends StaticArray<T> {
    @Deprecated
    public StaticArray18(List<T> values) {
        super(18, values);
    }
    @Deprecated
    @SafeVarargs
    public StaticArray18(T... values) {
        super(18, values);
    }
    public StaticArray18(Class<T> type, List<T> values) {
        super(type, 18, values);
    }
    @SafeVarargs
    public StaticArray18(Class<T> type, T... values) {
        super(type, 18, values);
    }
}
