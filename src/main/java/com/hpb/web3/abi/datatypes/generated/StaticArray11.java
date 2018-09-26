package com.hpb.web3.abi.datatypes.generated;

import java.util.List;
import com.hpb.web3.abi.datatypes.StaticArray;
import com.hpb.web3.abi.datatypes.Type;


public class StaticArray11<T extends Type> extends StaticArray<T> {
    public StaticArray11(List<T> values) {
        super(11, values);
    }

    @SafeVarargs
    public StaticArray11(T... values) {
        super(11, values);
    }
}
