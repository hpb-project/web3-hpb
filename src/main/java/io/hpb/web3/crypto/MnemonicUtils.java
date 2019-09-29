package io.hpb.web3.crypto;
import static io.hpb.web3.crypto.Hash.sha256;
import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.generators.PKCS5S2ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
public class MnemonicUtils {
    private static final int SEED_ITERATIONS = 2048;
    private static final int SEED_KEY_SIZE = 512;
    private static List<String> WORD_LIST = null;
    public static String generateMnemonic(byte[] initialEntropy) {
        validateEntropy(initialEntropy);
        final List<String> words = getWords();
        int ent = initialEntropy.length * 8;
        int checksumLength = ent / 32;
        byte checksum = calculateChecksum(initialEntropy);
        boolean[] bits = convertToBits(initialEntropy, checksum);
        int iterations = (ent + checksumLength) / 11;
        StringBuilder mnemonicBuilder = new StringBuilder();
        for (int i = 0; i < iterations; i++) {
            int index = toInt(nextElevenBits(bits, i));
            mnemonicBuilder.append(words.get(index));
            boolean notLastIteration = i < iterations - 1;
            if (notLastIteration) {
                mnemonicBuilder.append(" ");
            }
        }
        return mnemonicBuilder.toString();
    }
    public static byte[] generateEntropy(String mnemonic) {
        final BitSet bits = new BitSet();
        final int size = mnemonicToBits(mnemonic, bits);
        if (size == 0) {
            throw new IllegalArgumentException("Empty mnemonic");
        }
        final int ent = 32 * size / 33;
        if (ent % 8 != 0) {
            throw new IllegalArgumentException("Wrong mnemonic size");
        }
        final byte[] entropy = new byte[ent / 8];
        for (int i = 0; i < entropy.length; i++) {
            entropy[i] = readByte(bits, i);
        }
        validateEntropy(entropy);
        final byte expectedChecksum = calculateChecksum(entropy);
        final byte actualChecksum = readByte(bits, entropy.length);
        if (expectedChecksum != actualChecksum) {
            throw new IllegalArgumentException("Wrong checksum");
        }
        return entropy;
    }
    public static List<String> getWords() {
        if (WORD_LIST == null) {
            WORD_LIST = Collections.unmodifiableList(populateWordList());
        }
        return WORD_LIST;
    }
    public static byte[] generateSeed(String mnemonic, String passphrase) {
        if (isMnemonicEmpty(mnemonic)) {
            throw new IllegalArgumentException("Mnemonic is required to generate a seed");
        }
        passphrase = passphrase == null ? "" : passphrase;
        String salt = String.format("mnemonic%s", passphrase);
        PKCS5S2ParametersGenerator gen = new PKCS5S2ParametersGenerator(new SHA512Digest());
        gen.init(mnemonic.getBytes(UTF_8), salt.getBytes(UTF_8), SEED_ITERATIONS);
        return ((KeyParameter) gen.generateDerivedParameters(SEED_KEY_SIZE)).getKey();
    }
    public static boolean validateMnemonic(String mnemonic) {
        try {
            generateEntropy(mnemonic);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    private static boolean isMnemonicEmpty(String mnemonic) {
        return mnemonic == null || mnemonic.trim().isEmpty();
    }
    private static boolean[] nextElevenBits(boolean[] bits, int i) {
        int from = i * 11;
        int to = from + 11;
        return Arrays.copyOfRange(bits, from, to);
    }
    private static void validateEntropy(byte[] entropy) {
        if (entropy == null) {
            throw new IllegalArgumentException("Entropy is required");
        }
        int ent = entropy.length * 8;
        if (ent < 128 || ent > 256 || ent % 32 != 0) {
            throw new IllegalArgumentException(
                    "The allowed size of ENT is 128-256 bits of " + "multiples of 32");
        }
    }
    private static boolean[] convertToBits(byte[] initialEntropy, byte checksum) {
        int ent = initialEntropy.length * 8;
        int checksumLength = ent / 32;
        int totalLength = ent + checksumLength;
        boolean[] bits = new boolean[totalLength];
        for (int i = 0; i < initialEntropy.length; i++) {
            for (int j = 0; j < 8; j++) {
                byte b = initialEntropy[i];
                bits[8 * i + j] = toBit(b, j);
            }
        }
        for (int i = 0; i < checksumLength; i++) {
            bits[ent + i] = toBit(checksum, i);
        }
        return bits;
    }
    private static boolean toBit(byte value, int index) {
        return ((value >>> (7 - index)) & 1) > 0;
    }
    private static int toInt(boolean[] bits) {
        int value = 0;
        for (int i = 0; i < bits.length; i++) {
            boolean isSet = bits[i];
            if (isSet) {
                value += 1 << bits.length - i - 1;
            }
        }
        return value;
    }
    private static int mnemonicToBits(String mnemonic, BitSet bits) {
        int bit = 0;
        final List<String> vocabulary = getWords();
        final StringTokenizer tokenizer = new StringTokenizer(mnemonic, " ");
        while (tokenizer.hasMoreTokens()) {
            final String word = tokenizer.nextToken();
            final int index = vocabulary.indexOf(word);
            if (index < 0) {
                throw new IllegalArgumentException(
                        String.format("Mnemonic word '%s' should be in the word list", word));
            }
            for (int k = 0; k < 11; k++) {
                bits.set(bit++, isBitSet(index, 10 - k));
            }
        }
        return bit;
    }
    private static byte readByte(BitSet bits, int startByte) {
        byte res = 0;
        for (int k = 0; k < 8; k++) {
            if (bits.get(startByte * 8 + k)) {
                res = (byte) (res | (1 << (7 - k)));
            }
        }
        return res;
    }
    private static boolean isBitSet(int n, int k) {
        return ((n >> k) & 1) == 1;
    }
    public static byte calculateChecksum(byte[] initialEntropy) {
        int ent = initialEntropy.length * 8;
        byte mask = (byte) (0xff << 8 - ent / 32);
        byte[] bytes = sha256(initialEntropy);
        return (byte) (bytes[0] & mask);
    }
    private static List<String> populateWordList() {
        InputStream inputStream =
                Thread.currentThread()
                        .getContextClassLoader()
                        .getResourceAsStream("en-mnemonic-word-list.txt");
        try {
            return readAllLines(inputStream);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
    private static List<String> readAllLines(InputStream inputStream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        List<String> data = new ArrayList<>();
        for (String line; (line = br.readLine()) != null; ) {
            data.add(line);
        }
        return data;
    }
}
