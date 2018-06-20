package com.hpb.web3.protocol.parity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hpb.web3.crypto.WalletFile;
import com.hpb.web3.protocol.Web3Service;
import com.hpb.web3.protocol.admin.JsonRpc2_0Admin;
import com.hpb.web3.protocol.admin.methods.response.BooleanResponse;
import com.hpb.web3.protocol.admin.methods.response.NewAccountIdentifier;
import com.hpb.web3.protocol.admin.methods.response.PersonalSign;
import com.hpb.web3.protocol.core.DefaultBlockParameter;
import com.hpb.web3.protocol.core.Request;
import com.hpb.web3.protocol.core.methods.request.Transaction;
import com.hpb.web3.protocol.parity.methods.request.Derivation;
import com.hpb.web3.protocol.parity.methods.request.TraceFilter;
import com.hpb.web3.protocol.parity.methods.response.ParityAddressesResponse;
import com.hpb.web3.protocol.parity.methods.response.ParityAllAccountsInfo;
import com.hpb.web3.protocol.parity.methods.response.ParityDefaultAddressResponse;
import com.hpb.web3.protocol.parity.methods.response.ParityDeriveAddress;
import com.hpb.web3.protocol.parity.methods.response.ParityExportAccount;
import com.hpb.web3.protocol.parity.methods.response.ParityFullTraceResponse;
import com.hpb.web3.protocol.parity.methods.response.ParityListRecentDapps;
import com.hpb.web3.protocol.parity.methods.response.ParityTraceGet;
import com.hpb.web3.protocol.parity.methods.response.ParityTracesResponse;
import com.hpb.web3.utils.Numeric;


public class JsonRpc2_0Parity extends JsonRpc2_0Admin implements Parity {

    public JsonRpc2_0Parity(Web3Service web3Service) {
        super(web3Service);
    }
    
    @Override
    public Request<?, ParityAllAccountsInfo> parityAllAccountsInfo() {
        return new Request<>(
                "parity_allAccountsInfo",
                Collections.<String>emptyList(),
                web3Service,
                ParityAllAccountsInfo.class);
    }

    @Override
    public Request<?, BooleanResponse> parityChangePassword(
            String accountId, String oldPassword, String newPassword) {
        return new Request<>(
                "parity_changePassword",
                Arrays.asList(accountId, oldPassword, newPassword),
                web3Service,
                BooleanResponse.class);
    }

    @Override
    public Request<?, ParityDeriveAddress> parityDeriveAddressHash(
            String accountId, String password, Derivation hashType, boolean toSave) {
        return new Request<>(
                "parity_deriveAddressHash",
                Arrays.asList(accountId, password, hashType, toSave),
                web3Service,
                ParityDeriveAddress.class);
    }

    @Override
    public Request<?, ParityDeriveAddress> parityDeriveAddressIndex(
            String accountId, String password,
            List<Derivation> indicesType, boolean toSave) {
        return new Request<>(
                "parity_deriveAddressIndex",
                Arrays.asList(accountId, password, indicesType, toSave),
                web3Service,
                ParityDeriveAddress.class);
    }

    @Override
    public Request<?, ParityExportAccount> parityExportAccount(
            String accountId, String password) {
        return new Request<>(
                "parity_exportAccount",
                Arrays.asList(accountId, password),
                web3Service,
                ParityExportAccount.class);
    }

    @Override
    public Request<?, ParityAddressesResponse> parityGetDappAddresses(String dAppId) {
        return new Request<>(
                "parity_getDappAddresses",
                Arrays.asList(dAppId),
                web3Service,
                ParityAddressesResponse.class);
    }

    @Override
    public Request<?, ParityDefaultAddressResponse> parityGetDappDefaultAddress(String dAppId) {
        return new Request<>(
                "parity_getDappDefaultAddress",
                Arrays.asList(dAppId),
                web3Service,
                ParityDefaultAddressResponse.class);
    }

    @Override
    public Request<?, ParityAddressesResponse> parityGetNewDappsAddresses() {
        return new Request<>(
                "parity_getNewDappsAddresses",
                Collections.<String>emptyList(),
                web3Service,
                ParityAddressesResponse.class);
    }

    @Override
    public Request<?, ParityDefaultAddressResponse> parityGetNewDappsDefaultAddress() {
        return new Request<>(
                "parity_getNewDappsDefaultAddress",
                Collections.<String>emptyList(),
                web3Service,
                ParityDefaultAddressResponse.class);
    }

    @Override
    public Request<?, ParityAddressesResponse> parityImportGhpbAccounts(
            ArrayList<String> ghpbAddresses) {
        return new Request<>(
                "parity_importGhpbAccounts",
                ghpbAddresses,
                web3Service,
                ParityAddressesResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> parityKillAccount(String accountId, String password) {
        return new Request<>(
                "parity_killAccount",
                Arrays.asList(accountId, password),
                web3Service,
                BooleanResponse.class);
    }

    @Override
    public Request<?, ParityAddressesResponse> parityListGhpbAccounts() {
        return new Request<>(
                "parity_listGhpbAccounts",
                Collections.<String>emptyList(),
                web3Service,
                ParityAddressesResponse.class);
    }

    @Override
    public Request<?, ParityListRecentDapps> parityListRecentDapps() {
        return new Request<>(
                "parity_listRecentDapps",
                Collections.<String>emptyList(),
                web3Service,
                ParityListRecentDapps.class);
    }

    @Override
    public Request<?, NewAccountIdentifier> parityNewAccountFromPhrase(
            String phrase, String password) {
        return new Request<>(
                "parity_newAccountFromPhrase",
                Arrays.asList(phrase, password),
                web3Service,
                NewAccountIdentifier.class);
    }

    @Override
    public Request<?, NewAccountIdentifier> parityNewAccountFromSecret(
            String secret, String password) {
        return new Request<>(
                "parity_newAccountFromSecret",
                Arrays.asList(secret, password),
                web3Service,
                NewAccountIdentifier.class);
    }

    @Override
    public Request<?, NewAccountIdentifier> parityNewAccountFromWallet(
            WalletFile walletFile, String password) {
        return new Request<>(
                "parity_newAccountFromWallet",
                Arrays.asList(walletFile, password),
                web3Service,
                NewAccountIdentifier.class);
    }

    @Override
    public Request<?, BooleanResponse> parityRemoveAddress(String accountId) {
        return new Request<>(
                "parity_RemoveAddress",
                Arrays.asList(accountId),
                web3Service,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetAccountMeta(
            String accountId, Map<String, Object> metadata) {
        return new Request<>(
                "parity_setAccountMeta",
                Arrays.asList(accountId, metadata),
                web3Service,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetAccountName(
            String address, String name) {
        return new Request<>(
                "parity_setAccountName",
                Arrays.asList(address, name),
                web3Service,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetDappAddresses(
            String dAppId, ArrayList<String> availableAccountIds) {
        return new Request<>(
                "parity_setDappAddresses",
                Arrays.asList(dAppId, availableAccountIds),
                web3Service,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetDappDefaultAddress(
            String dAppId, String defaultAddress) {
        return new Request<>(
                "parity_setDappDefaultAddress",
                Arrays.asList(dAppId, defaultAddress),
                web3Service,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetNewDappsAddresses(
            ArrayList<String> availableAccountIds) {
        return new Request<>(
                "parity_setNewDappsAddresses",
                Arrays.asList(availableAccountIds),
                web3Service,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> paritySetNewDappsDefaultAddress(String defaultAddress) {
        return new Request<>(
                "parity_setNewDappsDefaultAddress",
                Arrays.asList(defaultAddress),
                web3Service,
                BooleanResponse.class);
    }

    @Override
    public Request<?, BooleanResponse> parityTestPassword(String accountId, String password) {
        return new Request<>(
                "parity_testPassword",
                Arrays.asList(accountId, password),
                web3Service,
                BooleanResponse.class);
    }

    @Override
    public Request<?, PersonalSign> paritySignMessage(
            String accountId, String password, String hexMessage) {
        return new Request<>(
                "parity_signMessage",
                Arrays.asList(accountId,password,hexMessage),
                web3Service,
                PersonalSign.class);
    }
    
        
    @Override
    public Request<?, ParityFullTraceResponse> traceCall(
            Transaction transaction, List<String> traces, DefaultBlockParameter blockParameter) {
        return new Request<>(
            "trace_call",
            Arrays.asList(transaction, traces, blockParameter),
            web3Service,
            ParityFullTraceResponse.class);
    }
    
    @Override
    public Request<?, ParityFullTraceResponse> traceRawTransaction(
            String data, List<String> traceTypes) {
        return new Request<>(
            "trace_rawTransaction",
            Arrays.asList(data, traceTypes),
            web3Service,
            ParityFullTraceResponse.class);
    }
    
    @Override
    public Request<?, ParityFullTraceResponse> traceReplayTransaction(
            String hash, List<String> traceTypes) {
        return new Request<>(
            "trace_replayTransaction",
            Arrays.asList(hash, traceTypes),
            web3Service,
            ParityFullTraceResponse.class);
    }
    
    @Override
    public Request<?, ParityTracesResponse> traceBlock(DefaultBlockParameter blockParameter) {
        return new Request<>(
            "trace_block",
            Arrays.asList(blockParameter),
            web3Service,
            ParityTracesResponse.class);
    }
    
    @Override
    public Request<?, ParityTracesResponse> traceFilter(TraceFilter traceFilter) {
        return new Request<>(
            "trace_filter",
            Arrays.asList(traceFilter),
            web3Service,
            ParityTracesResponse.class);
    }
    
    @Override
    public Request<?, ParityTraceGet> traceGet(String hash, List<BigInteger> indices) {
        List<String> encodedIndices = indices.stream()
                .map(Numeric::encodeQuantity)
                .collect(Collectors.toList());
        return new Request<>(
            "trace_get",
            Arrays.asList(hash, encodedIndices),
            web3Service,
            ParityTraceGet.class);
    }

    @Override
    public Request<?, ParityTracesResponse> traceTransaction(String hash) {
        return new Request<>(
            "trace_transaction",
            Arrays.asList(hash),
            web3Service,
            ParityTracesResponse.class);
    }
}
