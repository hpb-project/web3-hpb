package com.hpb.web3.protocol.admin;

import java.math.BigInteger;
import java.util.concurrent.ScheduledExecutorService;

import com.hpb.web3.protocol.Web3;
import com.hpb.web3.protocol.Web3Service;
import com.hpb.web3.protocol.admin.methods.response.NewAccountIdentifier;
import com.hpb.web3.protocol.admin.methods.response.PersonalListAccounts;
import com.hpb.web3.protocol.admin.methods.response.PersonalUnlockAccount;
import com.hpb.web3.protocol.core.Request;
import com.hpb.web3.protocol.core.methods.request.Transaction;
import com.hpb.web3.protocol.core.methods.response.HpbSendTransaction;


public interface Admin extends Web3 {

    static Admin build(Web3Service web3Service) {
        return new JsonRpc2_0Admin(web3Service);
    }
    
    static Admin build(
            Web3Service web3Service, long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        return new JsonRpc2_0Admin(web3Service, pollingInterval, scheduledExecutorService);
    }

    public Request<?, PersonalListAccounts> personalListAccounts();
    
    public Request<?, NewAccountIdentifier> personalNewAccount(String password);
    
    public Request<?, PersonalUnlockAccount> personalUnlockAccount(
            String address, String passphrase, BigInteger duration);
    
    public Request<?, PersonalUnlockAccount> personalUnlockAccount(
            String address, String passphrase);
    
    public Request<?, HpbSendTransaction> personalSendTransaction(
            Transaction transaction, String password);

}   
