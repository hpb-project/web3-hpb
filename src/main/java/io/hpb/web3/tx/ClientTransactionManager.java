package io.hpb.web3.tx;

import java.io.IOException;
import java.math.BigInteger;

import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.methods.request.Transaction;
import io.hpb.web3.protocol.core.methods.response.HpbSendTransaction;
import io.hpb.web3.tx.response.TransactionReceiptProcessor;


public class ClientTransactionManager extends TransactionManager {

    private final Web3 web3;

    public ClientTransactionManager(
            Web3 web3, String fromAddress) {
        super(web3, fromAddress);
        this.web3 = web3;
    }

    public ClientTransactionManager(
            Web3 web3, String fromAddress, int attempts, int sleepDuration) {
        super(web3, attempts, sleepDuration, fromAddress);
        this.web3 = web3;
    }

    public ClientTransactionManager(
            Web3 web3, String fromAddress,
            TransactionReceiptProcessor transactionReceiptProcessor) {
        super(transactionReceiptProcessor, fromAddress);
        this.web3 = web3;
    }

    @Override
    public HpbSendTransaction sendTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to,
            String data, BigInteger value)
            throws IOException {

        Transaction transaction = new Transaction(
                getFromAddress(), null, gasPrice, gasLimit, to, value, data);

        return web3.hpbSendTransaction(transaction)
                .send();
    }
}
