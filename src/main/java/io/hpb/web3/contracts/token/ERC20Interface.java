package io.hpb.web3.contracts.token;

import java.math.BigInteger;
import java.util.List;

import rx.Observable;

import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.RemoteCall;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;


@SuppressWarnings("unused")
public interface ERC20Interface<R, T> extends ERC20BasicInterface<T> {

    RemoteCall<BigInteger> allowance(String owner, String spender);

    RemoteCall<TransactionReceipt> approve(String spender, BigInteger value);

    RemoteCall<TransactionReceipt> transferFrom(String from, String to, BigInteger value);

    List<R> getApprovalEvents(TransactionReceipt transactionReceipt);

    Observable<R> approvalEventObservable(DefaultBlockParameter startBlock,
            DefaultBlockParameter endBlock);

}
