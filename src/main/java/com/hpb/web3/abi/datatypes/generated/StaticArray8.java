package com.hpb.web3.abi.datatypes.generated;

import java.util.List;
import com.hpb.web3.abi.datatypes.StaticArray;
import com.hpb.web3.abi.datatypes.Type;


public class StaticArray8<T extends Type> extends StaticArray<T> {
    public StaticArray8(List<T> values) {
        super(8, values);
    }

    @SafeVarargs
    public StaticArray8(T... values) {
        super(8, values);
    }
}
