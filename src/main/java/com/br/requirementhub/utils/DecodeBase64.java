package com.br.requirementhub.utils;

import java.util.Base64;

public final class DecodeBase64 {

    private DecodeBase64() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static byte[] decode(String base64Content) {
        return Base64.getDecoder().decode(base64Content);
    }

    public static String encodeToString(byte[] content) {
        return Base64.getEncoder().encodeToString(content);
    }
}