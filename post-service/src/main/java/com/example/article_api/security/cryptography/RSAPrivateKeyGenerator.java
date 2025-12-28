package com.example.article_api.security.cryptography;

import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class RSAPrivateKeyGenerator extends KeyGenerator{

    @Override
    protected String getKeyBegin() {
        return "-----BEGIN PRIVATE KEY-----";
    }

    @Override
    protected String getKeyEnd() {
        return "-----END PRIVATE KEY-----";
    }

    @Override
    protected EncodedKeySpec getEncodedKeySpec(byte[] rawBytes) {
        return new PKCS8EncodedKeySpec(rawBytes);
    }

    @Override
    protected Key generateKey(EncodedKeySpec encodedKeySpec) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return KeyFactory.getInstance("RSA").generatePrivate(encodedKeySpec);
    }
}
