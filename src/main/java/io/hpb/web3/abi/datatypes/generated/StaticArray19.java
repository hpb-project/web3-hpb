package io.hpb.web3.abi.datatypes.generated;

import java.util.List;
import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;


public class StaticArray19<T extends Type> extends StaticArray<T> {
    public StaticArray19(List<T> values) {
        super(19, values);
    }

    @SafeVarargs
    public StaticArray19(T... values) {
        super(19, values);
    }
}
