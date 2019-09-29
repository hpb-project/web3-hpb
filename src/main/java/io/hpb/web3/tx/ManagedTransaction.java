package io.hpb.web3.tx;
import java.io.IOException;
import java.math.BigInteger;

import io.hpb.web3.ens.EnsResolver;
import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.methods.response.HpbGasPrice;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.protocol.exceptions.TransactionException;
public abstract class ManagedTransaction {
    public static final BigInteger GAS_PRICE = BigInteger.valueOf(22_000_000_000L);
    protected Web3 web3;
    protected TransactionManager transactionManager;
    protected EnsResolver ensResolver;
    protected ManagedTransaction(Web3 web3, TransactionManager transactionManager) {
        this(new EnsResolver(web3), web3, transactionManager);
    }
    protected ManagedTransaction(
            EnsResolver ensResolver, Web3 web3, TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
        this.ensResolver = ensResolver;
        this.web3 = web3;
    }
    public long getSyncThreshold() {
        return ensResolver.getSyncThreshold();
    }
    public void setSyncThreshold(long syncThreshold) {
        ensResolver.setSyncThreshold(syncThreshold);
    }
    public BigInteger requestCurrentGasPrice() throws IOException {
        HpbGasPrice hpbGasPrice = web3.hpbGasPrice().send();
        return hpbGasPrice.getGasPrice();
    }
    protected TransactionReceipt send(
            String to, String data, BigInteger value, BigInteger gasPrice, BigInteger gasLimit)
            throws IOException, TransactionException {
        return transactionManager.executeTransaction(gasPrice, gasLimit, to, data, value);
    }
    protected TransactionReceipt send(
            String to,
            String data,
            BigInteger value,
            BigInteger gasPrice,
            BigInteger gasLimit,
            boolean constructor)
            throws IOException, TransactionException {
        return transactionManager.executeTransaction(
                gasPrice, gasLimit, to, data, value, constructor);
    }
    protected String call(String to, String data, DefaultBlockParameter defaultBlockParameter)
            throws IOException {
        return transactionManager.sendCall(to, data, defaultBlockParameter);
    }
}
