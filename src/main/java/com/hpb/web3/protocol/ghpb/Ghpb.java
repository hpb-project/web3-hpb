package com.hpb.web3.protocol.ghpb;

import com.hpb.web3.protocol.Web3Service;
import com.hpb.web3.protocol.admin.Admin;
import com.hpb.web3.protocol.admin.methods.response.BooleanResponse;
import com.hpb.web3.protocol.admin.methods.response.PersonalSign;
import com.hpb.web3.protocol.core.Request;
import com.hpb.web3.protocol.ghpb.response.PersonalEcRecover;
import com.hpb.web3.protocol.ghpb.response.PersonalImportRawKey;


public interface Ghpb extends Admin {
    static Ghpb build(Web3Service web3Service) {
        return new JsonRpc2_0Ghpb(web3Service);
    }
        
    public Request<?, PersonalImportRawKey> personalImportRawKey(String keydata, String password);
    
    public Request<?, BooleanResponse> personalLockAccount(String accountId);
    
    public Request<?, PersonalSign> personalSign(String message, String accountId, String password);
    
    public Request<?, PersonalEcRecover> personalEcRecover(String message, String signiture);
    
}
