/**
 *
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SecurityInfo
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 04-May-2002    Jagadeesh           Created.
 * 17 Oct 2005    Neo Sok Lay         Change isMatchingPair(): remove additional ;.
 * 01 Jul 2009    Tam Wei Xiang       #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib
 */


package com.gridnode.pdip.base.security.helpers;
//import com.gridnode.exception.*;
import java.io.*;
import java.security.AlgorithmParameters;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.mail.internet.MimeUtility;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.gridnode.pdip.base.certificate.helpers.CertificateLogger;
import com.gridnode.pdip.base.certificate.helpers.SecurityServices;
import com.gridnode.pdip.base.security.exceptions.ILogErrorCodes;

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
public class GridCertUtilities
{
//  public static final ExceptionValue EX_MD5 = new ExceptionValue("MD5 Exception ");
//  public static final ExceptionValue EX_ENCODE = new ExceptionValue("Encode Exception ");
//  public static final ExceptionValue EX_DECODE = new ExceptionValue("Decode Exception ");
//  public static final ExceptionValue EX_CERT = new ExceptionValue("Certificate Exception ");
  private static final String SEC_PROVIDER_BC = "BC";
  
  static
  {
    setProvider();
  }
  
  public static String getSecurityProvider()
  { 
    return SEC_PROVIDER_BC;
  }
  
  private static void setProvider()
  {
    Security.addProvider(new BouncyCastleProvider());
  }
  
  public static boolean writeByteTOFile (String fileName, byte[] byteArray,int arrayLen)
 {
    try {
      File outputFile = new File (fileName);
      FileOutputStream outputStream = new FileOutputStream (outputFile);
      outputStream.write (byteArray, 0, arrayLen);
      outputStream.close ();
      return (true);
    } catch (Exception anyException) {
      return (false);
    }
  }

  public static boolean writeByteTOFile (File outputFile, byte[] byteArray,int arrayLen)
  {
    try
    {
      FileOutputStream outputStream = new FileOutputStream (outputFile);
      outputStream.write (byteArray, 0, arrayLen);
      outputStream.close ();
      return (true);
    }
    catch (Exception anyException)
    {
      return (false);
    }
  }


  public static byte[] loadFileToByte (String fileName)
  {

    File inputFile = null;
    FileInputStream inputStream = null;
    byte [] byteArray=null;

    try {
        inputFile = new File (fileName);
        inputStream = new FileInputStream (inputFile);
        byteArray = new byte[(int)inputFile.length()];
        inputStream.read (byteArray, 0, byteArray.length);
        inputStream.close();
        return (byteArray);
        }
     catch (Exception anyException)
        {
         return (null);
        }
  }

  public static byte[] loadFileToByte (File inputFile)
  {

//    File inputFile = null;
    FileInputStream inputStream = null;
    byte [] byteArray=null;

    try {
//        inputFile = new File (fileName);
        inputStream = new FileInputStream (inputFile);
        byteArray = new byte[(int)inputFile.length()];
        inputStream.read (byteArray, 0, byteArray.length);
        inputStream.close();
        return (byteArray);
        }
     catch (Exception anyException)
        {
         return (null);
        }
  }
  
  public static boolean writePKCS8PrivateKeyFile (String passwordStr, String fileName,
                                                  PrivateKey privateKey )
  {
        byte[] data = writePKCS8PrivateKeyData(passwordStr, privateKey);
        return writeByteTOFile(fileName,data, data.length);
  }
  
  public static byte[] writePKCS8PrivateKeyData (String passwordStr, PrivateKey privateKey )
  {
    
    try
    {
      byte[] salt = {
        (byte)0x00, (byte)0x11, (byte)0x22, (byte)0x33,
        (byte)0x44, (byte)0x55, (byte)0x66, (byte)0x77
      };
      
      int iCount = 100;
      
      //PBEwithSHAand3-KeyTripleDES-CBC //from BC, pkcs#12 algo
      //PBE/SHA1/DES/CBC/PKCS5PBE-5-56 //use in RSA Lib, pkcs#5 algo
      
      String             pbeAlgorithm = "PBEWithSHAAnd3-KeyTripleDES-CBC";
      PBEKeySpec         pbeKeySpec = new PBEKeySpec(passwordStr.toCharArray(), salt, iCount);
      SecretKeyFactory   secretKeyFact = SecretKeyFactory.getInstance(
                                                       pbeAlgorithm, SEC_PROVIDER_BC);
      Cipher             cipher = Cipher.getInstance(pbeAlgorithm, SEC_PROVIDER_BC);
      cipher.init(Cipher.WRAP_MODE, secretKeyFact.generateSecret(pbeKeySpec));
  
      byte[]             wrappedKey = cipher.wrap(privateKey);
  
      // create carrier
      EncryptedPrivateKeyInfo pInfo = new EncryptedPrivateKeyInfo(
                                        cipher.getParameters(), wrappedKey);
  
      AlgorithmParameters params = pInfo.getAlgParameters();
      
      return pInfo.getEncoded();
    }
    catch(Exception ex)
    {
      CertificateLogger.error("", "Error writting PKCS8 Private Key", ex);
      return null;
    }
  }
  
  /**
   * Read a PrivateKey object from a file.  The input data needs
   * to be stored in EncryptedPrivateKeyInfo format, as defined in
   * PKCS #8.  The private key data will be decrypted using PKCS #5
   * DES PBE.  This function will query the user for the decryption
   * password.
   *
   * @param passwordStr The password string to encrypt the password file.
   * @param fileName The name of the file to load the encrypted key
   * from.
   * @return A JSAFE_PrivateKey object containing the key data, or null if failed. */
  public static PrivateKey loadPKCS8PrivateKeyFile(String passwordStr, String fileName)
  {
    return loadPKCS8PrivateKeyData(passwordStr, loadFileToByte(fileName));
  }

  /**
   * Read a PrivateKey object from memory.  The input data needs
   * to be stored in EncryptedPrivateKeyInfo format, as defined in
   * PKCS #8.  The private key data will be decrypted using PKCS #5
   * DES PBE.
   *
   * @param passwordStr The password string to encrypt the password file.
   * @param fileName The name of the file to load the encrypted key
   * from.
   * @return A JSAFE_PrivateKey object containing the key data, or null if failed. */

  public static PrivateKey loadPKCS8PrivateKeyData(String passwordStr, byte[] keyData )
  {
    char[] password = new char[passwordStr.length()];
    passwordStr.getChars (0, passwordStr.length(), password, 0);
    return loadPKCS8PrivateKeyData(password, keyData );
  }
  
  /**
   * TWX 20090607 BC version of loading PKCS8 private key
   * @param password the password to decrypt the encrypted private key
   * @param keyData the encrypted private key
   * @return
   */
  public static PrivateKey loadPKCS8PrivateKeyData(char[] password, byte[] keyData )
  {
    
    try
    {
      PBEKeySpec pbeKeySpec = new PBEKeySpec(password); 
      EncryptedPrivateKeyInfo privateKeyInfo = new EncryptedPrivateKeyInfo(keyData);
      String algo = privateKeyInfo.getAlgName();
      AlgorithmParameters param = privateKeyInfo.getAlgParameters();
      
      SecretKeyFactory   secretKeyFact = SecretKeyFactory.getInstance(param.getAlgorithm(), SEC_PROVIDER_BC);
      Cipher cipher = Cipher.getInstance(param.getAlgorithm(), SEC_PROVIDER_BC);
      cipher.init(Cipher.DECRYPT_MODE, secretKeyFact.generateSecret(pbeKeySpec), privateKeyInfo.getAlgParameters());
      
      PKCS8EncodedKeySpec pkcs8Spec = privateKeyInfo.getKeySpec(cipher);
      
      KeyFactory          keyFact = KeyFactory.getInstance("RSA", SEC_PROVIDER_BC);
      PrivateKey          privKey = keyFact.generatePrivate(pkcs8Spec);

      return privKey;
    }
    catch(Exception ex)
    {
      CertificateLogger.error("", "Error in loading the PKCS8 Private Key", ex);
      return null;
    }
  }
  
  /**
   * Load a public key from a file.  The file is expected to contain
   * BER encoded key data.  If the key is loaded, return the
   * JSAFE_PublicKey object, otherwise return <code>null</code>.
   *
   * @param fileName The file name containing the public key data.
   * @return A JSAFE_PublicKey object or <code>null</code>.
   */
  public static PublicKey loadPublicKey (String fileName)
  {
    return loadPublicKeyFromByte(loadFileToByte(fileName));
  }

//TWX 20090607 BC version, no invoker for such method.
  public static PublicKey loadPublicKeyFromByte(byte[] keyData)
  {
    
    try
    {
      ASN1InputStream aIn = new ASN1InputStream(keyData);
      SubjectPublicKeyInfo info = SubjectPublicKeyInfo.getInstance(aIn.readObject());

      X509EncodedKeySpec x509Spec = new X509EncodedKeySpec(keyData);
      KeyFactory keyFact = KeyFactory.getInstance(info.getAlgorithmId().getObjectId().getId(), 
                                                  SEC_PROVIDER_BC);
      PublicKey pubKey = keyFact.generatePublic(x509Spec);
      return pubKey;
    }
    catch(Exception e)
    {
      SecurityLogger.error(ILogErrorCodes.PUBLIC_KEY_LOAD, "Error in loading public key", e);
      return null;
    }
  }
  
  /**
   * Simply write the DER encoding of a public key out to a file.  If
   * the data is successfully saved, return <code>true</code>,
   * otherwise return <code>false</code>.
   *
   * @param fileName The name of the file to store the key in.
   * @param publicKey The <code>JSAFE_PublicKey</code> object that
   * needs to be written.
   * @return <code>true</code> if the key was saved successfully,
   * <code>false</code> otherwise.  */
  public static boolean writePublicKey (String fileName,
                                        PublicKey publicKey)
  {
    byte[] data = writePublicKeyToByte(publicKey);
    return writeByteTOFile(fileName,data, data.length);
  }
  
  //BC version
  public static byte[] writePublicKeyToByte(PublicKey publicKey)
  {
      try
      {
        return publicKey.getEncoded();
      }
      catch(Exception e)
      {
        return null;
      }
  }

  public static PrivateKey loadPrivateKeyFromString(String keyData)
  {
    return loadPrivateKeyFromByte(loadByteArrayFromString(keyData));
  }
  
  public static PrivateKey loadPrivateKeyFromByte(byte[] keyData)
  {
    return loadPKCS8PrivateKeyData(getPrivatePassword(), keyData);
  }
  
  private static String getPrivatePassword()
  {
    return SecurityServices.getPrivatePassword();
  }
  
  public static byte[] loadByteArrayFromString(String data)
  {
      return decode(data);
  }
  
  public static boolean writeX509Certificate (String fileName,
                                              java.security.cert.X509Certificate certificate)
  {
    byte[] data = getX509CertificateDER(certificate);
    return writeByteTOFile(fileName, data,data.length);
  }


  public static byte[] getX509CertificateDER(java.security.cert.X509Certificate cert)
  //  throws GNException
  {
    try
    {
      return cert.getEncoded();
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    //  GNException.throwEx(EX_CERT, "Unable to get DER encoding from cert", ex);
    }
    return null;
  }
  
  /**
   * Load an X509Certificate object from a specified file.  If this
   * function is able to load the certificate from the file, return
   * the object, otherwise, return <code>null</code>.
   *
   * @param fileName The name of the file containing the X.509 Certificate.
   * @return An <code>X509Certificate</code> object containing the
   * certificate, or <code>null</code>. */
  public static java.security.cert.X509Certificate loadX509Certificate (String fileName)
    throws Exception
  {
    return loadX509Certificate(loadFileToByte(fileName));
  }
  
  public static java.security.cert.X509Certificate loadX509Certificate (byte[] certificateData)
  {
    try
    {

      if(certificateData == null)
      {
        return null;
      }
      
      /* Create the X509Certificate object.  Use the DER encoding just
       * read from the inputStream.  If this call fails, return
       * null. */
      ByteArrayInputStream in = new ByteArrayInputStream(certificateData);
      CertificateFactory certFactory = CertificateFactory.getInstance("X.509", SEC_PROVIDER_BC);
      return (java.security.cert.X509Certificate)certFactory.generateCertificate(in);
    }
    catch (Exception ex)
    {
      ex.printStackTrace ();
    }
    return null;
  }


//TWX bc edition
  /**
   * Load a PKCS #10 certificate request from the specified file.  If
   * the data is able to be loaded and instantiated into an object,
   * return that new object.  Otherwise, simply return
   * <code>null</code>.
   *
   * @param fileName The name of the file containing the PKCS #10
   * certificate request.
   * @return A new <code>PKCS10CertRequest</code> object, or
   * <code>null</code>.  */
  public static PKCS10CertificationRequest loadPKCS10CertificationRequest (String fileName) {
    FileInputStream inputStream = null;
    try {
      inputStream = new FileInputStream (fileName);

//      Create a byte array that is big enough to hold the contents
//      of the file.
      byte[] certificateRequest = new byte[inputStream.available()];

//      Read the data from the file. 
      inputStream.read (certificateRequest);

//      Get an instance of a PKCS10CertRequest object using the data
//      just loaded.
      PKCS10CertificationRequest request =
        new PKCS10CertificationRequest (certificateRequest);

      return (request);
    } catch (Exception anyException) {
      anyException.printStackTrace();
      return (null);
    }
    finally
    {
      if(inputStream != null)
      {
        try
        {
          inputStream.close();
        }
        catch(Exception ex)
        {
          ex.printStackTrace();
        }
      }
    }
  }
  
  
  /**
   * Save a <code>PKCS10CertificationRequest</code> object to a specified file.
   *
   * @param fileName The name of the file to store the encoded
   * certificate request in.
   * @param request The <code>PKCS10CertificationRequest</code> object that
   * should be encoded and stored.  */
  public static boolean writePKCS10CertRequest (String fileName,
                                                PKCS10CertificationRequest request) {
    try {
      FileOutputStream outputStream = new FileOutputStream (fileName);
      byte[] certificateRequest = request.getDEREncoded();

      /* DER encode the certificate request.  The encoding will be
       * stored in certificateRequest starting at offset 0. */
      outputStream.write (certificateRequest, 0, certificateRequest.length);
      outputStream.close ();
      return (true);
    } catch (Exception anyException) {
      return (false);
    }
  }

  /**
   * Write an <code>X509CRL</code> object to a specified file.  If
   * this operation is successful, then <code>true</code> is returned.
   *
   * @param fileName The name of the file to store the
   * <code>X509CRL</code> in.
   * @param crl An <code>X509CRL</code> object to store in the file.
   * @return <code>true</code> if the object is saved, otherwise
   * <code>false</code> is returned.  */
  public static boolean writeX509CRL (String fileName, java.security.cert.X509CRL crl) {
    try {
      FileOutputStream outputStream = new FileOutputStream (fileName);

      byte[] crlData = crl.getEncoded();

      /* Write the data to the file. */
      outputStream.write (crlData, 0, crlData.length);
      outputStream.close ();
      return (true);
    } catch (Exception anyException) {
      return (false);
    }
  }

  /**
   * Load an X.509 CRL from the specified file, and return an
   * <code>X509CRL</code> object containing the data.
   *
   * @param fileName The name of the file to store the
   * <code>X509CRL</code> in.
   * @return An <code>X509CRL</code> object containing the data. */
//BC version
  public static java.security.cert.X509CRL loadX509CRL (byte[] crlData)
  {
    try
    {
      ByteArrayInputStream input = new ByteArrayInputStream(crlData);
      CertificateFactory   fact = CertificateFactory.getInstance("X.509", SEC_PROVIDER_BC);

      return (java.security.cert.X509CRL)fact.generateCRL(input);

    }
    catch (Exception ex)
    {
      CertificateLogger.error("", "Error in loading X509CRL", ex);
      return (null);
    }
  }

  public static byte[] getMD5(File inputFile) //throws GNException
  {
    MessageDigest digester = null;
    int blockSize = 4096;
    byte [] inputBlock = new byte[ blockSize ];
    FileInputStream fis = null;


    //Initialize the MD5 digester
    try
    {
      digester = MessageDigest.getInstance("MD5", SEC_PROVIDER_BC);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

    //Open the file
    try
    {
      fis = new FileInputStream( inputFile );
    }
    catch (Exception ex)
    {
    //  GNException.throwEx(EX_MD5, "Error reading file", ex);
      ex.printStackTrace();
    }

    byte [] digest = null;
    try
    {
      int bytesRead = 0;

      while((bytesRead = fis.read( inputBlock )) != -1 )
      {
        digester.update( inputBlock, 0, bytesRead );
      }
      digest = digester.digest();

    }
    catch (Exception ex)
    {
      //GNException.throwEx(EX_MD5, "Error in digesting data", ex);
      ex.printStackTrace();
    }

    return digest;
  }
 
  public static byte[] getMD5(byte[] data)
  {
    MessageDigest digester = null;
    
    //Initialize the MD5 digester
    try
    {
      digester = MessageDigest.getInstance("MD5", SEC_PROVIDER_BC);
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

    byte[] digest = null;
    try
    {
      digester.update(data, 0, data.length);
      digest = digester.digest();

    } catch (Exception ex) {
       ex.printStackTrace();
    }

    return digest;
  }

  public static String encode(byte[] data) //throws GNException
  {
    String rv = null;
    OutputStream os = null;
    ByteArrayOutputStream bos = null;
    try
    {
      bos = new ByteArrayOutputStream(data.length);
      os = MimeUtility.encode(bos, "base64");
      os.write(data);
      os.flush();
//      bos.write(data);
      rv = bos.toString();
    }
    catch (Exception ex)
    {
//      GNException.throwEx(EX_ENCODE, "Unable to encode data", ex);
    }
    return rv;
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

  public static void clearData(byte[] data)
  {
    for (int i = 0; i < data.length; i++)
    {
      data[i] = (byte)0x00;
    }
  }

  public static byte[] getByteArrayFromFile(File file)
  {

    try
    {
      FileInputStream fo = new FileInputStream(file);
      byte[] data = new byte[fo.available()];
      fo.read(data);
      fo.close();
      return data;
    }
    catch (Exception ex)
    {
      return null;
    }
  }

  public static void clearData(char[] data)
  {
    for (int i = 0; i < data.length; i++)
    {
      data[i] = '0';
    }
  }

  public static boolean isMatchingPair(PublicKey publicKey,
                                         PrivateKey privateKey)
  {
    boolean CompareOkay = true, match = false;
    byte [] encryptMe = null;
    byte [] cipherOut = null;
    byte [] recoveredText = null;
    int blockSize = 24;
    SecureRandom random = null;
    // We have an RSA key pair, and we have some data. Let's encrypt it!
    
    
    try {
      random = SecureRandom.getInstance("SHA1PRNG");
      random.setSeed( new Date().toString().getBytes() );
      encryptMe = new byte[blockSize];
      random.nextBytes(encryptMe);
      
      //encryption process
      Cipher cipher = Cipher.getInstance("RSA/NONE/OAEPWithMD5AndMGF1Padding", SEC_PROVIDER_BC);
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      cipherOut = cipher.doFinal(encryptMe);
      
    } 
    catch (Exception ex) 
    {
      SecurityLogger.error(ILogErrorCodes.KEY_PAIR_MATCHING, "Error in performing encryption for matching key", ex);
    }

    // Now that we have some encrypted data, let's decrypt it with the
    // private key and compare the recovered plaintext with the original.
    try
    {
      Cipher cipher = Cipher.getInstance("RSA/NONE/OAEPWithMD5AndMGF1Padding", SEC_PROVIDER_BC);
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      recoveredText = cipher.doFinal(cipherOut);
      System.out.println("+++++++++++++++++++++++++++++++[" + recoveredText + "]+++++++++++++++++++++++++++++++");
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
      SecurityLogger.error(ILogErrorCodes.KEY_PAIR_MATCHING, "Error in recovering plain text for matching key", ex);
    }
    return match;
  }

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
  
  public static String writeIssuerNameToString(X500Principal issuerName)
  {
    try
    {
      return encode(issuerName.getEncoded());
    }
    catch(Exception e)
    {
      return null;
    }
  }

  public static String writeByteArrayToString(byte[] data)
  {
      return encode(data);
  }
 
  public static void main(String[] args)
  {    
//    com.gridnode.security.mime.PKCS7BodyPart.setDebug(true);
    byte[] salt = {
      (byte)0xFF, (byte)0x12, (byte)0x34, (byte)0x56,
      (byte)0xAF, (byte)0x4B, (byte)0x9A, (byte)0x77,
      (byte)0x78, (byte)0x9A, (byte)0xBC, (byte)0xDE,
      (byte)0xCD, (byte)0xAB, (byte)0x81
    };

    System.out.println("Test byte[] : ");
    printBuffer(salt);
try
{
    String encodedStr = encode(salt);
    System.out.println("Encoded string: " + encodedStr);


    byte[] decodeData = decode(encodedStr);
    System.out.println("Decoded data");
    printBuffer(decodeData);
}
catch (Exception ex)
{
  ex.printStackTrace();
}

  }

  static protected void printBuffer( byte[] byteArray)
  {
//    com.gridnode.security.mime.PKCS7BodyPart.printBuffer(byteArray);
  }

}
