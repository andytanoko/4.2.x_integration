/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridCertUtilities
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 04-May-2002    Jagadeesh           Created.
 * 13-Nov-2002    Jagadeesh           Added Aditional Methods.
 *                                    1.loadPKCS8PrivateKeyData(char[] password,
 *                                      String pkKeyData())
 *                                      (load PKCS8PrivateKey by PrivateKey in String,
 *                                      *can be used when PrivateKey is retrieved from
 *                                      Certificate Entity.*)
 *                                    2.loadX509CertificateByString(String certData)
 *                                      (load X509Certificate by CertData in String,
 *                                      *can be used when certificate is retrieved
 *                                      from Certificate Entity.*)
 *
 * 02-April-2003    Qingsong           Fix bugs in Methods for loading Cert from File
 *                                     move helper methods from CertificateManagerBean to GridCertUtitiles
 * 25-Aug-2003      Jagadeesh          Modified - loadX509Certificate Method as this method
 *                                    throws java.io.IOException: DerInputStream.getLength()
 * 26 May 2004      Neo Sok Lay       Undo above change in loadX509Certificate
 * 17 Oct 2005      Neo Sok Lay       Remove additional ; in isMatchingPair() method. 
 * 07 Aug 2006      Tam Wei Xiang     Amend the way we access SecurityDB. 
 *                                    Modified method : getPrivatePassword()
 */

package com.gridnode.pdip.base.certificate.helpers;

import com.gridnode.pdip.base.certificate.model.IX500Name;
//import com.gridnode.pdip.base.transport.handler.HTTPTransportHandler;
//import com.gridnode.pdip.base.transport.helpers.JavaKeyStoreHandler;
import com.rsa.certj.cert.*;
import com.rsa.jsafe.*;

import javax.mail.internet.MimeUtility;

import java.io.*;
//import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Hashtable;

/**
 * Copyright (c) 1999, RSA Security Inc.
 *
 * This file is modified from RSA sample file CertUtilities.java
 * CertUtilities.java
 *
 * This file contains some methods that make working with X.509
 * certificates a bit easier.  This code is intended to make the RSA
 * Cert-J sample code more readable by removing unnecessary code.
 *
 * Feel free, however, to adopt the code contained within for your own
 * purposes
 *
 * Change History
 * @author Jason Gillis
 * @version 1.0
 */
public class TestGridCertUtilitiesJsafe
{
//  public static final ExceptionValue EX_MD5 = new ExceptionValue("MD5 Exception ");
//  public static final ExceptionValue EX_ENCODE = new ExceptionValue("Encode Exception ");
//  public static final ExceptionValue EX_DECODE = new ExceptionValue("Decode Exception ");
//  public static final ExceptionValue EX_CERT = new ExceptionValue("Certificate Exception ");


  public static boolean isMatchingPair(JSAFE_PublicKey publicKey,
                                       JSAFE_PrivateKey privateKey)
  {
    boolean CompareOkay = true, match = false;
    byte [] encryptMe = null;
    byte [] cipherText = null;
    byte [] recoveredText = null;
    int blockSize = 24;
    JSAFE_SecureRandom random = null;
    // We have an RSA key pair, and we have some data. Let's encrypt it!
    try {
      random = (JSAFE_SecureRandom)
        JSAFE_SecureRandom.getInstance ("MD5Random", "Java");
      random.seed( new Date().toString().getBytes() );
      encryptMe = random.generateRandomBytes( blockSize );
      // Create the JSAFE_AsymmetricCipher instance that actually does
      // the encryption.
      JSAFE_AsymmetricCipher encryptor =
       JSAFE_AsymmetricCipher.getInstance( "RSA/PKCS1Block02Pad", "Java" );

      encryptor.encryptInit( publicKey, random );
      cipherText = new byte[ encryptor.getOutputBlockSize() ];
      int partOutLen = encryptor.encryptUpdate( encryptMe, 0,
                                                encryptMe.length, cipherText,
                                                0 );
      //int finalOutLen = 
      encryptor.encryptFinal( cipherText, partOutLen);
      //int totalOutLen = partOutLen + finalOutLen;

//      println("[RSAEncrypt]  The " + totalOutLen +
//                         " bytes of the ciphertext are:");
//      printBuffer( cipherText );

      // There might be sensitive information still in the encryptor object,
      // so we should call clearSensitiveData() before it goes out of scope.
      encryptor.clearSensitiveData();
    } catch (Exception ex) {
//      println("[RSAEncrypt]  Exception anyExceptionncountered during" +
//                         "Encryption." );
//      println( "[RSAEncrypt]  " + anyException.toString() );
//      anyException.printStackTrace (new PrintStream (this.getOutputStream ()));
//    ex.printStackTrace();
//      exit (anyException, 3);
    }

    // Now that we have some encrypted data, let's decrypt it with the
    // private key and compare the recovered plaintext with the original.
    try {
      JSAFE_AsymmetricCipher decryptor =
       JSAFE_AsymmetricCipher.getInstance( "RSA/PKCS1Block02Pad", "Java" );

      decryptor.decryptInit( privateKey );
      recoveredText = new byte[ cipherText.length ];
      int partOutLen = decryptor.decryptUpdate( cipherText, 0,
                                                cipherText.length,
                                                recoveredText, 0 );
      //int finalOutLen = 
      decryptor.decryptFinal( recoveredText, partOutLen );
      //int totalOutLen = partOutLen + finalOutLen;

      // The decryptor may also contain sensitive information, so we should
      // call clearSensitiveData() before it goes out of scope.
      decryptor.clearSensitiveData();

      for( int i = 0 ; i < encryptMe.length ; i++ )
      {
        if( encryptMe[i] != recoveredText[i] ) {
          CompareOkay = false;
          break;
        }
      }
      match = CompareOkay;
    }
    catch (Exception ex)
    {
//      println("[RSAEncrypt]  Exception anyExceptionncountered " +
//                         "during decryption." );
//      println( "[RSAEncrypt]  " + anyException.toString() );
//      anyException.printStackTrace (new PrintStream (this.getOutputStream ()));
//      exit (anyException, 4);
//      ex.printStackTrace();
    }
    return match;
  }

  public static boolean isMatchingPair(X509Certificate cert,
                                       JSAFE_PrivateKey privateKey)
  {
    try
    {
      JSAFE_PublicKey publicKey = cert.getSubjectPublicKey("Java");
      return isMatchingPair(publicKey, privateKey);
    }
    catch (Exception ex)
    {
      return false;
    }
  }

public static X509Certificate loadX509CertificateByString(String certificateData)
  {
    return loadX509Certificate(decode(certificateData));
  }

  public static X509Certificate loadX509Certificate (byte[] certificateData)
  {
    try
    {
      // 040526NSL this method only support DER-encoded format.
      X509Certificate cert = new X509Certificate (certificateData, 0, 0);
      return (cert); 
	 // return JavaKeyStoreHelper.convertCertificate(JavaKeyStoreHandler.loadCertificate(certificateData));
    }
    catch (Exception ex)
    {
      ex.printStackTrace ();
    }
    return null;
  }

  public static byte[] decode(String data)
  {
    byte[] decodedData = null;
    //byte[] stringData = null; 

    try
    {
      ByteArrayInputStream bis = new ByteArrayInputStream(data.getBytes());
      InputStream is = MimeUtility.decode(bis, "base64");
      // This available value is not accurate, need to verify with the actual read value
      int a = is.available();
      decodedData = new byte[a];
      int r = is.read(decodedData);
      if(a != r)  // double check if it is correct
      {
        byte[] tmpData = new byte[r];
        System.arraycopy(decodedData, 0, tmpData, 0, r);
        decodedData = tmpData;
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

    return decodedData;
  }


  
}
