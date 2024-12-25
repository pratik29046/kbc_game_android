package com.app.mygame.network;

import android.os.Build;

import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;

public class RsaUtil {

    public static class Rsa {

        private static final String RSA_ALGORITHM = "RSA";

        // Encrypt data using RSA public key
        public String encrypt(String data) throws Exception {
            PublicKey publicKey = getPublicKeyFromBase64("LS0tLS1CRUdJTiBQVUJMSUMgS0VZLS0tLS0KTUlJQklqQU5CZ2txaGtpRzl3MEJBUUVGQUFPQ0FR\n" +
                    "OEFNSUlCQ2dLQ0FRRUEwL2NmN0M5U2FLNGxJcVdOanNiZApNRHhiSURYd3JCN2pDZElDQlI3VVE1\n" +
                    "K3M5WHg0aTJjNUkvajJLRjVmQjNkR0JZOHBEZmhiN1U0dyt6SWFDaU5VCjd2RmRnczRnSzlsbmZI\n" +
                    "SWNQV3h2UWtQYzZUTStZU0xYYWZ4NWZhTG0xc3NKcHdBSFo0MHFySy9DZlJKZU5Zc08KdUN0NU9p\n" +
                    "TC9RanpaTXVOekJaRmpKWFRtRCtGN1RMRlI1eG0vbndZMTFHU3V4aU0vS29kTG52ajJrYUNLRFph\n" +
                    "UworRnNRRUpmOHgvQWMrSXo0VjdkUStuOWIxMzQ1VHNWTVRiNktpRWlkUUJDU3B5djVDQitlbURU\n" +
                    "a3hiMFBPT2VKCnlscUZiazlQZHBXVC9KS2lUY3VmUk9oTU13Y3VLMWVrWWduZFFFZndxTTN2Qmg5\n" +
                    "dmFTRkQ5M0NJdjZTS0JJa0gKRHdJREFRQUIKLS0tLS1FTkQgUFVCTElDIEtFWS0tLS0t\n");

            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            byte[] encryptedData = cipher.doFinal(data.getBytes());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return Base64.getEncoder().encodeToString(encryptedData);
            }
            return data;
        }

        // Decrypt data using RSA private key
        public String decrypt(String encryptedData) throws Exception {
            PrivateKey privateKey = getPrivateKeyFromBase64("LS0tLS1CRUdJTiBSU0EgUFJJVkFURSBLRVktLS0tLQpNSUlFcGdJQkFBS0NBUUVBMC9jZjdDOVNh\n" +
                    "SzRsSXFXTmpzYmRNRHhiSURYd3JCN2pDZElDQlI3VVE1K3M5WHg0CmkyYzVJL2oyS0Y1ZkIzZEdC\n" +
                    "WThwRGZoYjdVNHcreklhQ2lOVTd2RmRnczRnSzlsbmZISWNQV3h2UWtQYzZUTSsKWVNMWGFmeDVm\n" +
                    "YUxtMXNzSnB3QUhaNDBxcksvQ2ZSSmVOWXNPdUN0NU9pTC9RanpaTXVOekJaRmpKWFRtRCtGNwpU\n" +
                    "TEZSNXhtL253WTExR1N1eGlNL0tvZExudmoya2FDS0RaYVMrRnNRRUpmOHgvQWMrSXo0VjdkUStu\n" +
                    "OWIxMzQ1ClRzVk1UYjZLaUVpZFFCQ1NweXY1Q0IrZW1EVGt4YjBQT09lSnlscUZiazlQZHBXVC9K\n" +
                    "S2lUY3VmUk9oTU13Y3UKSzFla1lnbmRRRWZ3cU0zdkJoOXZhU0ZEOTNDSXY2U0tCSWtIRHdJREFR\n" +
                    "QUJBb0lCQVFDVkM2bEx4NzYzMHZSVAoxM3Voa041Sm83Y2tBQ25hcmVGUnIzVXlLb3B4ZnA3OUEy\n" +
                    "ZzBCZ0pjOUJ3TEtNakRsREFOaXF3QW9jbnFKTmcyCldmV0xlV3FvbGRuK04zalJ2STZUS0I0Slp1\n" +
                    "MFkrR1VuS1p4TWExWnp0VStzYzFiRVpJTEdCalF1c1VuM29hTXQKbFRQZmpJZHVJMHVjQUszaTRx\n" +
                    "eUxwdm5qU1djaEZ6a3dhZUhYakNBVkVGMjhDQkR4ZGQ2SlpNaGY3UFRZdEVjVQp0cW1xa05TNk1W\n" +
                    "dGx6eUFGUkR3TDk4YkR0VENqWm9STjFGZHliaFo3TWQ4REVNRnJoS2VsRWJPMm1VdFBXdC9hCmtW\n" +
                    "UFVuUFY5UmNjUkJlbkd3bExZdHdFYTBrQ3JCbHZxOUxGMGF0UUZyQjVpMEkwN3RHcGlEaTRwQm1F\n" +
                    "cXBiOWcKVkpFVnVUVzVBb0dCQVBKSHduL2ErQ053blJ2Mno3SWRCQkZIdHN3TTRWYm5HbnRCN3pJ\n" +
                    "Y3h5d1lCay9ycVlDYgp4TkErUEZVVXlHVGNld1dxazlnS0dhUHk5KzRjcEY5OVFubEZTVVFnVUdV\n" +
                    "RVVRVEM5aGF0dzMxa0VlTGJNWUI3CkgxL2xxdlJhSzNmMTJGMm1hU1BNamswQ28ybXNZeU1SUUJO\n" +
                    "SVBHYjVza3RYMXFKalFYYnhGUU5UQW9HQkFOLzMKNXA0blNoenQrZ0tRdTNTOGtTYWxBaHVlR1dO\n" +
                    "NVdhaC9Uc2Y1L3VXcSsrdHpualU0TENpMlhGVTVsdmNMN2FsMgphYWkxbFJxaWJMenB4SnBsMlg5\n" +
                    "Y1Q5RkRXbjBUUTUrS1ZZVSs3THF2MllTbnowVXkyTEdXTGsyK0Fxd1UvUE0yCndpaDVKbDI3QnR0\n" +
                    "ODdHSzdHcEM5QzMxL3JlN0V0R3ovQXBRWUFGSFZBb0dCQUl4NzRhTGpaRGlnaHVqOUh3ZUgKWGFG\n" +
                    "cmRPb3ltY01iTkRhK1Q2VStISnRMdnZVK2o4V2NETzQyK0NMWmJ5MVV6eHpGQVA4bk5DRGx0Szlq\n" +
                    "K2tmSQpTUEFxcGNZVCtxbm9hOVlYK2p2Q08vV01QZmJONUpFOFV6LzIyejdldmcvTUJNNmd2Snhl\n" +
                    "Y3pEaVU4RGxqWUo2CjhTMTlHRkVaZmpIRDJCZkc5K0d3L3Q4ekFvR0JBSm5PRmxPWnk2MmVSOGlu\n" +
                    "Ti9SczRvbFZXK00zYVZ3RW8rdXcKc1p0bk5RTG5ydVQ4bXpNVkJrNVhIWVZ4YlJqdTJpRThMa2Ny\n" +
                    "bTkrVWwzUU1YWFZpYUVDc1ZpdnhReDBOczZ6RwpSUUNpTzlQZ20xSWNJNXp6MUJmd2VJL3U0c3Jt\n" +
                    "QnVmSFBRS3FZQXlTT1oxVlpzaE9rV25BU1RuQUN1UWp0WHY4CmkvckRtTVNaQW9HQkFPUmNWMi9u\n" +
                    "djZBRUpENUM1UFhtd29lVjFSYVdzYkRlM3BJOG1wcGRVSWFnQVBRWU12KzMKbHVuL2w2MWhybHpH\n" +
                    "cWFZM2dPMm1sWHcyRHBsd1A2bHdNdFlCWWJvWUVUY3l4ZEFBdVlVVG9XV3h1Y3VYbllpQQpEb2hH\n" +
                    "RmFPc1hFaXgyTU52N1BLemZIeCtBTzcyVkRNc0tDR2ZlbW5YckJSQTZrZ0hjRDQyS2VNZQotLS0t\n" +
                    "LUVORCBSU0EgUFJJVkFURSBLRVktLS0tLQ==\n");

            Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            byte[] decodedData = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                decodedData = Base64.getDecoder().decode(encryptedData);
            }
            byte[] decryptedData = cipher.doFinal(decodedData);
            return new String(decryptedData);
        }

        // Helper method to get PublicKey from Base64 encoded string
        private PublicKey getPublicKeyFromBase64(String base64PublicKey) throws Exception {
            byte[] decoded = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                decoded = Base64.getDecoder().decode(base64PublicKey);
            }
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            return keyFactory.generatePublic(new java.security.spec.X509EncodedKeySpec(decoded));
        }

        // Helper method to get PrivateKey from Base64 encoded string
        private PrivateKey getPrivateKeyFromBase64(String base64PrivateKey) throws Exception {
            byte[] decoded = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                decoded = Base64.getDecoder().decode(base64PrivateKey);
            }
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            return keyFactory.generatePrivate(new java.security.spec.PKCS8EncodedKeySpec(decoded));
        }
    }
}
