package com.secure;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class EncryptionDecryption {

	public static void main(String[] args) throws Exception {
		// Generate Key-Pair
		KeyPair keyPair = generateKeyPair();
		
		// string/pay-load/data to be encrypted //
		String orignalMsg = "Keep my secret";
		
		// encrypt the string using the public key //
		byte[] encryptedMsgBytes = encrypt(orignalMsg, keyPair.getPublic());
				
		// encrypt the string using the public key //
		String decryptedMsg = decrypt(encryptedMsgBytes, keyPair.getPrivate());
		
		// Print results //
		System.out.println("Original: "+orignalMsg);
		System.out.println("Encrypted: "+ new String(encryptedMsgBytes));
		System.out.println("Decrypted: "+ new String(decryptedMsg));
	}
	

	private static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(2048); // key size //
		
		return keyPairGenerator.generateKeyPair();
	}	
	
	private static byte[] encrypt(String orignalMsg, PublicKey publicKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		return cipher.doFinal(orignalMsg.getBytes());
	}
	
	private static String decrypt(byte[] encryptedMsg, PrivateKey privateKey) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		byte[] decryptedBytes= cipher.doFinal(encryptedMsg);
		return new String(decryptedBytes);
	}

}
