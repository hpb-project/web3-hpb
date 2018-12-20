package io.hpb.web3.protocol.rx;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.DefaultBlockParameterNumber;
import io.hpb.web3.protocol.core.filters.BlockFilter;
import io.hpb.web3.protocol.core.filters.LogFilter;
import io.hpb.web3.protocol.core.filters.PendingTransactionFilter;
import io.hpb.web3.protocol.core.methods.response.HpbBlock;
import io.hpb.web3.protocol.core.methods.response.Log;
import io.hpb.web3.protocol.core.methods.response.Transaction;
import io.hpb.web3.utils.Observables;


public class JsonRpc2_0Rx {

    private final Web3 web3;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Scheduler scheduler;

    public JsonRpc2_0Rx(Web3 web3, ScheduledExecutorService scheduledExecutorService) {
        this.web3 = web3;
        this.scheduledExecutorService = scheduledExecutorService;
        this.scheduler = Schedulers.from(scheduledExecutorService);
    }

    public Observable<String> hpbBlockHashObservable(long pollingInterval) {
        return Observable.create(subscriber -> {
            BlockFilter blockFilter = new BlockFilter(
                    web3, subscriber::onNext);
            run(blockFilter, subscriber, pollingInterval);
        });
    }

    public Observable<String> hpbPendingTransactionHashObservable(long pollingInterval) {
        return Observable.create(subscriber -> {
            PendingTransactionFilter pendingTransactionFilter = new PendingTransactionFilter(
                    web3, subscriber::onNext);

            run(pendingTransactionFilter, subscriber, pollingInterval);
        });
    }

    public Observable<Log> hpbLogObservable(
            io.hpb.web3.protocol.core.methods.request.HpbFilter hpbFilter, long pollingInterval) {
        return Observable.create((Subscriber<? super Log> subscriber) -> {
            LogFilter logFilter = new LogFilter(
                    web3, subscriber::onNext, hpbFilter);

            run(logFilter, subscriber, pollingInterval);
        });
    }

    private <T> void run(
            io.hpb.web3.protocol.core.filters.Filter<T> filter, Subscriber<? super T> subscriber,
            long pollingInterval) {

        filter.run(scheduledExecutorService, pollingInterval);
        subscriber.add(Subscriptions.create(filter::cancel));
    }

    public Observable<Transaction> transactionObservable(long pollingInterval) {
        return blockObservable(true, pollingInterval)
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Observable<Transaction> pendingTransactionObservable(long pollingInterval) {
        return hpbPendingTransactionHashObservable(pollingInterval)
                .flatMap(transactionHash ->
                        web3.hpbGetTransactionByHash(transactionHash).observable())
                .filter(hpbTransaction -> hpbTransaction.getTransaction().isPresent())
                .map(hpbTransaction -> hpbTransaction.getTransaction().get());
    }

    public Observable<HpbBlock> blockObservable(
            boolean fullTransactionObjects, long pollingInterval) {
        return hpbBlockHashObservable(pollingInterval)
                .flatMap(blockHash ->
                        web3.hpbGetBlockByHash(blockHash, fullTransactionObjects).observable());
    }

    public Observable<HpbBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects) {
        return replayBlocksObservable(startBlock, endBlock, fullTransactionObjects, true);
    }

    public Observable<HpbBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending) {
        // We use a scheduler to ensure this Observable runs asynchronously for users to be
        // consistent with the other Observables
        return replayBlocksObservableSync(startBlock, endBlock, fullTransactionObjects, ascending)
                .subscribeOn(scheduler);
    }

    private Observable<HpbBlock> replayBlocksObservableSync(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects) {
        return replayBlocksObservableSync(startBlock, endBlock, fullTransactionObjects, true);
    }

    private Observable<HpbBlock> replayBlocksObservableSync(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending) {

        BigInteger startBlockNumber = null;
        BigInteger endBlockNumber = null;
        try {
            startBlockNumber = getBlockNumber(startBlock);
            endBlockNumber = getBlockNumber(endBlock);
        } catch (IOException e) {
            Observable.error(e);
        }

        if (ascending) {
            return Observables.range(startBlockNumber, endBlockNumber)
                    .flatMap(i -> web3.hpbGetBlockByNumber(
                            new DefaultBlockParameterNumber(i),
                            fullTransactionObjects).observable());
        } else {
            return Observables.range(startBlockNumber, endBlockNumber, false)
                    .flatMap(i -> web3.hpbGetBlockByNumber(
                            new DefaultBlockParameterNumber(i),
                            fullTransactionObjects).observable());
        }
    }

    public Observable<Transaction> replayTransactionsObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        return replayBlocksObservable(startBlock, endBlock, true)
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Observable<HpbBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            Observable<HpbBlock> onCompleteObservable) {
        // We use a scheduler to ensure this Observable runs asynchronously for users to be
        // consistent with the other Observables
        return catchUpToLatestBlockObservableSync(
                startBlock, fullTransactionObjects, onCompleteObservable)
                .subscribeOn(scheduler);
    }

    public Observable<HpbBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return catchUpToLatestBlockObservable(
                startBlock, fullTransactionObjects, Observable.empty());
    }

    private Observable<HpbBlock> catchUpToLatestBlockObservableSync(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            Observable<HpbBlock> onCompleteObservable) {

        BigInteger startBlockNumber;
        BigInteger latestBlockNumber;
        try {
            startBlockNumber = getBlockNumber(startBlock);
            latestBlockNumber = getLatestBlockNumber();
        } catch (IOException e) {
            return Observable.error(e);
        }

        if (startBlockNumber.compareTo(latestBlockNumber) > -1) {
            return onCompleteObservable;
        } else {
            return Observable.concat(
                    replayBlocksObservableSync(
                            new DefaultBlockParameterNumber(startBlockNumber),
                            new DefaultBlockParameterNumber(latestBlockNumber),
                            fullTransactionObjects),
                    Observable.defer(() -> catchUpToLatestBlockObservableSync(
                            new DefaultBlockParameterNumber(latestBlockNumber.add(BigInteger.ONE)),
                            fullTransactionObjects,
                            onCompleteObservable)));
        }
    }

    public Observable<Transaction> catchUpToLatestTransactionObservable(
            DefaultBlockParameter startBlock) {
        return catchUpToLatestBlockObservable(
                startBlock, true, Observable.empty())
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Observable<HpbBlock> catchUpToLatestAndSubscribeToNewBlocksObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            long pollingInterval) {

        return catchUpToLatestBlockObservable(
                startBlock, fullTransactionObjects,
                blockObservable(fullTransactionObjects, pollingInterval));
    }

    public Observable<Transaction> catchUpToLatestAndSubscribeToNewTransactionsObservable(
            DefaultBlockParameter startBlock, long pollingInterval) {
        return catchUpToLatestAndSubscribeToNewBlocksObservable(
                startBlock, true, pollingInterval)
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    private BigInteger getLatestBlockNumber() throws IOException {
        return getBlockNumber(DefaultBlockParameterName.LATEST);
    }

    private BigInteger getBlockNumber(
            DefaultBlockParameter defaultBlockParameter) throws IOException {
        if (defaultBlockParameter instanceof DefaultBlockParameterNumber) {
            return ((DefaultBlockParameterNumber) defaultBlockParameter).getBlockNumber();
        } else {
            HpbBlock latestHpbBlock = web3.hpbGetBlockByNumber(
                    defaultBlockParameter, false).send();
            return latestHpbBlock.getBlock().getNumber();
        }
    }

    private static List<Transaction> toTransactions(HpbBlock hpbBlock) {
        // If you ever see an exception thrown here, it's probably due to an incomplete chain in
        // Ghpb/Parity. You should resync to solve.
        return hpbBlock.getBlock().getTransactions().stream()
                .map(transactionResult -> (Transaction) transactionResult.get())
                .collect(Collectors.toList());
    }
}