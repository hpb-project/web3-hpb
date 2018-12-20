package io.hpb.web3.abi.datatypes.generated;

import java.util.List;
import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;


public class StaticArray13<T extends Type> extends StaticArray<T> {
    public StaticArray13(List<T> values) {
        super(13, values);
    }

    @SafeVarargs
    public StaticArray13(T... values) {
        super(13, values);
    }
}
