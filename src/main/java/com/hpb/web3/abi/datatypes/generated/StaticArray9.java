package com.hpb.web3.abi.datatypes.generated;

import java.util.List;
import com.hpb.web3.abi.datatypes.StaticArray;
import com.hpb.web3.abi.datatypes.Type;


public class StaticArray9<T extends Type> extends StaticArray<T> {
    public StaticArray9(List<T> values) {
        super(9, values);
    }

    @SafeVarargs
    public StaticArray9(T... values) {
        super(9, values);
    }
}
