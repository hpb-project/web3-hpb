package io.hpb.web3.abi.datatypes.generated;

import java.util.List;

import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;


public class StaticArray12<T extends Type> extends StaticArray<T> {
    public StaticArray12(List<T> values) {
        super(12, values);
    }

    @SafeVarargs
    public StaticArray12(T... values) {
        super(12, values);
    }
}
