package io.hpb.web3.protocol.exceptions;


public class TransactionException extends Exception {
    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }
}
