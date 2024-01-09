package com.dbtest.yashwith.utils;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenHashUtil {
    public static byte[] getSHABytes(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");

        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String bytesToHex(byte[] hash) {
        if (hash.length <= 0) return "";
        return String.format("%0" + hash.length * 2 + "x", new BigInteger(hash));
    }

    public static String getSHAString(String input) {
        try {
            return bytesToHex(getSHABytes(input));
        } catch (NoSuchAlgorithmException e) {
            log.error("NoSuchAlgorithm Exception occurred in getting SHA string: ", e);
            return input;
        }
    }
}
