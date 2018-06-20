package com.hpb.web3.protocol.parity.methods.response;

import com.hpb.web3.crypto.WalletFile;
import com.hpb.web3.protocol.core.Response;


public class ParityExportAccount extends Response<WalletFile> {
    public WalletFile getWallet() {
        return getResult();
    }
}
