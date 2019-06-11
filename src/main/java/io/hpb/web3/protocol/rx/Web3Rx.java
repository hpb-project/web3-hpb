package io.hpb.web3.protocol.rx;

import java.util.List;

import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.methods.request.HpbFilter;
import io.hpb.web3.protocol.core.methods.response.HpbBlock;
import io.hpb.web3.protocol.core.methods.response.Log;
import io.hpb.web3.protocol.core.methods.response.Transaction;
import io.hpb.web3.protocol.websocket.events.LogNotification;
import io.hpb.web3.protocol.websocket.events.NewHeadsNotification;
import io.reactivex.Flowable;


public interface Web3Rx {

    
    Flowable<Log> hpbLogFlowable(HpbFilter hpbFilter);

    
    Flowable<String> hpbBlockHashFlowable();

    
    Flowable<String> hpbPendingTransactionHashFlowable();

    
    Flowable<Transaction> transactionFlowable();

    
    Flowable<Transaction> pendingTransactionFlowable();

    
    Flowable<HpbBlock> blockFlowable(boolean fullTransactionObjects);

    
    Flowable<HpbBlock> replayPastBlocksFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects);

    
    Flowable<HpbBlock> replayPastBlocksFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending);

    
    Flowable<HpbBlock> replayPastBlocksFlowable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            Flowable<HpbBlock> onCompleteFlowable);

    
    Flowable<HpbBlock> replayPastBlocksFlowable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects);

    
    Flowable<Transaction> replayPastTransactionsFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock);

    
    Flowable<Transaction> replayPastTransactionsFlowable(
            DefaultBlockParameter startBlock);

    
    Flowable<HpbBlock> replayPastAndFutureBlocksFlowable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects);

    
    Flowable<Transaction> replayPastAndFutureTransactionsFlowable(
            DefaultBlockParameter startBlock);

    
    Flowable<NewHeadsNotification> newHeadsNotifications();

    
    Flowable<LogNotification> logsNotifications(List<String> addresses, List<String> topics);
}
