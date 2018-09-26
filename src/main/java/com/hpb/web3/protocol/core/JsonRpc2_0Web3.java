package com.hpb.web3.protocol.core;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;

import rx.Observable;

import com.hpb.web3.protocol.Web3;
import com.hpb.web3.protocol.Web3Service;
import com.hpb.web3.protocol.core.methods.request.ShhFilter;
import com.hpb.web3.protocol.core.methods.request.ShhPost;
import com.hpb.web3.protocol.core.methods.request.Transaction;
import com.hpb.web3.protocol.core.methods.response.DbGetHex;
import com.hpb.web3.protocol.core.methods.response.DbGetString;
import com.hpb.web3.protocol.core.methods.response.DbPutHex;
import com.hpb.web3.protocol.core.methods.response.DbPutString;
import com.hpb.web3.protocol.core.methods.response.HpbAccounts;
import com.hpb.web3.protocol.core.methods.response.HpbBlock;
import com.hpb.web3.protocol.core.methods.response.HpbBlockNumber;
import com.hpb.web3.protocol.core.methods.response.HpbCoinbase;
import com.hpb.web3.protocol.core.methods.response.HpbCompileLLL;
import com.hpb.web3.protocol.core.methods.response.HpbCompileSerpent;
import com.hpb.web3.protocol.core.methods.response.HpbCompileSolidity;
import com.hpb.web3.protocol.core.methods.response.HpbEstimateGas;
import com.hpb.web3.protocol.core.methods.response.HpbFilter;
import com.hpb.web3.protocol.core.methods.response.HpbGasPrice;
import com.hpb.web3.protocol.core.methods.response.HpbGetBalance;
import com.hpb.web3.protocol.core.methods.response.HpbGetBlockTransactionCountByHash;
import com.hpb.web3.protocol.core.methods.response.HpbGetBlockTransactionCountByNumber;
import com.hpb.web3.protocol.core.methods.response.HpbGetCode;
import com.hpb.web3.protocol.core.methods.response.HpbGetCompilers;
import com.hpb.web3.protocol.core.methods.response.HpbGetStorageAt;
import com.hpb.web3.protocol.core.methods.response.HpbGetTransactionCount;
import com.hpb.web3.protocol.core.methods.response.HpbGetTransactionReceipt;
import com.hpb.web3.protocol.core.methods.response.HpbGetUncleCountByBlockHash;
import com.hpb.web3.protocol.core.methods.response.HpbGetUncleCountByBlockNumber;
import com.hpb.web3.protocol.core.methods.response.HpbGetWork;
import com.hpb.web3.protocol.core.methods.response.HpbHashrate;
import com.hpb.web3.protocol.core.methods.response.HpbLog;
import com.hpb.web3.protocol.core.methods.response.HpbMining;
import com.hpb.web3.protocol.core.methods.response.HpbProtocolVersion;
import com.hpb.web3.protocol.core.methods.response.HpbSign;
import com.hpb.web3.protocol.core.methods.response.HpbSubmitHashrate;
import com.hpb.web3.protocol.core.methods.response.HpbSubmitWork;
import com.hpb.web3.protocol.core.methods.response.HpbSubscribe;
import com.hpb.web3.protocol.core.methods.response.HpbSyncing;
import com.hpb.web3.protocol.core.methods.response.HpbTransaction;
import com.hpb.web3.protocol.core.methods.response.HpbUninstallFilter;
import com.hpb.web3.protocol.core.methods.response.Log;
import com.hpb.web3.protocol.core.methods.response.NetListening;
import com.hpb.web3.protocol.core.methods.response.NetPeerCount;
import com.hpb.web3.protocol.core.methods.response.NetVersion;
import com.hpb.web3.protocol.core.methods.response.ShhAddToGroup;
import com.hpb.web3.protocol.core.methods.response.ShhHasIdentity;
import com.hpb.web3.protocol.core.methods.response.ShhMessages;
import com.hpb.web3.protocol.core.methods.response.ShhNewFilter;
import com.hpb.web3.protocol.core.methods.response.ShhNewGroup;
import com.hpb.web3.protocol.core.methods.response.ShhNewIdentity;
import com.hpb.web3.protocol.core.methods.response.ShhUninstallFilter;
import com.hpb.web3.protocol.core.methods.response.ShhVersion;
import com.hpb.web3.protocol.core.methods.response.Web3ClientVersion;
import com.hpb.web3.protocol.core.methods.response.Web3Sha3;
import com.hpb.web3.protocol.rx.JsonRpc2_0Rx;
import com.hpb.web3.protocol.websocket.events.LogNotification;
import com.hpb.web3.protocol.websocket.events.NewHeadsNotification;
import com.hpb.web3.utils.Async;
import com.hpb.web3.utils.Numeric;


public class JsonRpc2_0Web3 implements Web3 {

    public static final int DEFAULT_BLOCK_TIME = 15 * 1000;

    protected final Web3Service web3Service;
    private final JsonRpc2_0Rx web3Rx;
    private final long blockTime;
    private final ScheduledExecutorService scheduledExecutorService;

    public JsonRpc2_0Web3(Web3Service web3Service) {
        this(web3Service, DEFAULT_BLOCK_TIME, Async.defaultExecutorService());
    }

    public JsonRpc2_0Web3(
            Web3Service web3Service, long pollingInterval,
            ScheduledExecutorService scheduledExecutorService) {
        this.web3Service = web3Service;
        this.web3Rx = new JsonRpc2_0Rx(this, scheduledExecutorService);
        this.blockTime = pollingInterval;
        this.scheduledExecutorService = scheduledExecutorService;
    }

    @Override
    public Request<?, Web3ClientVersion> web3ClientVersion() {
        return new Request<>(
                "web3_clientVersion",
                Collections.<String>emptyList(),
                web3Service,
                Web3ClientVersion.class);
    }

    @Override
    public Request<?, Web3Sha3> web3Sha3(String data) {
        return new Request<>(
                "web3_sha3",
                Arrays.asList(data),
                web3Service,
                Web3Sha3.class);
    }

    @Override
    public Request<?, NetVersion> netVersion() {
        return new Request<>(
                "net_version",
                Collections.<String>emptyList(),
                web3Service,
                NetVersion.class);
    }

    @Override
    public Request<?, NetListening> netListening() {
        return new Request<>(
                "net_listening",
                Collections.<String>emptyList(),
                web3Service,
                NetListening.class);
    }

    @Override
    public Request<?, NetPeerCount> netPeerCount() {
        return new Request<>(
                "net_peerCount",
                Collections.<String>emptyList(),
                web3Service,
                NetPeerCount.class);
    }

    @Override
    public Request<?, HpbProtocolVersion> hpbProtocolVersion() {
        return new Request<>(
                "hpb_protocolVersion",
                Collections.<String>emptyList(),
                web3Service,
                HpbProtocolVersion.class);
    }

    @Override
    public Request<?, HpbCoinbase> hpbCoinbase() {
        return new Request<>(
                "hpb_coinbase",
                Collections.<String>emptyList(),
                web3Service,
                HpbCoinbase.class);
    }

    @Override
    public Request<?, HpbSyncing> hpbSyncing() {
        return new Request<>(
                "hpb_syncing",
                Collections.<String>emptyList(),
                web3Service,
                HpbSyncing.class);
    }

    @Override
    public Request<?, HpbMining> hpbMining() {
        return new Request<>(
                "hpb_mining",
                Collections.<String>emptyList(),
                web3Service,
                HpbMining.class);
    }

    @Override
    public Request<?, HpbHashrate> hpbHashrate() {
        return new Request<>(
                "hpb_hashrate",
                Collections.<String>emptyList(),
                web3Service,
                HpbHashrate.class);
    }

    @Override
    public Request<?, HpbGasPrice> hpbGasPrice() {
        return new Request<>(
                "hpb_gasPrice",
                Collections.<String>emptyList(),
                web3Service,
                HpbGasPrice.class);
    }

    @Override
    public Request<?, HpbAccounts> hpbAccounts() {
        return new Request<>(
                "hpb_accounts",
                Collections.<String>emptyList(),
                web3Service,
                HpbAccounts.class);
    }

    @Override
    public Request<?, HpbBlockNumber> hpbBlockNumber() {
        return new Request<>(
                "hpb_blockNumber",
                Collections.<String>emptyList(),
                web3Service,
                HpbBlockNumber.class);
    }

    @Override
    public Request<?, HpbGetBalance> hpbGetBalance(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "hpb_getBalance",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                web3Service,
                HpbGetBalance.class);
    }

    @Override
    public Request<?, HpbGetStorageAt> hpbGetStorageAt(
            String address, BigInteger position, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "hpb_getStorageAt",
                Arrays.asList(
                        address,
                        Numeric.encodeQuantity(position),
                        defaultBlockParameter.getValue()),
                web3Service,
                HpbGetStorageAt.class);
    }

    @Override
    public Request<?, HpbGetTransactionCount> hpbGetTransactionCount(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "hpb_getTransactionCount",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                web3Service,
                HpbGetTransactionCount.class);
    }

    @Override
    public Request<?, HpbGetBlockTransactionCountByHash> hpbGetBlockTransactionCountByHash(
            String blockHash) {
        return new Request<>(
                "hpb_getBlockTransactionCountByHash",
                Arrays.asList(blockHash),
                web3Service,
                HpbGetBlockTransactionCountByHash.class);
    }

    @Override
    public Request<?, HpbGetBlockTransactionCountByNumber> hpbGetBlockTransactionCountByNumber(
            DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "hpb_getBlockTransactionCountByNumber",
                Arrays.asList(defaultBlockParameter.getValue()),
                web3Service,
                HpbGetBlockTransactionCountByNumber.class);
    }

    @Override
    public Request<?, HpbGetUncleCountByBlockHash> hpbGetUncleCountByBlockHash(String blockHash) {
        return new Request<>(
                "hpb_getUncleCountByBlockHash",
                Arrays.asList(blockHash),
                web3Service,
                HpbGetUncleCountByBlockHash.class);
    }

    @Override
    public Request<?, HpbGetUncleCountByBlockNumber> hpbGetUncleCountByBlockNumber(
            DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "hpb_getUncleCountByBlockNumber",
                Arrays.asList(defaultBlockParameter.getValue()),
                web3Service,
                HpbGetUncleCountByBlockNumber.class);
    }

    @Override
    public Request<?, HpbGetCode> hpbGetCode(
            String address, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "hpb_getCode",
                Arrays.asList(address, defaultBlockParameter.getValue()),
                web3Service,
                HpbGetCode.class);
    }

    @Override
    public Request<?, HpbSign> hpbSign(String address, String sha3HashOfDataToSign) {
        return new Request<>(
                "hpb_sign",
                Arrays.asList(address, sha3HashOfDataToSign),
                web3Service,
                HpbSign.class);
    }

    @Override
    public Request<?, com.hpb.web3.protocol.core.methods.response.HpbSendTransaction>
            hpbSendTransaction(
            Transaction transaction) {
        return new Request<>(
                "hpb_sendTransaction",
                Arrays.asList(transaction),
                web3Service,
                com.hpb.web3.protocol.core.methods.response.HpbSendTransaction.class);
    }

    @Override
    public Request<?, com.hpb.web3.protocol.core.methods.response.HpbSendTransaction>
            hpbSendRawTransaction(
            String signedTransactionData) {
        return new Request<>(
                "hpb_sendRawTransaction",
                Arrays.asList(signedTransactionData),
                web3Service,
                com.hpb.web3.protocol.core.methods.response.HpbSendTransaction.class);
    }

    @Override
    public Request<?, com.hpb.web3.protocol.core.methods.response.HpbCall> hpbCall(
            Transaction transaction, DefaultBlockParameter defaultBlockParameter) {
        return new Request<>(
                "hpb_call",
                Arrays.asList(transaction, defaultBlockParameter),
                web3Service,
                com.hpb.web3.protocol.core.methods.response.HpbCall.class);
    }

    @Override
    public Request<?, HpbEstimateGas> hpbEstimateGas(Transaction transaction) {
        return new Request<>(
                "hpb_estimateGas",
                Arrays.asList(transaction),
                web3Service,
                HpbEstimateGas.class);
    }

    @Override
    public Request<?, HpbBlock> hpbGetBlockByHash(
            String blockHash, boolean returnFullTransactionObjects) {
        return new Request<>(
                "hpb_getBlockByHash",
                Arrays.asList(
                        blockHash,
                        returnFullTransactionObjects),
                web3Service,
                HpbBlock.class);
    }

    @Override
    public Request<?, HpbBlock> hpbGetBlockByNumber(
            DefaultBlockParameter defaultBlockParameter,
            boolean returnFullTransactionObjects) {
        return new Request<>(
                "hpb_getBlockByNumber",
                Arrays.asList(
                        defaultBlockParameter.getValue(),
                        returnFullTransactionObjects),
                web3Service,
                HpbBlock.class);
    }

    @Override
    public Request<?, HpbTransaction> hpbGetTransactionByHash(String transactionHash) {
        return new Request<>(
                "hpb_getTransactionByHash",
                Arrays.asList(transactionHash),
                web3Service,
                HpbTransaction.class);
    }

    @Override
    public Request<?, HpbTransaction> hpbGetTransactionByBlockHashAndIndex(
            String blockHash, BigInteger transactionIndex) {
        return new Request<>(
                "hpb_getTransactionByBlockHashAndIndex",
                Arrays.asList(
                        blockHash,
                        Numeric.encodeQuantity(transactionIndex)),
                web3Service,
                HpbTransaction.class);
    }

    @Override
    public Request<?, HpbTransaction> hpbGetTransactionByBlockNumberAndIndex(
            DefaultBlockParameter defaultBlockParameter, BigInteger transactionIndex) {
        return new Request<>(
                "hpb_getTransactionByBlockNumberAndIndex",
                Arrays.asList(
                        defaultBlockParameter.getValue(),
                        Numeric.encodeQuantity(transactionIndex)),
                web3Service,
                HpbTransaction.class);
    }

    @Override
    public Request<?, HpbGetTransactionReceipt> hpbGetTransactionReceipt(String transactionHash) {
        return new Request<>(
                "hpb_getTransactionReceipt",
                Arrays.asList(transactionHash),
                web3Service,
                HpbGetTransactionReceipt.class);
    }

    @Override
    public Request<?, HpbBlock> hpbGetUncleByBlockHashAndIndex(
            String blockHash, BigInteger transactionIndex) {
        return new Request<>(
                "hpb_getUncleByBlockHashAndIndex",
                Arrays.asList(
                        blockHash,
                        Numeric.encodeQuantity(transactionIndex)),
                web3Service,
                HpbBlock.class);
    }

    @Override
    public Request<?, HpbBlock> hpbGetUncleByBlockNumberAndIndex(
            DefaultBlockParameter defaultBlockParameter, BigInteger uncleIndex) {
        return new Request<>(
                "hpb_getUncleByBlockNumberAndIndex",
                Arrays.asList(
                        defaultBlockParameter.getValue(),
                        Numeric.encodeQuantity(uncleIndex)),
                web3Service,
                HpbBlock.class);
    }

    @Override
    public Request<?, HpbGetCompilers> hpbGetCompilers() {
        return new Request<>(
                "hpb_getCompilers",
                Collections.<String>emptyList(),
                web3Service,
                HpbGetCompilers.class);
    }

    @Override
    public Request<?, HpbCompileLLL> hpbCompileLLL(String sourceCode) {
        return new Request<>(
                "hpb_compileLLL",
                Arrays.asList(sourceCode),
                web3Service,
                HpbCompileLLL.class);
    }

    @Override
    public Request<?, HpbCompileSolidity> hpbCompileSolidity(String sourceCode) {
        return new Request<>(
                "hpb_compileSolidity",
                Arrays.asList(sourceCode),
                web3Service,
                HpbCompileSolidity.class);
    }

    @Override
    public Request<?, HpbCompileSerpent> hpbCompileSerpent(String sourceCode) {
        return new Request<>(
                "hpb_compileSerpent",
                Arrays.asList(sourceCode),
                web3Service,
                HpbCompileSerpent.class);
    }

    @Override
    public Request<?, HpbFilter> hpbNewFilter(
            com.hpb.web3.protocol.core.methods.request.HpbFilter hpbFilter) {
        return new Request<>(
                "hpb_newFilter",
                Arrays.asList(hpbFilter),
                web3Service,
                HpbFilter.class);
    }

    @Override
    public Request<?, HpbFilter> hpbNewBlockFilter() {
        return new Request<>(
                "hpb_newBlockFilter",
                Collections.<String>emptyList(),
                web3Service,
                HpbFilter.class);
    }

    @Override
    public Request<?, HpbFilter> hpbNewPendingTransactionFilter() {
        return new Request<>(
                "hpb_newPendingTransactionFilter",
                Collections.<String>emptyList(),
                web3Service,
                HpbFilter.class);
    }

    @Override
    public Request<?, HpbUninstallFilter> hpbUninstallFilter(BigInteger filterId) {
        return new Request<>(
                "hpb_uninstallFilter",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3Service,
                HpbUninstallFilter.class);
    }

    @Override
    public Request<?, HpbLog> hpbGetFilterChanges(BigInteger filterId) {
        return new Request<>(
                "hpb_getFilterChanges",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3Service,
                HpbLog.class);
    }

    @Override
    public Request<?, HpbLog> hpbGetFilterLogs(BigInteger filterId) {
        return new Request<>(
                "hpb_getFilterLogs",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3Service,
                HpbLog.class);
    }

    @Override
    public Request<?, HpbLog> hpbGetLogs(
            com.hpb.web3.protocol.core.methods.request.HpbFilter hpbFilter) {
        return new Request<>(
                "hpb_getLogs",
                Arrays.asList(hpbFilter),
                web3Service,
                HpbLog.class);
    }

    @Override
    public Request<?, HpbGetWork> hpbGetWork() {
        return new Request<>(
                "hpb_getWork",
                Collections.<String>emptyList(),
                web3Service,
                HpbGetWork.class);
    }

    @Override
    public Request<?, HpbSubmitWork> hpbSubmitWork(
            String nonce, String headerPowHash, String mixDigest) {
        return new Request<>(
                "hpb_submitWork",
                Arrays.asList(nonce, headerPowHash, mixDigest),
                web3Service,
                HpbSubmitWork.class);
    }

    @Override
    public Request<?, HpbSubmitHashrate> hpbSubmitHashrate(String hashrate, String clientId) {
        return new Request<>(
                "hpb_submitHashrate",
                Arrays.asList(hashrate, clientId),
                web3Service,
                HpbSubmitHashrate.class);
    }

    @Override
    public Request<?, DbPutString> dbPutString(
            String databaseName, String keyName, String stringToStore) {
        return new Request<>(
                "db_putString",
                Arrays.asList(databaseName, keyName, stringToStore),
                web3Service,
                DbPutString.class);
    }

    @Override
    public Request<?, DbGetString> dbGetString(String databaseName, String keyName) {
        return new Request<>(
                "db_getString",
                Arrays.asList(databaseName, keyName),
                web3Service,
                DbGetString.class);
    }

    @Override
    public Request<?, DbPutHex> dbPutHex(String databaseName, String keyName, String dataToStore) {
        return new Request<>(
                "db_putHex",
                Arrays.asList(databaseName, keyName, dataToStore),
                web3Service,
                DbPutHex.class);
    }

    @Override
    public Request<?, DbGetHex> dbGetHex(String databaseName, String keyName) {
        return new Request<>(
                "db_getHex",
                Arrays.asList(databaseName, keyName),
                web3Service,
                DbGetHex.class);
    }

    @Override
    public Request<?, com.hpb.web3.protocol.core.methods.response.ShhPost> shhPost(ShhPost shhPost) {
        return new Request<>(
                "shh_post",
                Arrays.asList(shhPost),
                web3Service,
                com.hpb.web3.protocol.core.methods.response.ShhPost.class);
    }

    @Override
    public Request<?, ShhVersion> shhVersion() {
        return new Request<>(
                "shh_version",
                Collections.<String>emptyList(),
                web3Service,
                ShhVersion.class);
    }

    @Override
    public Request<?, ShhNewIdentity> shhNewIdentity() {
        return new Request<>(
                "shh_newIdentity",
                Collections.<String>emptyList(),
                web3Service,
                ShhNewIdentity.class);
    }

    @Override
    public Request<?, ShhHasIdentity> shhHasIdentity(String identityAddress) {
        return new Request<>(
                "shh_hasIdentity",
                Arrays.asList(identityAddress),
                web3Service,
                ShhHasIdentity.class);
    }

    @Override
    public Request<?, ShhNewGroup> shhNewGroup() {
        return new Request<>(
                "shh_newGroup",
                Collections.<String>emptyList(),
                web3Service,
                ShhNewGroup.class);
    }

    @Override
    public Request<?, ShhAddToGroup> shhAddToGroup(String identityAddress) {
        return new Request<>(
                "shh_addToGroup",
                Arrays.asList(identityAddress),
                web3Service,
                ShhAddToGroup.class);
    }

    @Override
    public Request<?, ShhNewFilter> shhNewFilter(ShhFilter shhFilter) {
        return new Request<>(
                "shh_newFilter",
                Arrays.asList(shhFilter),
                web3Service,
                ShhNewFilter.class);
    }

    @Override
    public Request<?, ShhUninstallFilter> shhUninstallFilter(BigInteger filterId) {
        return new Request<>(
                "shh_uninstallFilter",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3Service,
                ShhUninstallFilter.class);
    }

    @Override
    public Request<?, ShhMessages> shhGetFilterChanges(BigInteger filterId) {
        return new Request<>(
                "shh_getFilterChanges",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3Service,
                ShhMessages.class);
    }

    @Override
    public Request<?, ShhMessages> shhGetMessages(BigInteger filterId) {
        return new Request<>(
                "shh_getMessages",
                Arrays.asList(Numeric.toHexStringWithPrefixSafe(filterId)),
                web3Service,
                ShhMessages.class);
    }

    @Override
    public Observable<NewHeadsNotification> newHeadsNotifications() {
        return web3Service.subscribe(
                new Request<>(
                        "hpb_subscribe",
                        Collections.singletonList("newHeads"),
                        web3Service,
                        HpbSubscribe.class),
                "hpb_unsubscribe",
                NewHeadsNotification.class
        );
    }

    @Override
    public Observable<LogNotification> logsNotifications(
            List<String> addresses, List<String> topics) {

        Map<String, Object> params = createLogsParams(addresses, topics);

        return web3Service.subscribe(
                new Request<>(
                        "hpb_subscribe",
                        Arrays.asList("logs", params),
                        web3Service,
                        HpbSubscribe.class),
                "hpb_unsubscribe",
                LogNotification.class
        );
    }

    private Map<String, Object> createLogsParams(List<String> addresses, List<String> topics) {
        Map<String, Object> params = new HashMap<>();
        if (!addresses.isEmpty()) {
            params.put("address", addresses);
        }
        if (!topics.isEmpty()) {
            params.put("topics", topics);
        }
        return params;
    }

    @Override
    public Observable<String> hpbBlockHashObservable() {
        return web3Rx.hpbBlockHashObservable(blockTime);
    }

    @Override
    public Observable<String> hpbPendingTransactionHashObservable() {
        return web3Rx.hpbPendingTransactionHashObservable(blockTime);
    }

    @Override
    public Observable<Log> hpbLogObservable(
            com.hpb.web3.protocol.core.methods.request.HpbFilter hpbFilter) {
        return web3Rx.hpbLogObservable(hpbFilter, blockTime);
    }

    @Override
    public Observable<com.hpb.web3.protocol.core.methods.response.Transaction>
            transactionObservable() {
        return web3Rx.transactionObservable(blockTime);
    }

    @Override
    public Observable<com.hpb.web3.protocol.core.methods.response.Transaction>
            pendingTransactionObservable() {
        return web3Rx.pendingTransactionObservable(blockTime);
    }

    @Override
    public Observable<HpbBlock> blockObservable(boolean fullTransactionObjects) {
        return web3Rx.blockObservable(fullTransactionObjects, blockTime);
    }

    @Override
    public Observable<HpbBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects) {
        return web3Rx.replayBlocksObservable(startBlock, endBlock, fullTransactionObjects);
    }

    @Override
    public Observable<HpbBlock> replayBlocksObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock,
            boolean fullTransactionObjects, boolean ascending) {
        return web3Rx.replayBlocksObservable(startBlock, endBlock,
                fullTransactionObjects, ascending);
    }

    @Override
    public Observable<com.hpb.web3.protocol.core.methods.response.Transaction>
            replayTransactionsObservable(
            DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        return web3Rx.replayTransactionsObservable(startBlock, endBlock);
    }

    @Override
    public Observable<HpbBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects,
            Observable<HpbBlock> onCompleteObservable) {
        return web3Rx.catchUpToLatestBlockObservable(
                startBlock, fullTransactionObjects, onCompleteObservable);
    }

    @Override
    public Observable<HpbBlock> catchUpToLatestBlockObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return web3Rx.catchUpToLatestBlockObservable(startBlock, fullTransactionObjects);
    }

    @Override
    public Observable<com.hpb.web3.protocol.core.methods.response.Transaction>
            catchUpToLatestTransactionObservable(DefaultBlockParameter startBlock) {
        return web3Rx.catchUpToLatestTransactionObservable(startBlock);
    }

    @Override
    public Observable<HpbBlock> catchUpToLatestAndSubscribeToNewBlocksObservable(
            DefaultBlockParameter startBlock, boolean fullTransactionObjects) {
        return web3Rx.catchUpToLatestAndSubscribeToNewBlocksObservable(
                startBlock, fullTransactionObjects, blockTime);
    }

    @Override
    public Observable<com.hpb.web3.protocol.core.methods.response.Transaction>
            catchUpToLatestAndSubscribeToNewTransactionsObservable(
            DefaultBlockParameter startBlock) {
        return web3Rx.catchUpToLatestAndSubscribeToNewTransactionsObservable(
                startBlock, blockTime);
    }

    @Override
    public void shutdown() {
        scheduledExecutorService.shutdown();
        try {
            web3Service.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to close web3 service", e);
        }
    }
}
