package com.secure;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.secure.util.PemUtils;

public class PemEncryptionDecryption {

	public static void main(String[] args) {
		try {
	        PublicKey publicKey = PemUtils.readPublicKeyFromFile("public_key.pem", "RSA");
	        PrivateKey privateKey = PemUtils.readPrivateKeyFromFile("private_key.pem", "RSA");	
	        
	     // Create JSON object to encrypt
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "John Doe");
            jsonObject.put("age", 30);

            // Encrypt JSON data using public key
            byte[] encryptedData = PemUtils.encrypt(publicKey, jsonObject.toString().getBytes(StandardCharsets.UTF_8));

            // Convert encrypted data to Base64 for easy transmission
            String encryptedBase64 = Base64.encodeBase64String(encryptedData);
            System.out.println("Encrypted Data: " + encryptedBase64);

            // Decrypt data using private key
            byte[] decryptedData = PemUtils.decrypt(privateKey, Base64.decodeBase64(encryptedBase64));
            String decryptedText = new String(decryptedData, StandardCharsets.UTF_8);
            System.out.println("Decrypted Data: " + decryptedText);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}	


}
