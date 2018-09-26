package com.hpb.web3.abi.datatypes.generated;

import java.util.List;
import com.hpb.web3.abi.datatypes.StaticArray;
import com.hpb.web3.abi.datatypes.Type;


public class StaticArray4<T extends Type> extends StaticArray<T> {
    public StaticArray4(List<T> values) {
        super(4, values);
    }

    @SafeVarargs
    public StaticArray4(T... values) {
        super(4, values);
    }
}
