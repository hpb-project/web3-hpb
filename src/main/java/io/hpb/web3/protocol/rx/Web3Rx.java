package io.hpb.web3.protocol.rx;

import java.util.List;

import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.methods.request.HpbFilter;
import io.hpb.web3.protocol.core.methods.response.HpbBlock;
import io.hpb.web3.protocol.core.methods.response.Log;
import io.hpb.web3.protocol.core.methods.response.Transaction;
import io.hpb.web3.protocol.websocket.events.LogNotification;
import io.hpb.web3.protocol.websocket.events.NewHeadsNotification;

import rx.Observable;


public interface Web3Rx {

    
    Observable<Log> hpbLogObservable(HpbFilter hpbFilter);

    
    Observable<String> hpbBlockHashObservable();

    
    Observable<String> hpbPendingTransactionHashObservable();

    
    Observable<Transaction> transactionObservable();

    
    Observable<Transaction> pendingTransactionObservable();

    
    Observable<HpbBlock> blockObservable(boolean fullTransactionObjects);

    
    Observable<HpbBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects);

    
    Observable<HpbBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending);

    
    Observable<Transaction> replayTransactionsObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock);

    
    Observable<HpbBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            Observable<HpbBlock> onCompleteObservable);

    
    Observable<HpbBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects);

    
    Observable<Transaction> catchUpToLatestTransactionObservable(
            DefaultBlockParameter startBlock);

    
    Observable<HpbBlock> catchUpToLatestAndSubscribeToNewBlocksObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects);

    
    Observable<Transaction> catchUpToLatestAndSubscribeToNewTransactionsObservable(
            DefaultBlockParameter startBlock);

    
    Observable<NewHeadsNotification> newHeadsNotifications();

    
    Observable<LogNotification> logsNotifications(List<String> addresses, List<String> topics);
}
