package com.hpb.web3.abi.datatypes.generated;

import java.util.List;
import com.hpb.web3.abi.datatypes.StaticArray;
import com.hpb.web3.abi.datatypes.Type;


public class StaticArray29<T extends Type> extends StaticArray<T> {
    public StaticArray29(List<T> values) {
        super(29, values);
    }

    @SafeVarargs
    public StaticArray29(T... values) {
        super(29, values);
    }
}
