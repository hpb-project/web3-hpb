package io.hpb.web3.abi.datatypes.generated;

import java.util.List;
import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;


public class StaticArray15<T extends Type> extends StaticArray<T> {
    public StaticArray15(List<T> values) {
        super(15, values);
    }

    @SafeVarargs
    public StaticArray15(T... values) {
        super(15, values);
    }
}
