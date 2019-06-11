package io.hpb.web3.tx.gas;

import java.math.BigDecimal;
import java.math.BigInteger;

public class StaticGasProvider implements ContractGasProvider {
    private BigInteger gasPrice;
    private BigInteger gasLimit;

    public StaticGasProvider(BigInteger gasPrice, BigInteger gasLimit) {
        this.gasPrice = gasPrice;
        this.gasLimit = gasLimit;
    }
    public StaticGasProvider(BigDecimal gasPrice, BigDecimal gasLimit) {
    	this.gasPrice = gasPrice.toBigInteger();
    	this.gasLimit = gasLimit.toBigInteger();
    }

    @Override
    public BigInteger getGasPrice(String contractFunc) {
        return gasPrice;
    }

    @Override
    public BigInteger getGasPrice() {
        return gasPrice;
    }

    @Override
    public BigInteger getGasLimit(String contractFunc) {
        return gasLimit;
    }

    @Override
    public BigInteger getGasLimit() {
        return gasLimit;
    }
}
