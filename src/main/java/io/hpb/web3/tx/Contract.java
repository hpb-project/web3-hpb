package io.hpb.web3.tx;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import io.hpb.web3.abi.EventEncoder;
import io.hpb.web3.abi.EventValues;
import io.hpb.web3.abi.FunctionEncoder;
import io.hpb.web3.abi.FunctionReturnDecoder;
import io.hpb.web3.abi.TypeReference;
import io.hpb.web3.abi.datatypes.Address;
import io.hpb.web3.abi.datatypes.Event;
import io.hpb.web3.abi.datatypes.Function;
import io.hpb.web3.abi.datatypes.Type;
import io.hpb.web3.crypto.Credentials;
import io.hpb.web3.ens.EnsResolver;
import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.DefaultBlockParameter;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.RemoteCall;
import io.hpb.web3.protocol.core.RemoteFunctionCall;
import io.hpb.web3.protocol.core.methods.response.HpbGetCode;
import io.hpb.web3.protocol.core.methods.response.Log;
import io.hpb.web3.protocol.core.methods.response.TransactionReceipt;
import io.hpb.web3.protocol.exceptions.TransactionException;
import io.hpb.web3.tx.exceptions.ContractCallException;
import io.hpb.web3.tx.gas.ContractGasProvider;
import io.hpb.web3.tx.gas.StaticGasProvider;
import io.hpb.web3.utils.Numeric;
@SuppressWarnings("WeakerAccess")
public abstract class Contract extends ManagedTransaction {
    public static final BigInteger GAS_LIMIT = BigInteger.valueOf(4_300_000);
    public static final String BIN_NOT_PROVIDED = "Bin file was not provided";
    public static final String FUNC_DEPLOY = "deploy";
    protected final String contractBinary;
    protected String contractAddress;
    protected ContractGasProvider gasProvider;
    protected TransactionReceipt transactionReceipt;
    protected Map<String, String> deployedAddresses;
    protected DefaultBlockParameter defaultBlockParameter = DefaultBlockParameterName.LATEST;
    protected Contract(
            String contractBinary,
            String contractAddress,
            Web3 web3,
            TransactionManager transactionManager,
            ContractGasProvider gasProvider) {
        this(
                new EnsResolver(web3),
                contractBinary,
                contractAddress,
                web3,
                transactionManager,
                gasProvider);
    }
    protected Contract(
            EnsResolver ensResolver,
            String contractBinary,
            String contractAddress,
            Web3 web3,
            TransactionManager transactionManager,
            ContractGasProvider gasProvider) {
        super(ensResolver, web3, transactionManager);
        this.contractAddress = resolveContractAddress(contractAddress);
        this.contractBinary = contractBinary;
        this.gasProvider = gasProvider;
    }
    protected Contract(
            String contractBinary,
            String contractAddress,
            Web3 web3,
            Credentials credentials,
            ContractGasProvider gasProvider) {
        this(
                new EnsResolver(web3),
                contractBinary,
                contractAddress,
                web3,
                new RawTransactionManager(web3, credentials),
                gasProvider);
    }
    @Deprecated
    protected Contract(
            String contractBinary,
            String contractAddress,
            Web3 web3,
            TransactionManager transactionManager,
            BigInteger gasPrice,
            BigInteger gasLimit) {
        this(
                new EnsResolver(web3),
                contractBinary,
                contractAddress,
                web3,
                transactionManager,
                new StaticGasProvider(gasPrice, gasLimit));
    }
    @Deprecated
    protected Contract(
            String contractBinary,
            String contractAddress,
            Web3 web3,
            Credentials credentials,
            BigInteger gasPrice,
            BigInteger gasLimit) {
        this(
                contractBinary,
                contractAddress,
                web3,
                new RawTransactionManager(web3, credentials),
                gasPrice,
                gasLimit);
    }
    @Deprecated
    protected Contract(
            String contractAddress,
            Web3 web3,
            TransactionManager transactionManager,
            BigInteger gasPrice,
            BigInteger gasLimit) {
        this("", contractAddress, web3, transactionManager, gasPrice, gasLimit);
    }
    @Deprecated
    protected Contract(
            String contractAddress,
            Web3 web3,
            Credentials credentials,
            BigInteger gasPrice,
            BigInteger gasLimit) {
        this(
                "",
                contractAddress,
                web3,
                new RawTransactionManager(web3, credentials),
                gasPrice,
                gasLimit);
    }
    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }
    public String getContractAddress() {
        return contractAddress;
    }
    public void setTransactionReceipt(TransactionReceipt transactionReceipt) {
        this.transactionReceipt = transactionReceipt;
    }
    public String getContractBinary() {
        return contractBinary;
    }
    public void setGasProvider(ContractGasProvider gasProvider) {
        this.gasProvider = gasProvider;
    }
    public void setGasPrice(BigInteger newPrice) {
        this.gasProvider = new StaticGasProvider(newPrice, gasProvider.getGasLimit());
    }
    public BigInteger getGasPrice() {
        return gasProvider.getGasPrice();
    }
    public boolean isValid() throws IOException {
        if (contractBinary.equals(BIN_NOT_PROVIDED)) {
            throw new UnsupportedOperationException(
                    "Contract binary not present in contract wrapper, "
                            + "please generate your wrapper using -abiFile=<file>");
        }
        if (contractAddress.equals("")) {
            throw new UnsupportedOperationException(
                    "Contract binary not present, you will need to regenerate your smart "
                            + "contract wrapper with web3 v2.2.0+");
        }
        HpbGetCode hpbGetCode =
                web3.hpbGetCode(contractAddress, DefaultBlockParameterName.LATEST).send();
        if (hpbGetCode.hasError()) {
            return false;
        }
        String code = Numeric.cleanHexPrefix(hpbGetCode.getCode());
        int metadataIndex = code.indexOf("a165627a7a72305820");
        if (metadataIndex != -1) {
            code = code.substring(0, metadataIndex);
        }
        return !code.isEmpty() && contractBinary.contains(code);
    }
    public Optional<TransactionReceipt> getTransactionReceipt() {
        return Optional.ofNullable(transactionReceipt);
    }
    public void setDefaultBlockParameter(DefaultBlockParameter defaultBlockParameter) {
        this.defaultBlockParameter = defaultBlockParameter;
    }
    private List<Type> executeCall(Function function) throws IOException {
        String encodedFunction = FunctionEncoder.encode(function);
        String value = call(contractAddress, encodedFunction, defaultBlockParameter);
        return FunctionReturnDecoder.decode(value, function.getOutputParameters());
    }
    @SuppressWarnings("unchecked")
    protected <T extends Type> T executeCallSingleValueReturn(Function function)
            throws IOException {
        List<Type> values = executeCall(function);
        if (!values.isEmpty()) {
            return (T) values.get(0);
        } else {
            return null;
        }
    }
    @SuppressWarnings("unchecked")
    protected <T extends Type, R> R executeCallSingleValueReturn(
            Function function, Class<R> returnType) throws IOException {
        T result = executeCallSingleValueReturn(function);
        if (result == null) {
            throw new ContractCallException("Empty value (0x) returned from contract");
        }
        Object value = result.getValue();
        if (returnType.isAssignableFrom(value.getClass())) {
            return (R) value;
        } else if (result.getClass().equals(Address.class) && returnType.equals(String.class)) {
            return (R) result.toString(); 
        } else {
            throw new ContractCallException(
                    "Unable to convert response: "
                            + value
                            + " to expected type: "
                            + returnType.getSimpleName());
        }
    }
    protected List<Type> executeCallMultipleValueReturn(Function function) throws IOException {
        return executeCall(function);
    }
    protected TransactionReceipt executeTransaction(Function function)
            throws IOException, TransactionException {
        return executeTransaction(function, BigInteger.ZERO);
    }
    private TransactionReceipt executeTransaction(Function function, BigInteger weiValue)
            throws IOException, TransactionException {
        return executeTransaction(FunctionEncoder.encode(function), weiValue, function.getName());
    }
    TransactionReceipt executeTransaction(String data, BigInteger weiValue, String funcName)
            throws TransactionException, IOException {
        return executeTransaction(data, weiValue, funcName, false);
    }
    TransactionReceipt executeTransaction(
            String data, BigInteger weiValue, String funcName, boolean constructor)
            throws TransactionException, IOException {
        TransactionReceipt receipt =
                send(
                        contractAddress,
                        data,
                        weiValue,
                        gasProvider.getGasPrice(funcName),
                        gasProvider.getGasLimit(funcName),
                        constructor);
        if (!receipt.isStatusOK()) {
            throw new TransactionException(
                    String.format(
                            "Transaction has failed with status: %s. "
                                    + "Gas used: %d. (not-enough gas?)",
                            receipt.getStatus(), receipt.getGasUsed()));
        }
        return receipt;
    }
    protected <T extends Type> RemoteFunctionCall<T> executeRemoteCallSingleValueReturn(
            Function function) {
        return new RemoteFunctionCall<>(function, () -> executeCallSingleValueReturn(function));
    }
    protected <T> RemoteFunctionCall<T> executeRemoteCallSingleValueReturn(
            Function function, Class<T> returnType) {
        return new RemoteFunctionCall<>(
                function, () -> executeCallSingleValueReturn(function, returnType));
    }
    protected RemoteFunctionCall<List<Type>> executeRemoteCallMultipleValueReturn(
            Function function) {
        return new RemoteFunctionCall<>(function, () -> executeCallMultipleValueReturn(function));
    }
    protected RemoteFunctionCall<TransactionReceipt> executeRemoteCallTransaction(
            Function function) {
        return new RemoteFunctionCall<>(function, () -> executeTransaction(function));
    }
    protected RemoteFunctionCall<TransactionReceipt> executeRemoteCallTransaction(
            Function function, BigInteger weiValue) {
        return new RemoteFunctionCall<>(function, () -> executeTransaction(function, weiValue));
    }
    private static <T extends Contract> T create(
            T contract, String binary, String encodedConstructor, BigInteger value)
            throws IOException, TransactionException {
        TransactionReceipt transactionReceipt =
                contract.executeTransaction(binary + encodedConstructor, value, FUNC_DEPLOY, true);
        String contractAddress = transactionReceipt.getContractAddress();
        if (contractAddress == null) {
            throw new RuntimeException("Empty contract address returned");
        }
        contract.setContractAddress(contractAddress);
        contract.setTransactionReceipt(transactionReceipt);
        return contract;
    }
    protected static <T extends Contract> T deploy(
            Class<T> type,
            Web3 web3,
            Credentials credentials,
            ContractGasProvider contractGasProvider,
            String binary,
            String encodedConstructor,
            BigInteger value)
            throws RuntimeException, TransactionException {
        try {
            Constructor<T> constructor =
                    type.getDeclaredConstructor(
                            String.class,
                            Web3.class,
                            Credentials.class,
                            ContractGasProvider.class);
            constructor.setAccessible(true);
            T contract = constructor.newInstance(null, web3, credentials, contractGasProvider);
            return create(contract, binary, encodedConstructor, value);
        } catch (TransactionException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    protected static <T extends Contract> T deploy(
            Class<T> type,
            Web3 web3,
            TransactionManager transactionManager,
            ContractGasProvider contractGasProvider,
            String binary,
            String encodedConstructor,
            BigInteger value)
            throws RuntimeException, TransactionException {
        try {
            Constructor<T> constructor =
                    type.getDeclaredConstructor(
                            String.class,
                            Web3.class,
                            TransactionManager.class,
                            ContractGasProvider.class);
            constructor.setAccessible(true);
            T contract =
                    constructor.newInstance(null, web3, transactionManager, contractGasProvider);
            return create(contract, binary, encodedConstructor, value);
        } catch (TransactionException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Deprecated
    protected static <T extends Contract> T deploy(
            Class<T> type,
            Web3 web3,
            Credentials credentials,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String binary,
            String encodedConstructor,
            BigInteger value)
            throws RuntimeException, TransactionException {
        return deploy(
                type,
                web3,
                credentials,
                new StaticGasProvider(gasPrice, gasLimit),
                binary,
                encodedConstructor,
                value);
    }
    @Deprecated
    protected static <T extends Contract> T deploy(
            Class<T> type,
            Web3 web3,
            TransactionManager transactionManager,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String binary,
            String encodedConstructor,
            BigInteger value)
            throws RuntimeException, TransactionException {
        return deploy(
                type,
                web3,
                transactionManager,
                new StaticGasProvider(gasPrice, gasLimit),
                binary,
                encodedConstructor,
                value);
    }
    public static <T extends Contract> RemoteCall<T> deployRemoteCall(
            Class<T> type,
            Web3 web3,
            Credentials credentials,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String binary,
            String encodedConstructor,
            BigInteger value) {
        return new RemoteCall<>(
                () ->
                        deploy(
                                type,
                                web3,
                                credentials,
                                gasPrice,
                                gasLimit,
                                binary,
                                encodedConstructor,
                                value));
    }
    public static <T extends Contract> RemoteCall<T> deployRemoteCall(
            Class<T> type,
            Web3 web3,
            Credentials credentials,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String binary,
            String encodedConstructor) {
        return deployRemoteCall(
                type,
                web3,
                credentials,
                gasPrice,
                gasLimit,
                binary,
                encodedConstructor,
                BigInteger.ZERO);
    }
    public static <T extends Contract> RemoteCall<T> deployRemoteCall(
            Class<T> type,
            Web3 web3,
            Credentials credentials,
            ContractGasProvider contractGasProvider,
            String binary,
            String encodedConstructor,
            BigInteger value) {
        return new RemoteCall<>(
                () ->
                        deploy(
                                type,
                                web3,
                                credentials,
                                contractGasProvider,
                                binary,
                                encodedConstructor,
                                value));
    }
    public static <T extends Contract> RemoteCall<T> deployRemoteCall(
            Class<T> type,
            Web3 web3,
            Credentials credentials,
            ContractGasProvider contractGasProvider,
            String binary,
            String encodedConstructor) {
        return new RemoteCall<>(
                () ->
                        deploy(
                                type,
                                web3,
                                credentials,
                                contractGasProvider,
                                binary,
                                encodedConstructor,
                                BigInteger.ZERO));
    }
    public static <T extends Contract> RemoteCall<T> deployRemoteCall(
            Class<T> type,
            Web3 web3,
            TransactionManager transactionManager,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String binary,
            String encodedConstructor,
            BigInteger value) {
        return new RemoteCall<>(
                () ->
                        deploy(
                                type,
                                web3,
                                transactionManager,
                                gasPrice,
                                gasLimit,
                                binary,
                                encodedConstructor,
                                value));
    }
    public static <T extends Contract> RemoteCall<T> deployRemoteCall(
            Class<T> type,
            Web3 web3,
            TransactionManager transactionManager,
            BigInteger gasPrice,
            BigInteger gasLimit,
            String binary,
            String encodedConstructor) {
        return deployRemoteCall(
                type,
                web3,
                transactionManager,
                gasPrice,
                gasLimit,
                binary,
                encodedConstructor,
                BigInteger.ZERO);
    }
    public static <T extends Contract> RemoteCall<T> deployRemoteCall(
            Class<T> type,
            Web3 web3,
            TransactionManager transactionManager,
            ContractGasProvider contractGasProvider,
            String binary,
            String encodedConstructor,
            BigInteger value) {
        return new RemoteCall<>(
                () ->
                        deploy(
                                type,
                                web3,
                                transactionManager,
                                contractGasProvider,
                                binary,
                                encodedConstructor,
                                value));
    }
    public static <T extends Contract> RemoteCall<T> deployRemoteCall(
            Class<T> type,
            Web3 web3,
            TransactionManager transactionManager,
            ContractGasProvider contractGasProvider,
            String binary,
            String encodedConstructor) {
        return new RemoteCall<>(
                () ->
                        deploy(
                                type,
                                web3,
                                transactionManager,
                                contractGasProvider,
                                binary,
                                encodedConstructor,
                                BigInteger.ZERO));
    }
    public static EventValues staticExtractEventParameters(Event event, Log log) {
        final List<String> topics = log.getTopics();
        String encodedEventSignature = EventEncoder.encode(event);
        if (topics == null || topics.size() == 0 || !topics.get(0).equals(encodedEventSignature)) {
            return null;
        }
        List<Type> indexedValues = new ArrayList<>();
        List<Type> nonIndexedValues =
                FunctionReturnDecoder.decode(log.getData(), event.getNonIndexedParameters());
        List<TypeReference<Type>> indexedParameters = event.getIndexedParameters();
        for (int i = 0; i < indexedParameters.size(); i++) {
            Type value =
                    FunctionReturnDecoder.decodeIndexedValue(
                            topics.get(i + 1), indexedParameters.get(i));
            indexedValues.add(value);
        }
        return new EventValues(indexedValues, nonIndexedValues);
    }
    protected String resolveContractAddress(String contractAddress) {
        return ensResolver.resolve(contractAddress);
    }
    protected EventValues extractEventParameters(Event event, Log log) {
        return staticExtractEventParameters(event, log);
    }
    protected List<EventValues> extractEventParameters(
            Event event, TransactionReceipt transactionReceipt) {
        return transactionReceipt.getLogs().stream()
                .map(log -> extractEventParameters(event, log))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    protected EventValuesWithLog extractEventParametersWithLog(Event event, Log log) {
        return staticExtractEventParametersWithLog(event, log);
    }
    protected static EventValuesWithLog staticExtractEventParametersWithLog(Event event, Log log) {
        final EventValues eventValues = staticExtractEventParameters(event, log);
        return (eventValues == null) ? null : new EventValuesWithLog(eventValues, log);
    }
    protected List<EventValuesWithLog> extractEventParametersWithLog(
            Event event, TransactionReceipt transactionReceipt) {
        return transactionReceipt.getLogs().stream()
                .map(log -> extractEventParametersWithLog(event, log))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    protected String getStaticDeployedAddress(String networkId) {
        return null;
    }
    public final void setDeployedAddress(String networkId, String address) {
        if (deployedAddresses == null) {
            deployedAddresses = new HashMap<>();
        }
        deployedAddresses.put(networkId, address);
    }
    public final String getDeployedAddress(String networkId) {
        String addr = null;
        if (deployedAddresses != null) {
            addr = deployedAddresses.get(networkId);
        }
        return addr == null ? getStaticDeployedAddress(networkId) : addr;
    }
    public static class EventValuesWithLog {
        private final EventValues eventValues;
        private final Log log;
        private EventValuesWithLog(EventValues eventValues, Log log) {
            this.eventValues = eventValues;
            this.log = log;
        }
        public List<Type> getIndexedValues() {
            return eventValues.getIndexedValues();
        }
        public List<Type> getNonIndexedValues() {
            return eventValues.getNonIndexedValues();
        }
        public Log getLog() {
            return log;
        }
    }
    @SuppressWarnings("unchecked")
    protected static <S extends Type, T> List<T> convertToNative(List<S> arr) {
        List<T> out = new ArrayList<>();
        for (final S s : arr) {
            out.add((T) s.getValue());
        }
        return out;
    }
}
