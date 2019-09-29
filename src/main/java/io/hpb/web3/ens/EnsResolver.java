package io.hpb.web3.ens;
import io.hpb.web3.crypto.Keys;
import io.hpb.web3.crypto.WalletUtils;
import io.hpb.web3.ens.contracts.generated.ENS;
import io.hpb.web3.ens.contracts.generated.PublicResolver;
import io.hpb.web3.protocol.Web3;
import io.hpb.web3.protocol.core.DefaultBlockParameterName;
import io.hpb.web3.protocol.core.methods.response.HpbBlock;
import io.hpb.web3.protocol.core.methods.response.HpbSyncing;
import io.hpb.web3.protocol.core.methods.response.NetVersion;
import io.hpb.web3.tx.ClientTransactionManager;
import io.hpb.web3.tx.TransactionManager;
import io.hpb.web3.tx.gas.DefaultGasProvider;
import io.hpb.web3.utils.Numeric;
public class EnsResolver {
    public static final long DEFAULT_SYNC_THRESHOLD = 1000 * 60 * 3;
    public static final String REVERSE_NAME_SUFFIX = ".addr.reverse";
    private final Web3 web3;
    private final int addressLength;
    private final TransactionManager transactionManager;
    private long syncThreshold; 
    public EnsResolver(Web3 web3, long syncThreshold, int addressLength) {
        this.web3 = web3;
        transactionManager = new ClientTransactionManager(web3, null); 
        this.syncThreshold = syncThreshold;
        this.addressLength = addressLength;
    }
    public EnsResolver(Web3 web3, long syncThreshold) {
        this(web3, syncThreshold, Keys.ADDRESS_LENGTH_IN_HEX);
    }
    public EnsResolver(Web3 web3) {
        this(web3, DEFAULT_SYNC_THRESHOLD);
    }
    public void setSyncThreshold(long syncThreshold) {
        this.syncThreshold = syncThreshold;
    }
    public long getSyncThreshold() {
        return syncThreshold;
    }
    protected PublicResolver obtainPublicResolver(String ensName) {
        if (isValidEnsName(ensName, addressLength)) {
            try {
                if (!isSynced()) {
                    throw new EnsResolutionException("Node is not currently synced");
                } else {
                    return lookupResolver(ensName);
                }
            } catch (Exception e) {
                throw new EnsResolutionException("Unable to determine sync status of node", e);
            }
        } else {
            throw new EnsResolutionException("EnsName is invalid: " + ensName);
        }
    }
    public String resolve(String contractId) {
        if (isValidEnsName(contractId, addressLength)) {
            PublicResolver resolver = obtainPublicResolver(contractId);
            byte[] nameHash = NameHash.nameHashAsBytes(contractId);
            String contractAddress = null;
            try {
                contractAddress = resolver.addr(nameHash).send();
            } catch (Exception e) {
                throw new RuntimeException("Unable to execute Hpb request", e);
            }
            if (!WalletUtils.isValidAddress(contractAddress)) {
                throw new RuntimeException("Unable to resolve address for name: " + contractId);
            } else {
                return contractAddress;
            }
        } else {
            return contractId;
        }
    }
    public String reverseResolve(String address) {
        if (WalletUtils.isValidAddress(address, addressLength)) {
            String reverseName = Numeric.cleanHexPrefix(address) + REVERSE_NAME_SUFFIX;
            PublicResolver resolver = obtainPublicResolver(reverseName);
            byte[] nameHash = NameHash.nameHashAsBytes(reverseName);
            String name;
            try {
                name = resolver.name(nameHash).send();
            } catch (Exception e) {
                throw new RuntimeException("Unable to execute Hpb request", e);
            }
            if (!isValidEnsName(name, addressLength)) {
                throw new RuntimeException("Unable to resolve name for address: " + address);
            } else {
                return name;
            }
        } else {
            throw new EnsResolutionException("Address is invalid: " + address);
        }
    }
    private PublicResolver lookupResolver(String ensName) throws Exception {
        NetVersion netVersion = web3.netVersion().send();
        String registryContract = Contracts.resolveRegistryContract(netVersion.getNetVersion());
        ENS ensRegistry =
                ENS.load(
                        registryContract,
                        web3,
                        transactionManager,
                        DefaultGasProvider.GAS_PRICE,
                        DefaultGasProvider.GAS_LIMIT);
        byte[] nameHash = NameHash.nameHashAsBytes(ensName);
        String resolverAddress = ensRegistry.resolver(nameHash).send();
        return PublicResolver.load(
                resolverAddress,
                web3,
                transactionManager,
                DefaultGasProvider.GAS_PRICE,
                DefaultGasProvider.GAS_LIMIT);
    }
    boolean isSynced() throws Exception {
        HpbSyncing hpbSyncing = web3.hpbSyncing().send();
        if (hpbSyncing.isSyncing()) {
            return false;
        } else {
            HpbBlock hpbBlock =
                    web3.hpbGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send();
            long timestamp = hpbBlock.getBlock().getTimestamp().longValueExact() * 1000;
            return System.currentTimeMillis() - syncThreshold < timestamp;
        }
    }
    public static boolean isValidEnsName(String input) {
        return isValidEnsName(input, Keys.ADDRESS_LENGTH_IN_HEX);
    }
    public static boolean isValidEnsName(String input, int addressLength) {
        return input != null 
                && (input.contains(".") || !WalletUtils.isValidAddress(input, addressLength));
    }
}
