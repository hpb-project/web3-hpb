package io.hpb.web3.protocol.core;

import java.math.BigInteger;

import io.hpb.web3.protocol.core.methods.request.ShhFilter;
import io.hpb.web3.protocol.core.methods.response.DbGetHex;
import io.hpb.web3.protocol.core.methods.response.DbGetString;
import io.hpb.web3.protocol.core.methods.response.DbPutHex;
import io.hpb.web3.protocol.core.methods.response.DbPutString;
import io.hpb.web3.protocol.core.methods.response.HpbAccounts;
import io.hpb.web3.protocol.core.methods.response.HpbBlock;
import io.hpb.web3.protocol.core.methods.response.HpbBlockNumber;
import io.hpb.web3.protocol.core.methods.response.HpbCoinbase;
import io.hpb.web3.protocol.core.methods.response.HpbCompileLLL;
import io.hpb.web3.protocol.core.methods.response.HpbCompileSerpent;
import io.hpb.web3.protocol.core.methods.response.HpbCompileSolidity;
import io.hpb.web3.protocol.core.methods.response.HpbEstimateGas;
import io.hpb.web3.protocol.core.methods.response.HpbFilter;
import io.hpb.web3.protocol.core.methods.response.HpbGasPrice;
import io.hpb.web3.protocol.core.methods.response.HpbGetBalance;
import io.hpb.web3.protocol.core.methods.response.HpbGetBlockTransactionCountByHash;
import io.hpb.web3.protocol.core.methods.response.HpbGetBlockTransactionCountByNumber;
import io.hpb.web3.protocol.core.methods.response.HpbGetCode;
import io.hpb.web3.protocol.core.methods.response.HpbGetCompilers;
import io.hpb.web3.protocol.core.methods.response.HpbGetStorageAt;
import io.hpb.web3.protocol.core.methods.response.HpbGetTransactionCount;
import io.hpb.web3.protocol.core.methods.response.HpbGetTransactionReceipt;
import io.hpb.web3.protocol.core.methods.response.HpbGetUncleCountByBlockHash;
import io.hpb.web3.protocol.core.methods.response.HpbGetUncleCountByBlockNumber;
import io.hpb.web3.protocol.core.methods.response.HpbGetWork;
import io.hpb.web3.protocol.core.methods.response.HpbHashrate;
import io.hpb.web3.protocol.core.methods.response.HpbLog;
import io.hpb.web3.protocol.core.methods.response.HpbMining;
import io.hpb.web3.protocol.core.methods.response.HpbProtocolVersion;
import io.hpb.web3.protocol.core.methods.response.HpbSign;
import io.hpb.web3.protocol.core.methods.response.HpbSubmitHashrate;
import io.hpb.web3.protocol.core.methods.response.HpbSubmitWork;
import io.hpb.web3.protocol.core.methods.response.HpbSyncing;
import io.hpb.web3.protocol.core.methods.response.HpbTransaction;
import io.hpb.web3.protocol.core.methods.response.HpbUninstallFilter;
import io.hpb.web3.protocol.core.methods.response.NetListening;
import io.hpb.web3.protocol.core.methods.response.NetPeerCount;
import io.hpb.web3.protocol.core.methods.response.NetVersion;
import io.hpb.web3.protocol.core.methods.response.ShhAddToGroup;
import io.hpb.web3.protocol.core.methods.response.ShhHasIdentity;
import io.hpb.web3.protocol.core.methods.response.ShhMessages;
import io.hpb.web3.protocol.core.methods.response.ShhNewFilter;
import io.hpb.web3.protocol.core.methods.response.ShhNewGroup;
import io.hpb.web3.protocol.core.methods.response.ShhNewIdentity;
import io.hpb.web3.protocol.core.methods.response.ShhUninstallFilter;
import io.hpb.web3.protocol.core.methods.response.ShhVersion;
import io.hpb.web3.protocol.core.methods.response.Web3ClientVersion;
import io.hpb.web3.protocol.core.methods.response.Web3Sha3;


public interface Hpb {
    Request<?, Web3ClientVersion> web3ClientVersion();

    Request<?, Web3Sha3> web3Sha3(String data);

    Request<?, NetVersion> netVersion();

    Request<?, NetListening> netListening();

    Request<?, NetPeerCount> netPeerCount();

    Request<?, HpbProtocolVersion> hpbProtocolVersion();

    Request<?, HpbCoinbase> hpbCoinbase();

    Request<?, HpbSyncing> hpbSyncing();

    Request<?, HpbMining> hpbMining();

    Request<?, HpbHashrate> hpbHashrate();

    Request<?, HpbGasPrice> hpbGasPrice();

    Request<?, HpbAccounts> hpbAccounts();

    Request<?, HpbBlockNumber> hpbBlockNumber();

    Request<?, HpbGetBalance> hpbGetBalance(
            String address, DefaultBlockParameter defaultBlockParameter);

    Request<?, HpbGetStorageAt> hpbGetStorageAt(
            String address, BigInteger position,
            DefaultBlockParameter defaultBlockParameter);

    Request<?, HpbGetTransactionCount> hpbGetTransactionCount(
            String address, DefaultBlockParameter defaultBlockParameter);

    Request<?, HpbGetBlockTransactionCountByHash> hpbGetBlockTransactionCountByHash(
            String blockHash);

    Request<?, HpbGetBlockTransactionCountByNumber> hpbGetBlockTransactionCountByNumber(
            DefaultBlockParameter defaultBlockParameter);

    Request<?, HpbGetUncleCountByBlockHash> hpbGetUncleCountByBlockHash(String blockHash);

    Request<?, HpbGetUncleCountByBlockNumber> hpbGetUncleCountByBlockNumber(
            DefaultBlockParameter defaultBlockParameter);

    Request<?, HpbGetCode> hpbGetCode(String address, DefaultBlockParameter defaultBlockParameter);

    Request<?, HpbSign> hpbSign(String address, String sha3HashOfDataToSign);

    Request<?, io.hpb.web3.protocol.core.methods.response.HpbSendTransaction> hpbSendTransaction(
            io.hpb.web3.protocol.core.methods.request.Transaction transaction);

    Request<?, io.hpb.web3.protocol.core.methods.response.HpbSendTransaction> hpbSendRawTransaction(
            String signedTransactionData);

    Request<?, io.hpb.web3.protocol.core.methods.response.HpbCall> hpbCall(
            io.hpb.web3.protocol.core.methods.request.Transaction transaction,
            DefaultBlockParameter defaultBlockParameter);

    Request<?, HpbEstimateGas> hpbEstimateGas(
            io.hpb.web3.protocol.core.methods.request.Transaction transaction);

    Request<?, HpbBlock> hpbGetBlockByHash(String blockHash, boolean returnFullTransactionObjects);

    Request<?, HpbBlock> hpbGetBlockByNumber(
            DefaultBlockParameter defaultBlockParameter,
            boolean returnFullTransactionObjects);

    Request<?, HpbTransaction> hpbGetTransactionByHash(String transactionHash);

    Request<?, HpbTransaction> hpbGetTransactionByBlockHashAndIndex(
            String blockHash, BigInteger transactionIndex);

    Request<?, HpbTransaction> hpbGetTransactionByBlockNumberAndIndex(
            DefaultBlockParameter defaultBlockParameter, BigInteger transactionIndex);

    Request<?, HpbGetTransactionReceipt> hpbGetTransactionReceipt(String transactionHash);

    Request<?, HpbBlock> hpbGetUncleByBlockHashAndIndex(
            String blockHash, BigInteger transactionIndex);

    Request<?, HpbBlock> hpbGetUncleByBlockNumberAndIndex(
            DefaultBlockParameter defaultBlockParameter, BigInteger transactionIndex);

    Request<?, HpbGetCompilers> hpbGetCompilers();

    Request<?, HpbCompileLLL> hpbCompileLLL(String sourceCode);

    Request<?, HpbCompileSolidity> hpbCompileSolidity(String sourceCode);

    Request<?, HpbCompileSerpent> hpbCompileSerpent(String sourceCode);

    Request<?, HpbFilter> hpbNewFilter(io.hpb.web3.protocol.core.methods.request.HpbFilter hpbFilter);

    Request<?, HpbFilter> hpbNewBlockFilter();

    Request<?, HpbFilter> hpbNewPendingTransactionFilter();

    Request<?, HpbUninstallFilter> hpbUninstallFilter(BigInteger filterId);

    Request<?, HpbLog> hpbGetFilterChanges(BigInteger filterId);

    Request<?, HpbLog> hpbGetFilterLogs(BigInteger filterId);

    Request<?, HpbLog> hpbGetLogs(io.hpb.web3.protocol.core.methods.request.HpbFilter hpbFilter);

    Request<?, HpbGetWork> hpbGetWork();

    Request<?, HpbSubmitWork> hpbSubmitWork(String nonce, String headerPowHash, String mixDigest);

    Request<?, HpbSubmitHashrate> hpbSubmitHashrate(String hashrate, String clientId);

    Request<?, DbPutString> dbPutString(String databaseName, String keyName, String stringToStore);

    Request<?, DbGetString> dbGetString(String databaseName, String keyName);

    Request<?, DbPutHex> dbPutHex(String databaseName, String keyName, String dataToStore);

    Request<?, DbGetHex> dbGetHex(String databaseName, String keyName);

    Request<?, io.hpb.web3.protocol.core.methods.response.ShhPost> shhPost(
            io.hpb.web3.protocol.core.methods.request.ShhPost shhPost);

    Request<?, ShhVersion> shhVersion();

    Request<?, ShhNewIdentity> shhNewIdentity();

    Request<?, ShhHasIdentity> shhHasIdentity(String identityAddress);

    Request<?, ShhNewGroup> shhNewGroup();

    Request<?, ShhAddToGroup> shhAddToGroup(String identityAddress);

    Request<?, ShhNewFilter> shhNewFilter(ShhFilter shhFilter);

    Request<?, ShhUninstallFilter> shhUninstallFilter(BigInteger filterId);

    Request<?, ShhMessages> shhGetFilterChanges(BigInteger filterId);

    Request<?, ShhMessages> shhGetMessages(BigInteger filterId);
}
