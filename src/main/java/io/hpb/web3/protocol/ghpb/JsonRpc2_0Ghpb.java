package io.hpb.web3.protocol.ghpb;

import java.util.Arrays;
import java.util.Collections;

import io.hpb.web3.protocol.Web3Service;
import io.hpb.web3.protocol.admin.JsonRpc2_0Admin;
import io.hpb.web3.protocol.admin.methods.response.BooleanResponse;
import io.hpb.web3.protocol.admin.methods.response.PersonalSign;
import io.hpb.web3.protocol.core.Request;
import io.hpb.web3.protocol.core.methods.response.HpbSubscribe;
import io.hpb.web3.protocol.core.methods.response.MinerStartResponse;
import io.hpb.web3.protocol.ghpb.response.PersonalEcRecover;
import io.hpb.web3.protocol.ghpb.response.PersonalImportRawKey;
import io.hpb.web3.protocol.websocket.events.PendingTransactionNotification;
import io.hpb.web3.protocol.websocket.events.SyncingNotfication;
import io.reactivex.Flowable;


public class JsonRpc2_0Ghpb extends JsonRpc2_0Admin implements Ghpb {

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

    @Override
    public Request<?, MinerStartResponse> minerStart(int threadCount) {
        return new Request<>(
                "miner_start",
                Arrays.asList(threadCount),
                web3Service,
                MinerStartResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> minerStop() {
        return new Request<>(
                "miner_stop",
                Collections.<String>emptyList(),
                web3Service,
                BooleanResponse.class);
    }

    public Flowable<PendingTransactionNotification> newPendingTransactionsNotifications() {
        return web3Service.subscribe(
                new Request<>(
                        "hpb_subscribe",
                        Arrays.asList("newPendingTransactions"),
                        web3Service,
                        HpbSubscribe.class),
                "hpb_unsubscribe",
                PendingTransactionNotification.class
        );
    }

    @Override
    public Flowable<SyncingNotfication> syncingStatusNotifications() {
        return web3Service.subscribe(
                new Request<>(
                        "hpb_subscribe",
                        Arrays.asList("syncing"),
                        web3Service,
                        HpbSubscribe.class),
                "hpb_unsubscribe",
                SyncingNotfication.class
        );
    }
}
