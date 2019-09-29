package io.hpb.web3.contracts.eip721.generated;
import java.math.BigInteger;
import java.util.Arrays;

import io.hpb.web3.abi.TypeReference;
import io.hpb.web3.abi.datatypes.Function;
import io.hpb.web3.abi.datatypes.Type;
import io.hpb.web3.abi.datatypes.Utf8String;
import io.hpb.web3.crypto.Credentials;
import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.RemoteCall;
import io.hpb.web3.tx.Contract;
import io.hpb.web3.tx.TransactionManager;
import io.hpb.web3.tx.gas.ContractGasProvider;
public class ERC721Metadata extends Contract {
    private static final String BINARY = "Bin file was not provided";
    public static final String FUNC_NAME = "name";
    public static final String FUNC_SYMBOL = "symbol";
    public static final String FUNC_TOKENURI = "tokenURI";
    @Deprecated
    protected ERC721Metadata(String contractAddress, Web3 web3, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3, credentials, gasPrice, gasLimit);
    }
    protected ERC721Metadata(String contractAddress, Web3 web3, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3, credentials, contractGasProvider);
    }
    @Deprecated
    protected ERC721Metadata(String contractAddress, Web3 web3, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3, transactionManager, gasPrice, gasLimit);
    }
    protected ERC721Metadata(String contractAddress, Web3 web3, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3, transactionManager, contractGasProvider);
    }
    public RemoteCall<String> name() {
        final Function function = new Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }
    public RemoteCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }
    public RemoteCall<String> tokenURI(BigInteger _tokenId) {
        final Function function = new Function(FUNC_TOKENURI, 
                Arrays.<Type>asList(new io.hpb.web3.abi.datatypes.generated.Uint256(_tokenId)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }
    @Deprecated
    public static ERC721Metadata load(String contractAddress, Web3 web3, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ERC721Metadata(contractAddress, web3, credentials, gasPrice, gasLimit);
    }
    @Deprecated
    public static ERC721Metadata load(String contractAddress, Web3 web3, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ERC721Metadata(contractAddress, web3, transactionManager, gasPrice, gasLimit);
    }
    public static ERC721Metadata load(String contractAddress, Web3 web3, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ERC721Metadata(contractAddress, web3, credentials, contractGasProvider);
    }
    public static ERC721Metadata load(String contractAddress, Web3 web3, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ERC721Metadata(contractAddress, web3, transactionManager, contractGasProvider);
    }
}
