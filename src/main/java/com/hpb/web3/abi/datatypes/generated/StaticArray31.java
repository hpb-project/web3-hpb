package com.hpb.web3.abi.datatypes.generated;

import java.util.List;
import com.hpb.web3.abi.datatypes.StaticArray;
import com.hpb.web3.abi.datatypes.Type;


public class StaticArray31<T extends Type> extends StaticArray<T> {
    public StaticArray31(List<T> values) {
        super(31, values);
    }

    @SafeVarargs
    public StaticArray31(T... values) {
        super(31, values);
    }
}
