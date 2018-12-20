package io.hpb.web3.ens;

import io.hpb.web3.tx.ChainId;


public class Contracts {

    public static final String MAINNET = "0x314159265dd8dbb310642f98f50c066173c1259b";

    public static String resolveRegistryContract(String chainId) {
        switch (Byte.valueOf(chainId)) {
            case ChainId.MAINNET:
                return MAINNET;
            default:
                throw new EnsResolutionException(
                        "Unable to resolve ENS registry contract for network id: " + chainId);
        }
    }
}
