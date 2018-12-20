package io.hpb.web3.tx.response;

import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;


public interface Callback {
    void accept(TransactionReceipt transactionReceipt);

    void exception(Exception exception);
}
