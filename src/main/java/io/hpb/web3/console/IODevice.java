package io.hpb.web3.console;


public interface IODevice {
    void printf(String format, Object... args);

    String readLine(String fmt, Object... args);

    char[] readPassword(String fmt, Object... args);
}
