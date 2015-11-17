package com.gridnode.pdip.base.certificate.helpers;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Date;

import javax.crypto.Cipher;

public class TestGridCertUtilities {
	private static final String SEC_PROVIDER_BC = "BC";
	
	
	
	public static boolean isMatchingPair(java.security.cert.X509Certificate cert,
	            PrivateKey privateKey)
	{
		try
		{
			PublicKey publicKey = cert.getPublicKey();
			return isMatchingPair(publicKey, privateKey);
		}
		catch (Exception ex)
		{
		return false;
		}
	}
	
	 public static boolean isMatchingPair(PublicKey publicKey,
             PrivateKey privateKey)
	{
		boolean compareOkay = true, match = false;
		byte [] encryptMe = null;
		byte [] cipherOut = null;
		byte [] recoveredText = null;
		int blockSize = 128;
		SecureRandom random = null;
		// We have an RSA key pair, and we have some data. Let's encrypt it!
		
		
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
			random.setSeed( new Date().toString().getBytes() );
			encryptMe = new byte[blockSize];
			random.nextBytes(encryptMe);
			
			//encryption process
			//Cipher cipher = Cipher.getInstance("RSA/NONE/OAEPWithMD5AndMGF1Padding", getSecurityProvider());
			//Cipher cipher = Cipher.getInstance("RSA/NONE/OAEPWithSHA256AndMGF1Padding", getSecurityProvider());
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", getSecurityProvider());
			
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			cipherOut = cipher.doFinal(encryptMe);
		
		} 
		catch (Exception ex) 
		{
		ex.printStackTrace();
		//CertificateLogger.error(ILogErrorCodes.KEY_PAIR_MATCHING, "Error in performing encryption for matching key", ex);
		}
		
		// Now that we have some encrypted data, let's decrypt it with the
		// private key and compare the recovered plaintext with the original.
		try
		{
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", getSecurityProvider());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			System.out.println("#############################[" + cipherOut.length + "]#############################");
			recoveredText = cipher.doFinal(cipherOut);
			System.out.println("#############################[" + recoveredText + "]#############################");
			for( int i = 0 ; i < encryptMe.length ; i++ )
			{
			System.out.println("#############################(" + encryptMe[i] + " " + recoveredText[i] + ")#############################");
			if( encryptMe[i] != recoveredText[i] ) {
			compareOkay = false;
			break;
			}
			}
			match = compareOkay;
		}
		
		
		catch (Exception ex)
		{
		//CertificateLogger.error(ILogErrorCodes.KEY_PAIR_MATCHING, "Error in recovering plain text for matching key", ex);
		ex.printStackTrace();
		}
		return match;
	}


	private static String getSecurityProvider() {
		// TODO Auto-generated method stub
		return SEC_PROVIDER_BC;
	}


	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
