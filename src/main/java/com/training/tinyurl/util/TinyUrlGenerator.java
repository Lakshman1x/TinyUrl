package com.training.tinyurl.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Base64;

public class TinyUrlGenerator {
    private TinyUrlGenerator(){}
    public static String createShortCode(String longUrl) throws NoSuchAlgorithmException {
        String salt = String.valueOf(Instant.now().getEpochSecond() + longUrl);
        MessageDigest msgDigest = MessageDigest.getInstance("SHA-256");
        byte[] generatedHash = msgDigest.digest(salt.getBytes());
        return Base64.getUrlEncoder().withoutPadding().encodeToString(generatedHash).substring(0, 8);
    }
}