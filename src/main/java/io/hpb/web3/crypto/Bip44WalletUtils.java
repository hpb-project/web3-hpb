package io.hpb.web3.crypto;
import static io.hpb.web3.crypto.Bip32ECKeyPair.HARDENED_BIT;

import java.io.File;
import java.io.IOException;
public class Bip44WalletUtils extends WalletUtils {
    public static Bip39Wallet generateBip44Wallet(String password, File destinationDirectory)
            throws CipherException, IOException {
        return generateBip44Wallet(password, destinationDirectory, false);
    }
    public static Bip39Wallet generateBip44Wallet(
            String password, File destinationDirectory, boolean testNet)
            throws CipherException, IOException {
        byte[] initialEntropy = new byte[16];
        SecureRandomUtils.secureRandom().nextBytes(initialEntropy);
        String mnemonic = MnemonicUtils.generateMnemonic(initialEntropy);
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, null);
        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(seed);
        Bip32ECKeyPair bip44Keypair = generateBip44KeyPair(masterKeypair, testNet);
        String walletFile = generateWalletFile(password, bip44Keypair, destinationDirectory, false);
        return new Bip39Wallet(walletFile, mnemonic);
    }
    public static Bip32ECKeyPair generateBip44KeyPair(Bip32ECKeyPair master) {
        return generateBip44KeyPair(master, false);
    }
    public static Bip32ECKeyPair generateBip44KeyPair(Bip32ECKeyPair master, boolean testNet) {
        if (testNet) {
            final int[] path = {44 | HARDENED_BIT, 0 | HARDENED_BIT, 0 | HARDENED_BIT, 0};
            return Bip32ECKeyPair.deriveKeyPair(master, path);
        } else {
            final int[] path = {44 | HARDENED_BIT, 60 | HARDENED_BIT, 0 | HARDENED_BIT, 0};
            return Bip32ECKeyPair.deriveKeyPair(master, path);
        }
    }
    public static Credentials loadBip44Credentials(String password, String mnemonic) {
        return loadBip44Credentials(password, mnemonic, false);
    }
    public static Credentials loadBip44Credentials(
            String password, String mnemonic, boolean testNet) {
        byte[] seed = MnemonicUtils.generateSeed(mnemonic, password);
        Bip32ECKeyPair masterKeypair = Bip32ECKeyPair.generateKeyPair(seed);
        Bip32ECKeyPair bip44Keypair = generateBip44KeyPair(masterKeypair, testNet);
        return Credentials.create(bip44Keypair);
    }
}
