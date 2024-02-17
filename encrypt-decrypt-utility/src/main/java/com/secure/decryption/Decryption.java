package com.secure.decryption;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.secure.util.PemUtils;

public class Decryption {

    public static void main(String[] args) throws Exception {
        // Read private key from file
    	PrivateKey privateKey = PemUtils.readPrivateKeyFromFile("private_key.pem", "RSA");

        // Encrypted data (replace this with the actual encrypted data)
        byte[] encryptedData = getEncryptedData(); 
        // Convert encrypted data to Base64 for easy transmission
        String encryptedBase64 = Base64.encodeBase64String(encryptedData);

        
        // Decrypt data using private key
        byte[] decryptedData = PemUtils.decrypt(privateKey, Base64.decodeBase64(encryptedBase64));
        String decryptedJson = new String(decryptedData, StandardCharsets.UTF_8);
        System.out.println("Decrypted Data: " + decryptedJson);
    }

	public static byte[] decrypt(PrivateKey privateKey, byte[] encryptedData) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return cipher.doFinal(encryptedData);
    }
	
	private static byte[] getEncryptedData() throws IOException, Exception {
	    // Create JSON object to encrypt
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "Amit Verma");
        jsonObject.put("age", 33);

        // Encrypt JSON data using public key
        byte[] encryptedData = PemUtils.encrypt(PemUtils.readPublicKeyFromFile("public_key.pem", "RSA"), jsonObject.toString().getBytes(StandardCharsets.UTF_8));

        return encryptedData;
	}
}
