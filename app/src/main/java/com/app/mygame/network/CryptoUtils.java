package com.app.mygame.network;

import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class CryptoUtils {

    // AES Encryption
    public static String encryptAES(String plainText, String aesKey, String aesIV) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(hexToBytes(aesKey), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(hexToBytes(aesIV));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    public static String decryptAES(String cipherText, String aesKey, String aesIV) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(hexToBytes(aesKey), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(hexToBytes(aesIV));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] original = cipher.doFinal(Base64.decode(cipherText, Base64.DEFAULT));
        return new String(original, StandardCharsets.UTF_8);
    }

    // RSA Encryption
    public static String encryptRSA(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    public static String decryptRSA(String cipherText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] original = cipher.doFinal(Base64.decode(cipherText, Base64.DEFAULT));
        return new String(original, StandardCharsets.UTF_8);
    }

    // Utility to convert hex string to byte array
    private static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }
}
