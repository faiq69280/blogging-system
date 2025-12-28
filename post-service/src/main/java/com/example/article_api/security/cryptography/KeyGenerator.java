package com.example.article_api.security.cryptography;

import io.jsonwebtoken.io.Decoders;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;

public abstract class KeyGenerator {

    public Key loadKeyFromResource(Resource resource) {
        try (InputStream inputStream = resource.getInputStream()) {
            String keyContent = new String(inputStream.readAllBytes());

            String keyEnd = getKeyEnd();
            String keyBegin = getKeyBegin();

            if (keyBegin != null) {
                keyContent = keyContent.replace(keyBegin, "");
            }

            if (keyEnd != null) {
                keyContent = keyContent.replace(keyEnd, "");
            }

            keyContent = keyContent.replaceAll("\\s", "");
            byte[] rawBytes = Decoders.BASE64.decode(keyContent);

            EncodedKeySpec encodedKeySpec = getEncodedKeySpec(rawBytes);
            return generateKey(encodedKeySpec);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract String getKeyBegin();
    protected abstract String getKeyEnd();
    protected abstract EncodedKeySpec getEncodedKeySpec(byte[] rawBytes);
    protected abstract Key generateKey(EncodedKeySpec encodedKeySpec) throws NoSuchAlgorithmException, InvalidKeySpecException;
}
