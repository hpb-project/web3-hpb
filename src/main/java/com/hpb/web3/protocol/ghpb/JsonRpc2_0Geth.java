package com.hpb.web3.protocol.ghpb;

import java.util.Arrays;

import com.hpb.web3.protocol.Web3Service;
import com.hpb.web3.protocol.admin.JsonRpc2_0Admin;
import com.hpb.web3.protocol.admin.methods.response.BooleanResponse;
import com.hpb.web3.protocol.admin.methods.response.PersonalSign;
import com.hpb.web3.protocol.core.Request;
import com.hpb.web3.protocol.ghpb.response.PersonalEcRecover;
import com.hpb.web3.protocol.ghpb.response.PersonalImportRawKey;


class JsonRpc2_0Ghpb extends JsonRpc2_0Admin implements Ghpb {

    public JsonRpc2_0Ghpb(Web3Service web3Service) {
        super(web3Service);
    }
    
    @Override
    public Request<?, PersonalImportRawKey> personalImportRawKey(
            String keydata, String password) {
        return new Request<>(
                "personal_importRawKey",
                Arrays.asList(keydata, password),
                web3Service,
                PersonalImportRawKey.class);
    }

    @Override
    public Request<?, BooleanResponse> personalLockAccount(String accountId) {
        return new Request<>(
                "personal_lockAccount",
                Arrays.asList(accountId),
                web3Service,
                BooleanResponse.class);
    }

    @Override
    public Request<?, PersonalSign> personalSign(
            String message, String accountId, String password) {
        return new Request<>(
                "personal_sign",
                Arrays.asList(message,accountId,password),
                web3Service,
                PersonalSign.class);
    }

    @Override
    public Request<?, PersonalEcRecover> personalEcRecover(
            String hexMessage, String signedMessage) {
        return new Request<>(
                "personal_ecRecover",
                Arrays.asList(hexMessage,signedMessage),
                web3Service,
                PersonalEcRecover.class);
    } 
    
}
