package io.hpb.web3.contracts.token;
import java.math.BigInteger;
import java.util.List;

import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.RemoteCall;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.reactivex.Flowable;
@SuppressWarnings("unused")
public interface ERC20BasicInterface<T> {
    RemoteCall<BigInteger> totalSupply();
    RemoteCall<BigInteger> balanceOf(String who);
    RemoteCall<TransactionReceipt> transfer(String to, BigInteger value);
    List<T> getTransferEvents(TransactionReceipt transactionReceipt);
    Flowable<T> transferEventFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock);
}
