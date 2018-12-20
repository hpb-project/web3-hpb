package io.hpb.web3.crypto;

import io.hpb.web3.utils.Numeric;


public class TransactionUtils {

    
    public static byte[] generateTransactionHash(
            RawTransaction rawTransaction, Credentials credentials) {
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
        return Hash.sha3(signedMessage);
    }

    
    public static byte[] generateTransactionHash(
            RawTransaction rawTransaction, byte chainId, Credentials credentials) {
        byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, chainId, credentials);
        return Hash.sha3(signedMessage);
    }

    
    public static String generateTransactionHashHexEncoded(
            RawTransaction rawTransaction, Credentials credentials) {
        return Numeric.toHexString(generateTransactionHash(rawTransaction, credentials));
    }

    
    public static String generateTransactionHashHexEncoded(
            RawTransaction rawTransaction, byte chainId, Credentials credentials) {
        return Numeric.toHexString(generateTransactionHash(rawTransaction, chainId, credentials));
    }
}
