package com.example.authenticationService.Utils;

import java.security.SecureRandom;
import java.util.Base64;

public class Generator {
    public static String generateNonce() {
        SecureRandom random = new SecureRandom();
        byte[] nonceBytes = new byte[16];
        random.nextBytes(nonceBytes);
        String nonce = Base64.getUrlEncoder().withoutPadding().encodeToString(nonceBytes);
        return nonce;
    }
}
