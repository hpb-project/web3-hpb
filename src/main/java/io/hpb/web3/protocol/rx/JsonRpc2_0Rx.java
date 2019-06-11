package io.hpb.web3.protocol.rx;

import java.io.IOException;
import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

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
import io.hpb.web3.utils.Flowables;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;


public class JsonRpc2_0Rx {

    private final Web3 web3;
    private final ScheduledExecutorService scheduledExecutorService;
    private final Scheduler scheduler;

    public JsonRpc2_0Rx(Web3 web3, ScheduledExecutorService scheduledExecutorService) {
        this.web3 = web3;
        this.scheduledExecutorService = scheduledExecutorService;
        this.scheduler = Schedulers.from(scheduledExecutorService);
    }

    public Flowable<String> hpbBlockHashFlowable(long pollingInterval) {
        return Flowable.create(subscriber -> {
            BlockFilter blockFilter = new BlockFilter(
                    web3, subscriber::onNext);
            run(blockFilter, subscriber, pollingInterval);
        }, BackpressureStrategy.BUFFER);
    }

    public Flowable<String> hpbPendingTransactionHashFlowable(long pollingInterval) {
        return Flowable.create(subscriber -> {
            PendingTransactionFilter pendingTransactionFilter = new PendingTransactionFilter(
                    web3, subscriber::onNext);

            run(pendingTransactionFilter, subscriber, pollingInterval);
        }, BackpressureStrategy.BUFFER);
    }

    public Flowable<Log> hpbLogFlowable(
            io.hpb.web3.protocol.core.methods.request.HpbFilter hpbFilter, long pollingInterval) {
        return Flowable.create(subscriber -> {
            LogFilter logFilter = new LogFilter(
                    web3, subscriber::onNext, hpbFilter);

            run(logFilter, subscriber, pollingInterval);
        }, BackpressureStrategy.BUFFER);
    }

    private <T> void run(
            io.hpb.web3.protocol.core.filters.Filter<T> filter, FlowableEmitter<? super T> emitter,
            long pollingInterval) {

        filter.run(scheduledExecutorService, pollingInterval);
        emitter.setCancellable(filter::cancel);
    }

    public Flowable<Transaction> transactionFlowable(long pollingInterval) {
        return blockFlowable(true, pollingInterval)
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Flowable<Transaction> pendingTransactionFlowable(long pollingInterval) {
        return hpbPendingTransactionHashFlowable(pollingInterval)
                .flatMap(transactionHash ->
                        web3.hpbGetTransactionByHash(transactionHash).flowable())
                .filter(hpbTransaction -> hpbTransaction.getTransaction().isPresent())
                .map(hpbTransaction -> hpbTransaction.getTransaction().get());
    }

    public Flowable<HpbBlock> blockFlowable(
            boolean fullTransactionObjects, long pollingInterval) {
        return hpbBlockHashFlowable(pollingInterval)
                .flatMap(blockHash ->
                        web3.hpbGetBlockByHash(blockHash, fullTransactionObjects).flowable());
    }

    public Flowable<HpbBlock> replayBlocksFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects) {
        return replayBlocksFlowable(startBlock, endBlock, fullTransactionObjects, true);
    }

    public Flowable<HpbBlock> replayBlocksFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending) {
        
        
        return replayBlocksFlowableSync(startBlock, endBlock, fullTransactionObjects, ascending)
                .subscribeOn(scheduler);
    }

    private Flowable<HpbBlock> replayBlocksFlowableSync(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects) {
        return replayBlocksFlowableSync(startBlock, endBlock, fullTransactionObjects, true);
    }

    private Flowable<HpbBlock> replayBlocksFlowableSync(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending) {

        BigInteger startBlockNumber = null;
        BigInteger endBlockNumber = null;
        try {
            startBlockNumber = getBlockNumber(startBlock);
            endBlockNumber = getBlockNumber(endBlock);
        } catch (IOException e) {
            Flowable.error(e);
        }

        if (ascending) {
            return Flowables.range(startBlockNumber, endBlockNumber)
                    .flatMap(i -> web3.hpbGetBlockByNumber(
                            new DefaultBlockParameterNumber(i),
                            fullTransactionObjects).flowable());
        } else {
            return Flowables.range(startBlockNumber, endBlockNumber, false)
                    .flatMap(i -> web3.hpbGetBlockByNumber(
                            new DefaultBlockParameterNumber(i),
                            fullTransactionObjects).flowable());
        }
    }

    public Flowable<Transaction> replayTransactionsFlowable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        return replayBlocksFlowable(startBlock, endBlock, true)
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Flowable<HpbBlock> replayPastBlocksFlowable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            Flowable<HpbBlock> onCompleteFlowable) {
        
        
        return replayPastBlocksFlowableSync(
                startBlock, fullTransactionObjects, onCompleteFlowable)
                .subscribeOn(scheduler);
    }

    public Flowable<HpbBlock> replayPastBlocksFlowable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return replayPastBlocksFlowable(
                startBlock, fullTransactionObjects, Flowable.empty());
    }

    private Flowable<HpbBlock> replayPastBlocksFlowableSync(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            Flowable<HpbBlock> onCompleteFlowable) {

        BigInteger startBlockNumber;
        BigInteger latestBlockNumber;
        try {
            startBlockNumber = getBlockNumber(startBlock);
            latestBlockNumber = getLatestBlockNumber();
        } catch (IOException e) {
            return Flowable.error(e);
        }

        if (startBlockNumber.compareTo(latestBlockNumber) > -1) {
            return onCompleteFlowable;
        } else {
            return Flowable.concat(
                    replayBlocksFlowableSync(
                            new DefaultBlockParameterNumber(startBlockNumber),
                            new DefaultBlockParameterNumber(latestBlockNumber),
                            fullTransactionObjects),
                    Flowable.defer(() -> replayPastBlocksFlowableSync(
                            new DefaultBlockParameterNumber(latestBlockNumber.add(BigInteger.ONE)),
                            fullTransactionObjects,
                            onCompleteFlowable)));
        }
    }

    public Flowable<Transaction> replayPastTransactionsFlowable(
            DefaultBlockParameter startBlock) {
        return replayPastBlocksFlowable(
                startBlock, true, Flowable.empty())
                .flatMapIterable(JsonRpc2_0Rx::toTransactions);
    }

    public Flowable<HpbBlock> replayPastAndFutureBlocksFlowable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            long pollingInterval) {

        return replayPastBlocksFlowable(
                startBlock, fullTransactionObjects,
                blockFlowable(fullTransactionObjects, pollingInterval));
    }

    public Flowable<Transaction> replayPastAndFutureTransactionsFlowable(
            DefaultBlockParameter startBlock, long pollingInterval) {
        return replayPastAndFutureBlocksFlowable(
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
        
        
        return hpbBlock.getBlock().getTransactions().stream()
                .map(transactionResult -> (Transaction) transactionResult.get())
                .collect(Collectors.toList());
    }
}
