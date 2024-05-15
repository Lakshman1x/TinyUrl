package com.training.tinyurl.util;

import com.training.tinyurl.constants.MessageDigestAlgorithm;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;


public class TinyUrlGenerator {
    private TinyUrlGenerator(){}
    public static String createShortCode(String longUrl) throws NoSuchAlgorithmException {
        String salt = String.valueOf(Instant.now().getEpochSecond() + longUrl);
        MessageDigest msgDigest = MessageDigest.getInstance(MessageDigestAlgorithm.SHA_256);
        byte[] generatedHash = msgDigest.digest(salt.getBytes());
        return Base64.getEncoder().withoutPadding().encodeToString(generatedHash).substring(0, 8);
    }
}