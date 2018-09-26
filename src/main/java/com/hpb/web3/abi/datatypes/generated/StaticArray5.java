package com.hpb.web3.abi.datatypes.generated;

import java.util.List;
import com.hpb.web3.abi.datatypes.StaticArray;
import com.hpb.web3.abi.datatypes.Type;


public class StaticArray5<T extends Type> extends StaticArray<T> {
    public StaticArray5(List<T> values) {
        super(5, values);
    }

    @SafeVarargs
    public StaticArray5(T... values) {
        super(5, values);
    }
}
