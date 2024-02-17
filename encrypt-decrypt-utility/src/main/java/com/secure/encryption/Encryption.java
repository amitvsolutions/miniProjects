package com.secure.encryption;

import java.nio.charset.StandardCharsets;
import java.security.PublicKey;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.secure.util.PemUtils;

public class Encryption {

    public static void main(String[] args) {
        try {
            // Load public key from PEM file
        	PublicKey publicKey = PemUtils.readPublicKeyFromFile("public_key.pem", "RSA");

            // Create JSON object to encrypt
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "Amit Verma");
            jsonObject.put("age", 33);

            // Encrypt JSON data
            byte[] encryptedData = PemUtils.encrypt(publicKey, jsonObject.toString().getBytes(StandardCharsets.UTF_8));

            // Convert encrypted data to Base64 for easy transmission
            String encryptedBase64 = Base64.encodeBase64String(encryptedData);
            System.out.println("Encrypted Data: " + encryptedBase64);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


	
}
