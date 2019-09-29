package io.hpb.web3.contracts.eip165.generated;
import java.math.BigInteger;
import java.util.Arrays;

import io.hpb.web3.abi.TypeReference;
import io.hpb.web3.abi.datatypes.Bool;
import io.hpb.web3.abi.datatypes.Function;
import io.hpb.web3.abi.datatypes.Type;
import io.hpb.web3.crypto.Credentials;
import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.RemoteCall;
import io.hpb.web3.tx.Contract;
import io.hpb.web3.tx.TransactionManager;
import io.hpb.web3.tx.gas.ContractGasProvider;
public class ERC165 extends Contract {
    private static final String BINARY = "Bin file was not provided";
    public static final String FUNC_SUPPORTSINTERFACE = "supportsInterface";
    @Deprecated
    protected ERC165(String contractAddress, Web3 web3, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3, credentials, gasPrice, gasLimit);
    }
    protected ERC165(String contractAddress, Web3 web3, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3, credentials, contractGasProvider);
    }
    @Deprecated
    protected ERC165(String contractAddress, Web3 web3, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3, transactionManager, gasPrice, gasLimit);
    }
    protected ERC165(String contractAddress, Web3 web3, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3, transactionManager, contractGasProvider);
    }
    public RemoteCall<Boolean> supportsInterface(byte[] interfaceID) {
        final Function function = new Function(FUNC_SUPPORTSINTERFACE, 
                Arrays.<Type>asList(new io.hpb.web3.abi.datatypes.generated.Bytes4(interfaceID)), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }
    @Deprecated
    public static ERC165 load(String contractAddress, Web3 web3, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new ERC165(contractAddress, web3, credentials, gasPrice, gasLimit);
    }
    @Deprecated
    public static ERC165 load(String contractAddress, Web3 web3, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new ERC165(contractAddress, web3, transactionManager, gasPrice, gasLimit);
    }
    public static ERC165 load(String contractAddress, Web3 web3, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new ERC165(contractAddress, web3, credentials, contractGasProvider);
    }
    public static ERC165 load(String contractAddress, Web3 web3, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new ERC165(contractAddress, web3, transactionManager, contractGasProvider);
    }
}
