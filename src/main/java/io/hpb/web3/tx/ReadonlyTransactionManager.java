package io.hpb.web3.tx;
import java.io.IOException;
import java.math.BigInteger;

import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.methods.request.Transaction;
import io.hpb.web3.protocol.core.methods.response.HpbSendTransaction;
public class ReadonlyTransactionManager extends TransactionManager {
    private final Web3 web3;
    private String fromAddress;
    public ReadonlyTransactionManager(Web3 web3, String fromAddress) {
        super(web3, fromAddress);
        this.web3 = web3;
        this.fromAddress = fromAddress;
    }
    @Override
    public HpbSendTransaction sendTransaction(
            BigInteger gasPrice,
            BigInteger gasLimit,
            String to,
            String data,
            BigInteger value,
            boolean constructor)
            throws IOException {
        throw new UnsupportedOperationException(
                "Only read operations are supported by this transaction manager");
    }
    @Override
    public String sendCall(String to, String data, DefaultBlockParameter defaultBlockParameter)
            throws IOException {
        return web3.hpbCall(
                        Transaction.createHpbCallTransaction(fromAddress, to, data),
                        defaultBlockParameter)
                .send()
                .getValue();
    }
}
