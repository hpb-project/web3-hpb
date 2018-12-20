package io.hpb.web3.ens;

import java.net.IDN;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import io.hpb.web3.crypto.Hash;
import io.hpb.web3.utils.Numeric;


public class NameHash {

    private static final byte[] EMPTY = new byte[32];

    public static byte[] nameHashAsBytes(String ensName) {
        return Numeric.hexStringToByteArray(nameHash(ensName));
    }

    public static String nameHash(String ensName) {
        String normalisedEnsName = normalise(ensName);
        return Numeric.toHexString(nameHash(normalisedEnsName.split("\\.")));
    }

    private static byte[] nameHash(String[] labels) {
        if (labels.length == 0 || labels[0].equals("")) {
            return EMPTY;
        } else {
            String[] tail;
            if (labels.length == 1) {
                tail = new String[] { };
            } else {
                tail = Arrays.copyOfRange(labels, 1, labels.length);
            }

            byte[] remainderHash = nameHash(tail);
            byte[] result = Arrays.copyOf(remainderHash, 64);

            byte[] labelHash = Hash.sha3(labels[0].getBytes(StandardCharsets.UTF_8));
            System.arraycopy(labelHash, 0, result, 32, labelHash.length);

            return Hash.sha3(result);
        }
    }

    
    static String normalise(String ensName) {
        try {
            return IDN.toASCII(ensName, IDN.USE_STD3_ASCII_RULES)
                    .toLowerCase();
        } catch (IllegalArgumentException e) {
            throw new EnsResolutionException("Invalid ENS name provided: " + ensName);
        }
    }
}
