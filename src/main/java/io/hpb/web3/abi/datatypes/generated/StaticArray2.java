package io.hpb.web3.abi.datatypes.generated;

import java.util.List;
import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;


public class StaticArray2<T extends Type> extends StaticArray<T> {
    public StaticArray2(List<T> values) {
        super(2, values);
    }

    @SafeVarargs
    public StaticArray2(T... values) {
        super(2, values);
    }
}
