package io.hpb.web3.abi.datatypes.generated;

import java.util.List;
import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;


public class StaticArray3<T extends Type> extends StaticArray<T> {
    public StaticArray3(List<T> values) {
        super(3, values);
    }

    @SafeVarargs
    public StaticArray3(T... values) {
        super(3, values);
    }
}
