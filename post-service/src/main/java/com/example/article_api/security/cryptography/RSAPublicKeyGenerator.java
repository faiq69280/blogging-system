package com.example.article_api.security.cryptography;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class RSAPublicKeyGenerator extends KeyGenerator {

    @Override
    protected String getKeyBegin() {
        return "-----BEGIN PUBLIC KEY-----";
    }

    @Override
    protected String getKeyEnd() {
        return "-----END PUBLIC KEY-----";
    }

    @Override
    protected EncodedKeySpec getEncodedKeySpec(byte[] rawBytes) {
        return new X509EncodedKeySpec(rawBytes);
    }

    @Override
    protected Key generateKey(EncodedKeySpec encodedKeySpec) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyFactory.getInstance("RSA").generatePublic(encodedKeySpec);
    }
}
