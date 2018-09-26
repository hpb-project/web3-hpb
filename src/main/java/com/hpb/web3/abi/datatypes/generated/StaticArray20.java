package com.hpb.web3.abi.datatypes.generated;

import java.util.List;
import com.hpb.web3.abi.datatypes.StaticArray;
import com.hpb.web3.abi.datatypes.Type;


public class StaticArray20<T extends Type> extends StaticArray<T> {
    public StaticArray20(List<T> values) {
        super(20, values);
    }

    @SafeVarargs
    public StaticArray20(T... values) {
        super(20, values);
    }
}
