package io.hpb.web3.abi.datatypes.generated;

import java.util.List;
import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;


public class StaticArray22<T extends Type> extends StaticArray<T> {
    public StaticArray22(List<T> values) {
        super(22, values);
    }

    @SafeVarargs
    public StaticArray22(T... values) {
        super(22, values);
    }
}
