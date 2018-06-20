package com.hpb.web3.protocol.core.filters;


public interface Callback<T> {
    void onEvent(T value);
}
