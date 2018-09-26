package com.hpb.web3.abi.datatypes.generated;

import java.util.List;
import com.hpb.web3.abi.datatypes.StaticArray;
import com.hpb.web3.abi.datatypes.Type;


public class StaticArray26<T extends Type> extends StaticArray<T> {
    public StaticArray26(List<T> values) {
        super(26, values);
    }

    @SafeVarargs
    public StaticArray26(T... values) {
        super(26, values);
    }
}
