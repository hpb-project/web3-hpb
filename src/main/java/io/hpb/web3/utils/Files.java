package io.hpb.web3.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class Files {

    private Files() { }

    public static byte[] readBytes(File file) throws IOException {
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(bytes);
        }
        return bytes;
    }

    public static String readString(File file) throws IOException {
        return new String(readBytes(file));
    }
}
