package com.hpb.web3.abi.datatypes.generated;

import java.util.List;
import com.hpb.web3.abi.datatypes.StaticArray;
import com.hpb.web3.abi.datatypes.Type;


public class StaticArray10<T extends Type> extends StaticArray<T> {
    public StaticArray10(List<T> values) {
        super(10, values);
    }

    @SafeVarargs
    public StaticArray10(T... values) {
        super(10, values);
    }
}
