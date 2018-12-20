package io.hpb.web3.abi;

import java.util.List;

import io.hpb.web3.abi.datatypes.Type;


public class EventValues {
    private final List<Type> indexedValues;
    private final List<Type> nonIndexedValues;

    public EventValues(List<Type> indexedValues, List<Type> nonIndexedValues) {
        this.indexedValues = indexedValues;
        this.nonIndexedValues = nonIndexedValues;
    }

    public List<Type> getIndexedValues() {
        return indexedValues;
    }

    public List<Type> getNonIndexedValues() {
        return nonIndexedValues;
    }
}
