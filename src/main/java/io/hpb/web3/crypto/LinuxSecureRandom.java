package io.hpb.web3.crypto;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.Provider;
import java.security.SecureRandomSpi;
import java.security.Security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class LinuxSecureRandom extends SecureRandomSpi {
    private static final FileInputStream urandom;
    private static class LinuxSecureRandomProvider extends Provider {
        public LinuxSecureRandomProvider() {
            super(
                    "LinuxSecureRandom",
                    1.0,
                    "A Linux specific random number provider that uses /dev/urandom");
            put("SecureRandom.LinuxSecureRandom", LinuxSecureRandom.class.getName());
        }
    }
    private static final Logger log = LoggerFactory.getLogger(LinuxSecureRandom.class);
    static {
        try {
            File file = new File("/dev/urandom");
            urandom = new FileInputStream(file);
            if (urandom.read() == -1) {
                throw new RuntimeException("/dev/urandom not readable?");
            }
            int position = Security.insertProviderAt(new LinuxSecureRandomProvider(), 1);
            if (position != -1) {
                log.info("Secure randomness will be read from {} only.", file);
            } else {
                log.info("Randomness is already secure.");
            }
        } catch (FileNotFoundException e) {
            log.error("/dev/urandom does not appear to exist or is not openable");
            throw new RuntimeException(e);
        } catch (IOException e) {
            log.error("/dev/urandom does not appear to be readable");
            throw new RuntimeException(e);
        }
    }
    private final DataInputStream dis;
    public LinuxSecureRandom() {
        dis = new DataInputStream(urandom);
    }
    @Override
    protected void engineSetSeed(byte[] bytes) {
    }
    @Override
    protected void engineNextBytes(byte[] bytes) {
        try {
            dis.readFully(bytes); 
        } catch (IOException e) {
            throw new RuntimeException(e); 
        }
    }
    @Override
    protected byte[] engineGenerateSeed(int i) {
        byte[] bits = new byte[i];
        engineNextBytes(bits);
        return bits;
    }
}
