package com.hpb.web3.protocol.ghpb;

import rx.Observable;

import com.hpb.web3.protocol.Web3Service;
import com.hpb.web3.protocol.admin.Admin;
import com.hpb.web3.protocol.admin.methods.response.BooleanResponse;
import com.hpb.web3.protocol.admin.methods.response.PersonalSign;
import com.hpb.web3.protocol.core.Request;
import com.hpb.web3.protocol.core.methods.response.MinerStartResponse;
import com.hpb.web3.protocol.ghpb.response.PersonalEcRecover;
import com.hpb.web3.protocol.ghpb.response.PersonalImportRawKey;
import com.hpb.web3.protocol.websocket.events.PendingTransactionNotification;
import com.hpb.web3.protocol.websocket.events.SyncingNotfication;


public interface Ghpb extends Admin {
    static Ghpb build(Web3Service web3Service) {
        return new JsonRpc2_0Ghpb(web3Service);
    }
        
    Request<?, PersonalImportRawKey> personalImportRawKey(String keydata, String password);

    Request<?, BooleanResponse> personalLockAccount(String accountId);
    
    Request<?, PersonalSign> personalSign(String message, String accountId, String password);
    
    Request<?, PersonalEcRecover> personalEcRecover(String message, String signiture);

    Request<?, MinerStartResponse> minerStart(int threadCount);

    Request<?, BooleanResponse> minerStop();

    
    Observable<PendingTransactionNotification> newPendingTransactionsNotifications();

    
    Observable<SyncingNotfication> syncingStatusNotifications();

}
