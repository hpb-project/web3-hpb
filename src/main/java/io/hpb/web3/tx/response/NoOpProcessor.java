package io.hpb.web3.tx.response;

import java.io.IOException;

import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.protocol.exceptions.TransactionException;


public class NoOpProcessor extends TransactionReceiptProcessor {

    public NoOpProcessor(Web3 web3) {
        super(web3);
    }

    @Override
    public TransactionReceipt waitForTransactionReceipt(String transactionHash)
            throws IOException, TransactionException {
        return new EmptyTransactionReceipt(transactionHash);
    }
}
