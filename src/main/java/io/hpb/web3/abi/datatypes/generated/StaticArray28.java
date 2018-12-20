package io.hpb.web3.abi.datatypes.generated;

import java.util.List;
import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;


public class StaticArray28<T extends Type> extends StaticArray<T> {
    public StaticArray28(List<T> values) {
        super(28, values);
    }

    @SafeVarargs
    public StaticArray28(T... values) {
        super(28, values);
    }
}
