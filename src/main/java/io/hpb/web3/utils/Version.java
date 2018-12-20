package io.hpb.web3.utils;

import java.io.IOException;
import java.util.Properties;


public class Version {

    private Version() {}

    public static final String DEFAULT = "none";

    private static final String TIMESTAMP = "timestamp";
    private static final String VERSION = "version";

    public static String getVersion() throws IOException {
        return loadProperties().getProperty(VERSION);
    }

    public static String getTimestamp() throws IOException {
        return loadProperties().getProperty(TIMESTAMP);
    }

    private static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(Version.class.getResourceAsStream("/web3-version.properties"));
        return properties;
    }
}
