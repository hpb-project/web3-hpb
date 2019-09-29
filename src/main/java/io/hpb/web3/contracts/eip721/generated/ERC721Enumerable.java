package io.hpb.web3.contracts.eip721.generated;
import java.math.BigInteger;
import java.util.Arrays;

import io.hpb.web3.abi.TypeReference;
import io.hpb.web3.abi.datatypes.Function;
import io.hpb.web3.abi.datatypes.Type;
import io.hpb.web3.abi.datatypes.generated.Uint256;
import io.hpb.web3.crypto.Credentials;
import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.RemoteCall;
import io.hpb.web3.tx.Contract;
import io.hpb.web3.tx.TransactionManager;
import io.hpb.web3.tx.gas.ContractGasProvider;
public class ERC721Enumerable extends Contract {
    private static final String BINARY = "Bin file was not provided";
    public static final String FUNC_TOTALSUPPLY = "totalSupply";
    public static final String FUNC_TOKENOFOWNERBYINDEX = "tokenOfOwnerByIndex";
    public static final String FUNC_TOKENBYINDEX = "tokenByIndex";
    @Deprecated
    protected ERC721Enumerable(String contractAddress, Web3 web3, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3, credentials, gasPrice, gasLimit);
    }
    protected ERC721Enumerable(String contractAddress, Web3 web3, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3, credentials, contractGasProvider);
    }
    @Deprecated
    protected ERC721Enumerable(String contractAddress, Web3 web3, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3, transactionManager, gasPrice, gasLimit);
    }
    protected ERC721Enumerable(String contractAddress, Web3 web3, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3, transactionManager, contractGasProvider);
    }
    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }
    public RemoteCall<BigInteger> tokenOfOwnerByIndex(String _owner, BigInteger _index) {
        final Function function = new Function(FUNC_TOKENOFOWNERBYINDEX, 
                Arrays.<Type>asList(new io.hpb.web3.abi.datatypes.Address(_owner), 
                new io.hpb.web3.abi.datatypes.generated.Uint256(_index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }
    public RemoteCall<BigInteger> tokenByIndex(BigInteger _index) {
        final Function function = new Function(FUNC_TOKENBYINDEX, 
                Arrays.<Type>asList(new io.hpb.web3.abi.datatypes.generated.Uint256(_index)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }
    @Deprecated
    public static ERC721Enumerable load(String contractAddress, Web3 web3, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ERC721Enumerable(contractAddress, web3, credentials, gasPrice, gasLimit);
    }
    @Deprecated
    public static ERC721Enumerable load(String contractAddress, Web3 web3, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ERC721Enumerable(contractAddress, web3, transactionManager, gasPrice, gasLimit);
    }
    public static ERC721Enumerable load(String contractAddress, Web3 web3, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ERC721Enumerable(contractAddress, web3, credentials, contractGasProvider);
    }
    public static ERC721Enumerable load(String contractAddress, Web3 web3, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ERC721Enumerable(contractAddress, web3, transactionManager, contractGasProvider);
    }
}
