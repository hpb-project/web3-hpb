package io.hpb.web3.protocol.admin;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import io.hpb.web3.protocol.Web3Service;
import io.hpb.web3.protocol.admin.methods.response.NewAccountIdentifier;
import io.hpb.web3.protocol.admin.methods.response.PersonalListAccounts;
import io.hpb.web3.protocol.admin.methods.response.PersonalUnlockAccount;
import io.hpb.web3.protocol.core.JsonRpc2_0Web3;
import io.hpb.web3.protocol.core.Request;
import io.hpb.web3.protocol.core.methods.request.Transaction;
import io.hpb.web3.protocol.core.methods.response.HpbSendTransaction;


public class JsonRpc2_0Admin extends JsonRpc2_0Web3 implements Admin {

    public JsonRpc2_0Admin(Web3Service web3Service) {
        super(web3Service);
    }
    
    public JsonRpc2_0Admin(Web3Service web3Service, long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        super(web3Service, pollingInterval, scheduledExecutorService);
    }

    @Override
    public Request<?, PersonalListAccounts> personalListAccounts() {
        return new Request<>(
                "personal_listAccounts",
                Collections.<String>emptyList(),
                web3Service,
                PersonalListAccounts.class);
    }

    @Override
    public Request<?, NewAccountIdentifier> personalNewAccount(String password) {
        return new Request<>(
                "personal_newAccount",
                Arrays.asList(password),
                web3Service,
                NewAccountIdentifier.class);
    }   

    @Override
    public Request<?, PersonalUnlockAccount> personalUnlockAccount(
            String accountId, String password,
            BigInteger duration) {
        List<Object> attributes = new ArrayList<>(3);
        attributes.add(accountId);
        attributes.add(password);
        
        if (duration != null) {
            // Parity has a bug where it won't support a duration
            // See https://github.com/hpbcore/parity/issues/1215
            attributes.add(duration.longValue());
        } else {
            // we still need to include the null value, otherwise Parity rejects request
            attributes.add(null);
        }
        
        return new Request<>(
                "personal_unlockAccount",
                attributes,
                web3Service,
                PersonalUnlockAccount.class);
    }
    
    @Override
    public Request<?, PersonalUnlockAccount> personalUnlockAccount(
            String accountId, String password) {
        
        return personalUnlockAccount(accountId, password, null);
    }
    
    @Override
    public Request<?, HpbSendTransaction> personalSendTransaction(
            Transaction transaction, String passphrase) {
        return new Request<>(
                "personal_sendTransaction",
                Arrays.asList(transaction, passphrase),
                web3Service,
                HpbSendTransaction.class);
    }
    
}
