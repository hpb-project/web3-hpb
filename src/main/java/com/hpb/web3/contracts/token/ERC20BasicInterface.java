package com.hpb.web3.contracts.token;

import java.math.BigInteger;
import java.util.List;

import rx.Observable;

import com.hpb.web3.protocol.core.DefaultBlockParameter;
import com.hpb.web3.protocol.core.RemoteCall;
import com.hpb.web3.protocol.core.methods.response.TransactionReceipt;


@SuppressWarnings("unused")
public interface ERC20BasicInterface<T> {

    RemoteCall<BigInteger> totalSupply();

    RemoteCall<BigInteger> balanceOf(String who);

    RemoteCall<TransactionReceipt> transfer(String to, BigInteger value);
    
    List<T> getTransferEvents(TransactionReceipt transactionReceipt);

    Observable<T> transferEventObservable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock);

}
