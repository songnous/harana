package com.harana.s3.services.s3_server.crypto;

import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;

import javax.annotation.concurrent.ThreadSafe;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

@ThreadSafe
public class Encryption {
    private final InputStream cis;
    private final IvParameterSpec iv;
    private final int part;

    public Encryption(SecretKeySpec key, InputStream isRaw, int partNumber)
            throws Exception {
        iv = generateIV();

        Cipher cipher = Cipher.getInstance(Constants.AES_CIPHER);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        cis = new CipherInputStream(isRaw, cipher);
        part = partNumber;
    }

    public final InputStream openStream() throws IOException {
        return new EncryptionInputStream(cis, part, iv);
    }

    private IvParameterSpec generateIV() {
        byte[] iv = new byte[Constants.AES_BLOCK_SIZE];
        SecureRandom randomSecureRandom = new SecureRandom();
        randomSecureRandom.nextBytes(iv);

        return new IvParameterSpec(iv);
    }
}