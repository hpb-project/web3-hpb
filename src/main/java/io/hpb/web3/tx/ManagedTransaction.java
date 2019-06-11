package io.hpb.web3.tx;

import java.io.IOException;
import java.math.BigInteger;

import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.methods.response.HpbGasPrice;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.protocol.exceptions.TransactionException;



public abstract class ManagedTransaction {

    
    public static final BigInteger GAS_PRICE = BigInteger.valueOf(22_000_000_000L);

    protected Web3 web3;

    protected TransactionManager transactionManager;


    protected ManagedTransaction(Web3 web3, TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.web3 = web3;
    }

    public BigInteger requestCurrentGasPrice() throws IOException {
        HpbGasPrice hpbGasPrice = web3.hpbGasPrice().send();

        return hpbGasPrice.getGasPrice();
    }

    protected TransactionReceipt send(
            String to, String data, BigInteger value, BigInteger gasPrice, BigInteger gasLimit)
            throws IOException, TransactionException {

        return transactionManager.executeTransaction(
                gasPrice, gasLimit, to, data, value);
    }

    protected String call(
            String to, String data, DefaultBlockParameter defaultBlockParameter)
            throws IOException {

        return transactionManager.sendCall(to, data, defaultBlockParameter);
    }
}
