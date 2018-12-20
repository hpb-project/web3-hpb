package io.hpb.web3.abi.datatypes.generated;

import java.util.List;
import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;


public class StaticArray21<T extends Type> extends StaticArray<T> {
    public StaticArray21(List<T> values) {
        super(21, values);
    }

    @SafeVarargs
    public StaticArray21(T... values) {
        super(21, values);
    }
}
