package io.hpb.web3.tx.response;

import java.io.IOException;
import java.util.Optional;

import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.methods.response.HpbGetTransactionReceipt;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.protocol.exceptions.TransactionException;


public abstract class TransactionReceiptProcessor {

    private final Web3 web3;

    public TransactionReceiptProcessor(Web3 web3) {
        this.web3 = web3;
    }

    public abstract TransactionReceipt waitForTransactionReceipt(
            String transactionHash)
            throws IOException, TransactionException;

    Optional<TransactionReceipt> sendTransactionReceiptRequest(
            String transactionHash) throws IOException, TransactionException {
        HpbGetTransactionReceipt transactionReceipt =
                web3.hpbGetTransactionReceipt(transactionHash).send();
        if (transactionReceipt.hasError()) {
            throw new TransactionException("Error processing request: "
                    + transactionReceipt.getError().getMessage());
        }

        return transactionReceipt.getTransactionReceipt();
    }
}
