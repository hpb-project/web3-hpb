package com.hpb.web3.protocol.parity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.hpb.web3.crypto.WalletFile;
import com.hpb.web3.protocol.Web3Service;
import com.hpb.web3.protocol.admin.Admin;
import com.hpb.web3.protocol.admin.methods.response.BooleanResponse;
import com.hpb.web3.protocol.admin.methods.response.NewAccountIdentifier;
import com.hpb.web3.protocol.admin.methods.response.PersonalSign;
import com.hpb.web3.protocol.core.Request;
import com.hpb.web3.protocol.parity.methods.request.Derivation;
import com.hpb.web3.protocol.parity.methods.response.ParityAddressesResponse;
import com.hpb.web3.protocol.parity.methods.response.ParityAllAccountsInfo;
import com.hpb.web3.protocol.parity.methods.response.ParityDefaultAddressResponse;
import com.hpb.web3.protocol.parity.methods.response.ParityDeriveAddress;
import com.hpb.web3.protocol.parity.methods.response.ParityExportAccount;
import com.hpb.web3.protocol.parity.methods.response.ParityListRecentDapps;


public interface Parity extends Admin, Trace {
    static Parity build(Web3Service web3Service) {
        return new JsonRpc2_0Parity(web3Service);
    }
    
    Request<?, ParityAllAccountsInfo> parityAllAccountsInfo();
    
    Request<?, BooleanResponse> parityChangePassword(
            String accountId, String oldPassword, String newPassword);
    
    Request<?, ParityDeriveAddress> parityDeriveAddressHash(
            String accountId, String password, Derivation hashType, boolean toSave);
    
    Request<?, ParityDeriveAddress> parityDeriveAddressIndex(
            String accountId, String password, List<Derivation> indicesType, boolean toSave);
    
    Request<?, ParityExportAccount> parityExportAccount(String accountId, String password);
    
    Request<?, ParityAddressesResponse> parityGetDappAddresses(String dAppId);
    
    Request<?, ParityDefaultAddressResponse> parityGetDappDefaultAddress(String dAppId);
    
    Request<?, ParityAddressesResponse> parityGetNewDappsAddresses();
    
    Request<?, ParityDefaultAddressResponse> parityGetNewDappsDefaultAddress();
    
    Request<?, ParityAddressesResponse> parityImportGhpbAccounts(ArrayList<String> ghpbAddresses);
    
    Request<?, BooleanResponse> parityKillAccount(String accountId, String password);
    
    Request<?, ParityAddressesResponse> parityListGhpbAccounts();
    
    Request<?, ParityListRecentDapps> parityListRecentDapps();
    
    Request<?, NewAccountIdentifier> parityNewAccountFromPhrase(String phrase, String password);
    
    Request<?, NewAccountIdentifier> parityNewAccountFromSecret(String secret, String password);
    
    Request<?, NewAccountIdentifier> parityNewAccountFromWallet(
            WalletFile walletFile, String password);
    
    Request<?, BooleanResponse> parityRemoveAddress(String accountId);
    
    Request<?, BooleanResponse> paritySetAccountMeta(
            String accountId, Map<String, Object> metadata);
    
    Request<?, BooleanResponse> paritySetAccountName(String address, String name);
    
    Request<?, BooleanResponse> paritySetDappAddresses(
            String dAppId, ArrayList<String> availableAccountIds);
    
    Request<?, BooleanResponse> paritySetDappDefaultAddress(String dAppId, String defaultAddress);
    
    Request<?, BooleanResponse> paritySetNewDappsAddresses(ArrayList<String> availableAccountIds);
    
    Request<?, BooleanResponse> paritySetNewDappsDefaultAddress(String defaultAddress);
    
    Request<?, BooleanResponse> parityTestPassword(String accountId, String password);
    
    Request<?, PersonalSign> paritySignMessage(
            String accountId, String password, String hexMessage);
}
