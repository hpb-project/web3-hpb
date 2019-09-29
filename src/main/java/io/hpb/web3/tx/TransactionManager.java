package io.hpb.web3.tx;
import static io.hpb.web3.protocol.core.JsonRpc2_0Web3.DEFAULT_BLOCK_TIME;

import java.io.IOException;
import java.math.BigInteger;

import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.methods.response.HpbSendTransaction;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.protocol.exceptions.TransactionException;
import io.hpb.web3.tx.response.PollingTransactionReceiptProcessor;
import io.hpb.web3.tx.response.TransactionReceiptProcessor;
public abstract class TransactionManager {
    public static final int DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH = 40;
    public static final long DEFAULT_POLLING_FREQUENCY = DEFAULT_BLOCK_TIME;
    private final TransactionReceiptProcessor transactionReceiptProcessor;
    private final String fromAddress;
    protected TransactionManager(
            TransactionReceiptProcessor transactionReceiptProcessor, String fromAddress) {
        this.transactionReceiptProcessor = transactionReceiptProcessor;
        this.fromAddress = fromAddress;
    }
    protected TransactionManager(Web3 web3, String fromAddress) {
        this(
                new PollingTransactionReceiptProcessor(
                        web3, DEFAULT_POLLING_FREQUENCY, DEFAULT_POLLING_ATTEMPTS_PER_TX_HASH),
                fromAddress);
    }
    protected TransactionManager(
            Web3 web3, int attempts, long sleepDuration, String fromAddress) {
        this(new PollingTransactionReceiptProcessor(web3, sleepDuration, attempts), fromAddress);
    }
    protected TransactionReceipt executeTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value)
            throws IOException, TransactionException {
        return executeTransaction(gasPrice, gasLimit, to, data, value, false);
    }
    protected TransactionReceipt executeTransaction(
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger value,
            boolean constructor)
            throws IOException, TransactionException {
        HpbSendTransaction hpbSendTransaction =
                sendTransaction(gasPrice, gasLimit, to, data, value, constructor);
        return processResponse(hpbSendTransaction);
    }
    public HpbSendTransaction sendTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value)
            throws IOException {
        return sendTransaction(gasPrice, gasLimit, to, data, value, false);
    }
    public abstract HpbSendTransaction sendTransaction(
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger value,
            boolean constructor)
            throws IOException;
    public abstract String sendCall(
            String to, String data, DefaultBlockParameter defaultBlockParameter) throws IOException;
    public String getFromAddress() {
        return fromAddress;
    }
    private TransactionReceipt processResponse(HpbSendTransaction transactionResponse)
            throws IOException, TransactionException {
        if (transactionResponse.hasError()) {
            throw new RuntimeException(
                    "Error processing transaction request: "
                            + transactionResponse.getError().getMessage());
        }
        String transactionHash = transactionResponse.getTransactionHash();
        return transactionReceiptProcessor.waitForTransactionReceipt(transactionHash);
    }
}
