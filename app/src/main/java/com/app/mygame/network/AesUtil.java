package com.app.mygame.network;

import android.os.Build;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class AesUtil {

    // AES Encryption method
    public static String encrypt(String data, String aesKeyHex, String aesIvHex) throws Exception {
        // Convert hex to byte arrays
        byte[] keyBytes = hexToBytes(aesKeyHex);
        byte[] ivBytes = hexToBytes(aesIvHex);

        SecretKey secretKey = new javax.crypto.spec.SecretKeySpec(keyBytes, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return Base64.getEncoder().encodeToString(encrypted);
        }
        return data;
    }

    // AES Decryption method
    public static String decrypt(String encryptedData, String aesKeyHex, String aesIvHex) throws Exception {
        // Convert hex to byte arrays
        byte[] keyBytes = hexToBytes(aesKeyHex);
        byte[] ivBytes = hexToBytes(aesIvHex);

        SecretKey secretKey = new javax.crypto.spec.SecretKeySpec(keyBytes, "AES");
        IvParameterSpec iv = new IvParameterSpec(ivBytes);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);

        byte[] decodedData = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            decodedData = Base64.getDecoder().decode(encryptedData);
        }
        byte[] decrypted = cipher.doFinal(decodedData);

        return new String(decrypted, StandardCharsets.UTF_8);
    }

    // Convert byte array to hex string
    public static String hex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // Convert hex string to byte array
    public static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    // Generate AES Key
    public static SecretKey generateAesKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(128); // AES-128
        return keyGen.generateKey();
    }

    // Generate random AES IV
    public static byte[] generateAesIv() {
        byte[] iv = new byte[16]; // AES block size is 16 bytes
        new java.security.SecureRandom().nextBytes(iv);
        return iv;
    }
}
