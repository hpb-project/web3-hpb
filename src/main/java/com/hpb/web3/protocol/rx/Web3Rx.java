package com.hpb.web3.protocol.rx;

import com.hpb.web3.protocol.core.DefaultBlockParameter;
import com.hpb.web3.protocol.core.methods.request.HpbFilter;
import com.hpb.web3.protocol.core.methods.response.HpbBlock;
import com.hpb.web3.protocol.core.methods.response.Log;
import com.hpb.web3.protocol.core.methods.response.Transaction;

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
}
