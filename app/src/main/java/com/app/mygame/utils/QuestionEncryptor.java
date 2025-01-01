package com.app.mygame.utils;

import android.os.Build;
import android.util.Log;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;

public class QuestionEncryptor {

	private String questionEncryptionPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuZQk+uBmmacLzph7giLelDEPRTdCrR23qC8yE3Z0CZI2N0BRnmyvMEiXRYda1S1DSy2QPAtgNfljpLWFTW6/FIsXS7YQivkt/RBbwPDIYIKLE2+sCZ4GSD1LDH+Ub0ePQuzMrpfq06YR/FL+1Pnkvru+tUJSldqoXu9d8P5y1sA5e7Z60U9jaQG0zNU7QJgCU2xax21xU7VM5noLTTpVR4hi4wxjljyuXXgsBfhDE6FRPN2oZD+Vq2rUGSs9DA4fQLipF9akpeeDR73CWStN1yQM4t2H427xFNhTsLnzrN2KrEktQSw/4csfIvCOET3IeJyHG4SozOUiT4389LDB2wIDAQAB";
	private String questionDecryptedPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC5lCT64GaZpwvOmHuCIt6UMQ9FN0KtHbeoLzITdnQJkjY3QFGebK8wSJdFh1rVLUNLLZA8C2A1+WOktYVNbr8UixdLthCK+S39EFvA8MhggosTb6wJngZIPUsMf5RvR49C7Myul+rTphH8Uv7U+eS+u761QlKV2qhe713w/nLWwDl7tnrRT2NpAbTM1TtAmAJTbFrHbXFTtUzmegtNOlVHiGLjDGOWPK5deCwF+EMToVE83ahkP5WratQZKz0MDh9AuKkX1qSl54NHvcJZK03XJAzi3YfjbvEU2FOwufOs3YqsSS1BLD/hyx8i8I4RPch4nIcbhKjM5SJPjfz0sMHbAgMBAAECggEAFgTd3maBxflsDTdKcEZy4mJvzWahIqlEAVCYQHbtjbUreaLIDGQY5UG3sSg+Ps0ADwt48abn43+TsxdWmblqMqXOPc18aQDn0/785AetvsW+eq/lWb7GQAwFu5Xnpsx20xoFrzGXS0WJHMc8NUjxOzBLkHYuFlm7YIhGPTHzrkcspKa/HNErq/PHMsGyhaq/h9AwEuxAoAJwCby8LUq+fgHSZ2yLyaSdBV+CrNZziIR/4yM8FktQGEjsJTt+ZcMoZTwPkQm3T0M8+pIOFz27Nsab/fkZPBpf2+yM8Y68ZYzWegFyKBoiDOOAFm6PCGmq7P3/y34QxAdW1trGvS95AQKBgQDIux4Z6UpCZHIvj/44lzLXidJfAYMCr90382gV12OEEJ/HkSlcy3szLcjd17MXU13YqFNKGhOehbgAcklcVSUdnvF4SZnJjLkGm9PRqp5WgfVszJLw8XuDBu+NmhQA67Cqe3RsNAvirtJxDYeklA+HnBDVvYBIQkPCke4oz7hP1QKBgQDsrP5xpR0UeHao12lF19bGGBirmtfhEU6ZShcIF9mW++cDT9B8DaPDjIG9ceucgl6V06XI5/PrcLoOLog2AahG4vLAtB8VRZul1IFb1SQP8QowQ/NwUjolrq+Xy/37kXbAXEpYcM3bYz/kAwSmXMM2ok849LzcyQvyPISEV9xS7wKBgQCQv3YuYy88fyhkKea9sjJkgSslfXaWynCwtyJ4lZBanI5Ln97+3nDh8AMoDjJEAMbsxD0Dc5hwOKb+E2vZXEDegk5IWnK+h26pWFc/m1SAt0heg4r2mnxheO/n4ZLJMyx7U3kZydJETaLNCV+mC2rDdIQjTXqjIL2yIf9miJu1XQKBgDSZWc8d9/GFuDFb9lx8VJIf5tO0jQfLFeHBco7s5gHFKBk3UslPKrcZ8feNauSUoLrBl9WnnzETE3Z1ZvZBtglTBMpI/aN3ndjj+n/TRbl5Q19oqqCq1ZtsUD/3KRIQyQiFOkyffVClpRxig1UJxqTMu7iXw6V51PrX8keLUz7nAoGBAJ9693pdaiTaDyZ38wl9wpMhgrtYvB95AEmdj/YDUfnjK9ZfcIxbLIHUzfv2sZDKoQEntc01KfKo8PCitFGxhposYQbsfp9/3eSKsBOs79iSkVew84sus+hsF3qLshLFbhaZTAtdx5YKsVHctddPsW/yo+lO/gdSG5Uq68wnwaH2";

	public String encrypt(String plainText) {
		try {
			PublicKey publicKey = convertBase64ToPublicKey(questionEncryptionPublicKey);
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Base64.getEncoder().encodeToString(encryptedBytes);
            }
        } catch (Exception e) {
			throw new RuntimeException(e);
		}
        return plainText;
    }

	public String decrypt(String encryptedText) {
		try {
			PrivateKey privateKey = convertBase64ToPrivateKey(questionDecryptedPrivateKey);
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] encryptedBytes = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                encryptedBytes = Base64.getDecoder().decode(encryptedText);
            }
			byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
			String decryptedData = new String(decryptedBytes);
			return decryptedData;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


	public PublicKey convertBase64ToPublicKey(String publicKeyBase64) throws Exception {
        byte[] keyBytes = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            keyBytes = Base64.getDecoder().decode(publicKeyBase64);
        }
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(keySpec);
	}

	public PrivateKey convertBase64ToPrivateKey(String privateKeyBase64) throws Exception {
        byte[] keyBytes = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            keyBytes = Base64.getDecoder().decode(privateKeyBase64);
        }
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePrivate(keySpec);
	}
}
