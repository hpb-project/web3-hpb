package io.hpb.web3.abi.datatypes.generated;

import java.util.List;
import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;


public class StaticArray27<T extends Type> extends StaticArray<T> {
    public StaticArray27(List<T> values) {
        super(27, values);
    }

    @SafeVarargs
    public StaticArray27(T... values) {
        super(27, values);
    }
}
