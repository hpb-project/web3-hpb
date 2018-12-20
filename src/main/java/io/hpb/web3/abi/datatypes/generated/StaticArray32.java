package io.hpb.web3.abi.datatypes.generated;

import java.util.List;
import io.hpb.web3.abi.datatypes.StaticArray;
import io.hpb.web3.abi.datatypes.Type;


public class StaticArray32<T extends Type> extends StaticArray<T> {
    public StaticArray32(List<T> values) {
        super(32, values);
    }

    @SafeVarargs
    public StaticArray32(T... values) {
        super(32, values);
    }
}
