package com.app.mygame.network;

import java.security.NoSuchAlgorithmException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public class RsaEncryptionLogic {

    public static void main(String[] args) throws Exception {
        // Step 1: Generate AES Key
        SecretKey aesKey = AesUtil.generateAesKey();
        String aesKeyHex = AesUtil.hex(aesKey.getEncoded());

        // Step 2: Generate AES IV
        byte[] ivBytes = AesUtil.generateAesIv();
        String aesIvHex = AesUtil.hex(ivBytes);

        // Step 3: Combine AES Key and IV
        String combinedKeyIv = aesKeyHex + "|" + aesIvHex;

        // Step 4: Encrypt with RSA Public Key
        RsaUtil.Rsa rsaUtil = new RsaUtil.Rsa(); // Ensure Rsa class is initialized
        String encryptedKeyIv = rsaUtil.encrypt(combinedKeyIv);

        // Step 5: Print or Send `YOUR_RSA_ENCRYPTED_KEY`
        System.out.println("YOUR_RSA_ENCRYPTED_KEY: " + encryptedKeyIv);
    }
}

