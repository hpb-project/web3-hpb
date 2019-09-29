package io.hpb.web3.protocol.exceptions;
import java.util.Optional;
public class TransactionException extends Exception {
    private Optional<String> transactionHash = Optional.empty();
    public TransactionException(String message) {
        super(message);
    }
    public TransactionException(String message, String transactionHash) {
        super(message);
        this.transactionHash = Optional.ofNullable(transactionHash);
    }
    public TransactionException(Throwable cause) {
        super(cause);
    }
    public Optional<String> getTransactionHash() {
        return transactionHash;
    }
}
