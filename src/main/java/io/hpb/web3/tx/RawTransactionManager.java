package io.hpb.web3.tx;

import java.io.IOException;
import java.math.BigInteger;

import io.hpb.web3.crypto.Credentials;
import io.hpb.web3.crypto.Hash;
import io.hpb.web3.crypto.RawTransaction;
import io.hpb.web3.crypto.TransactionEncoder;
import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.methods.response.HpbGetTransactionCount;
import io.hpb.web3.protocol.core.methods.response.HpbSendTransaction;
import io.hpb.web3.tx.exceptions.TxHashMismatchException;
import io.hpb.web3.tx.response.TransactionReceiptProcessor;
import io.hpb.web3.utils.Numeric;
import io.hpb.web3.utils.TxHashVerifier;


public class RawTransactionManager extends TransactionManager {

    private final Web3 web3;
    final Credentials credentials;

    private final byte chainId;

    protected TxHashVerifier txHashVerifier = new TxHashVerifier();

    public RawTransactionManager(Web3 web3, Credentials credentials, byte chainId) {
        super(web3, credentials.getAddress());

        this.web3 = web3;
        this.credentials = credentials;

        this.chainId = chainId;
    }

    public RawTransactionManager(
            Web3 web3, Credentials credentials, byte chainId,
            TransactionReceiptProcessor transactionReceiptProcessor) {
        super(transactionReceiptProcessor, credentials.getAddress());

        this.web3 = web3;
        this.credentials = credentials;

        this.chainId = chainId;
    }

    public RawTransactionManager(
            Web3 web3, Credentials credentials, byte chainId, int attempts, long sleepDuration) {
        super(web3, attempts, sleepDuration, credentials.getAddress());

        this.web3 = web3;
        this.credentials = credentials;

        this.chainId = chainId;
    }

    public RawTransactionManager(Web3 web3, Credentials credentials) {
        this(web3, credentials, ChainId.NONE);
    }

    public RawTransactionManager(
            Web3 web3, Credentials credentials, int attempts, int sleepDuration) {
        this(web3, credentials, ChainId.NONE, attempts, sleepDuration);
    }

    protected BigInteger getNonce() throws IOException {
        HpbGetTransactionCount hpbGetTransactionCount = web3.hpbGetTransactionCount(
                credentials.getAddress(), DefaultBlockParameterName.PENDING).send();

        return hpbGetTransactionCount.getTransactionCount();
    }

    public TxHashVerifier getTxHashVerifier() {
        return txHashVerifier;
    }

    public void setTxHashVerifier(TxHashVerifier txHashVerifier) {
        this.txHashVerifier = txHashVerifier;
    }

    @Override
    public HpbSendTransaction sendTransaction(
            BigInteger gasPrice, BigInteger gasLimit, String to,
            String data, BigInteger value) throws IOException {

        BigInteger nonce = getNonce();

        RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasPrice,
                gasLimit,
                to,
                value,
                data);

        return signAndSend(rawTransaction);
    }

    public HpbSendTransaction signAndSend(RawTransaction rawTransaction)
            throws IOException {

        byte[] signedMessage;

        if (chainId > ChainId.NONE) {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        } else {
            signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        }

        String hexValue = Numeric.toHexString(signedMessage);
        HpbSendTransaction hpbSendTransaction = web3.hpbSendRawTransaction(hexValue).send();

        if (hpbSendTransaction != null && !hpbSendTransaction.hasError()) {
            String txHashLocal = Hash.sha3(hexValue);
            String txHashRemote = hpbSendTransaction.getTransactionHash();
            if (!txHashVerifier.verify(txHashLocal, txHashRemote)) {
                throw new TxHashMismatchException(txHashLocal, txHashRemote);
            }
        }

        return hpbSendTransaction;
    }
}
