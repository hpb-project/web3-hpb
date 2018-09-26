package com.hpb.web3.abi.datatypes.generated;

import java.util.List;
import com.hpb.web3.abi.datatypes.StaticArray;
import com.hpb.web3.abi.datatypes.Type;


public class StaticArray25<T extends Type> extends StaticArray<T> {
    public StaticArray25(List<T> values) {
        super(25, values);
    }

    @SafeVarargs
    public StaticArray25(T... values) {
        super(25, values);
    }
}
