package com.hpb.web3.abi.datatypes.generated;

import java.util.List;
import com.hpb.web3.abi.datatypes.StaticArray;
import com.hpb.web3.abi.datatypes.Type;


public class StaticArray16<T extends Type> extends StaticArray<T> {
    public StaticArray16(List<T> values) {
        super(16, values);
    }

    @SafeVarargs
    public StaticArray16(T... values) {
        super(16, values);
    }
}
