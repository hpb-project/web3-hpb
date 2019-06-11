package io.hpb.web3.protocol.admin;

import java.math.BigInteger;
import java.util.concurrent.ScheduledExecutorService;

import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.Web3Service;
import io.hpb.web3.protocol.admin.methods.response.NewAccountIdentifier;
import io.hpb.web3.protocol.admin.methods.response.PersonalListAccounts;
import io.hpb.web3.protocol.admin.methods.response.PersonalUnlockAccount;
import io.hpb.web3.protocol.core.Request;
import io.hpb.web3.protocol.core.methods.request.Transaction;
import io.hpb.web3.protocol.core.methods.response.HpbSendTransaction;


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
