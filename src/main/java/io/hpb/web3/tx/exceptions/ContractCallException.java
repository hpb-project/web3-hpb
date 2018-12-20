package io.hpb.web3.tx.exceptions;


public class ContractCallException extends RuntimeException {

    public ContractCallException(String message) {
        super(message);
    }

    public ContractCallException(String message, Throwable cause) {
        super(message, cause);
    }
}
