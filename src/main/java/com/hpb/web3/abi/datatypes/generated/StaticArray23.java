package com.hpb.web3.abi.datatypes.generated;

import java.util.List;
import com.hpb.web3.abi.datatypes.StaticArray;
import com.hpb.web3.abi.datatypes.Type;


public class StaticArray23<T extends Type> extends StaticArray<T> {
    public StaticArray23(List<T> values) {
        super(23, values);
    }

    @SafeVarargs
    public StaticArray23(T... values) {
        super(23, values);
    }
}
