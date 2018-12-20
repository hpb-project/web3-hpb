package io.hpb.web3.tx.response;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.protocol.exceptions.TransactionException;
import io.hpb.web3.utils.Async;


public class QueuingTransactionReceiptProcessor extends TransactionReceiptProcessor {

    private final int pollingAttemptsPerTxHash;

    private final ScheduledExecutorService scheduledExecutorService;
    private final Callback callback;
    private final BlockingQueue<RequestWrapper> pendingTransactions;

    public QueuingTransactionReceiptProcessor(
            Web3 web3, Callback callback,
            int pollingAttemptsPerTxHash, long pollingFrequency) {
        super(web3);
        this.scheduledExecutorService = Async.defaultExecutorService();
        this.callback = callback;
        this.pendingTransactions = new LinkedBlockingQueue<>();
        this.pollingAttemptsPerTxHash = pollingAttemptsPerTxHash;

        scheduledExecutorService.scheduleAtFixedRate(
                this::sendTransactionReceiptRequests,
                pollingFrequency, pollingFrequency, TimeUnit.MILLISECONDS);
    }

    @Override
    public TransactionReceipt waitForTransactionReceipt(String transactionHash)
            throws IOException, TransactionException {
        pendingTransactions.add(new RequestWrapper(transactionHash));

        return new EmptyTransactionReceipt(transactionHash);
    }

    private void sendTransactionReceiptRequests() {
        for (RequestWrapper requestWrapper : pendingTransactions) {
            try {
                String transactionHash = requestWrapper.getTransactionHash();
                Optional<TransactionReceipt> transactionReceipt =
                        sendTransactionReceiptRequest(transactionHash);
                if (transactionReceipt.isPresent()) {
                    callback.accept(transactionReceipt.get());
                    pendingTransactions.remove(requestWrapper);
                } else {
                    if (requestWrapper.getCount() == pollingAttemptsPerTxHash) {
                        throw new TransactionException(
                                "No transaction receipt for txHash: " + transactionHash
                                        + "received after " + pollingAttemptsPerTxHash
                                        + " attempts");
                    } else {
                        requestWrapper.incrementCount();
                    }
                }
            } catch (IOException | TransactionException e) {
                pendingTransactions.remove(requestWrapper);
                callback.exception(e);
            }
        }
    }

    
    private static class RequestWrapper {
        private final String transactionHash;
        private int count;

        RequestWrapper(String transactionHash) {
            this.transactionHash = transactionHash;
            this.count = 0;
        }

        String getTransactionHash() {
            return transactionHash;
        }

        int getCount() {
            return count;
        }

        void incrementCount() {
            this.count += 1;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            RequestWrapper that = (RequestWrapper) o;

            return transactionHash.equals(that.transactionHash);
        }

        @Override
        public int hashCode() {
            return transactionHash.hashCode();
        }
    }
}
