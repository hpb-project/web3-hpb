package io.hpb.web3.tx;

import java.io.IOException;
import java.math.BigInteger;

import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.methods.response.HpbSendTransaction;


public class ReadonlyTransactionManager extends TransactionManager {

    public ReadonlyTransactionManager(Web3 web3, String fromAddress) {
        super(web3, fromAddress);
    }

    @Override
    public HpbSendTransaction sendTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to, String data, BigInteger value)
            throws IOException {
        throw new UnsupportedOperationException(
                "Only read operations are supported by this transaction manager");
    }
}
