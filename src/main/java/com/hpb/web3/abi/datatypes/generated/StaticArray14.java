package com.hpb.web3.abi.datatypes.generated;

import java.util.List;
import com.hpb.web3.abi.datatypes.StaticArray;
import com.hpb.web3.abi.datatypes.Type;


public class StaticArray14<T extends Type> extends StaticArray<T> {
    public StaticArray14(List<T> values) {
        super(14, values);
    }

    @SafeVarargs
    public StaticArray14(T... values) {
        super(14, values);
    }
}
