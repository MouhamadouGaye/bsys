package com.mgaye.bsys.utility;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

// public class KeyGen {
//     public static void main(String[] args) throws Exception {
//         KeyGenerator keyGenAES = KeyGenerator.getInstance("AES");
//         KeyGenerator keyGenHmacSha = KeyGenerator.getInstance("HmacSHA256");

//         keyGenAES.init(256); // 256-bit key
//         keyGenHmacSha.init(256);

//         SecretKey secretKey = keyGenAES.generateKey();
//         SecretKey secretKey2 = keyGenHmacSha.generateKey();

//         // Encode to Base64 for readable storage/transfer
//         String base64KeyAES = Base64.getEncoder().encodeToString(secretKey.getEncoded());
//         String base64KeyHmacSHA = Base64.getEncoder().encodeToString(secretKey2.getEncoded());

//         System.out.println("Generated 256-bit AES Key (Base64): " + base64KeyAES
//                 + "Generated 256-bit HmacSha Key (Base64):" + base64KeyHmacSHA);
//     }
// }

public class KeyGen {
    public static void main(String[] args) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("HmacSHA512");
        keyGen.init(512); // 512 bits
        SecretKey secretKey = keyGen.generateKey();

        // Convert key to Base64 for use in JWT or config
        String base64Key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Generated HS512 Key (Base64): " + base64Key);
    }
}