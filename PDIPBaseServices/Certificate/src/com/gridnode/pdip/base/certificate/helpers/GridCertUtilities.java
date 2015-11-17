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
import com.gridnode.pdip.base.transport.handler.HTTPTransportHandler;
import com.gridnode.pdip.base.transport.helpers.JavaKeyStoreHandler;
import com.rsa.certj.cert.*;
import com.rsa.jsafe.*;

import javax.mail.internet.MimeUtility;

import java.io.*;
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
public class GridCertUtilities
{
//  public static final ExceptionValue EX_MD5 = new ExceptionValue("MD5 Exception ");
//  public static final ExceptionValue EX_ENCODE = new ExceptionValue("Encode Exception ");
//  public static final ExceptionValue EX_DECODE = new ExceptionValue("Decode Exception ");
//  public static final ExceptionValue EX_CERT = new ExceptionValue("Certificate Exception ");

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

  static public String writeByteArrayToString(byte[] data)
  {
      return encode(data);
  }

  static public byte[] loadByteArrayFromString(String data)
  {
      return decode(data);
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
                                              JSAFE_PrivateKey privateKey )
  {
    byte[] data = writePKCS8PrivateKeyData(passwordStr, privateKey);
    return writeByteTOFile(fileName,data, data.length);
  }

  public static byte[] writePKCS8PrivateKeyData (String passwordStr, JSAFE_PrivateKey privateKey )
  {
    JSAFE_SymmetricCipher encrypter = null;
    JSAFE_SecretKey key = null;
    byte[] privateKeyData;
    byte[] salt = {
      (byte)0x00, (byte)0x11, (byte)0x22, (byte)0x33,
      (byte)0x44, (byte)0x55, (byte)0x66, (byte)0x77
    };

    try {
      encrypter = JSAFE_SymmetricCipher.getInstance
        ("PBE/SHA1/DES/CBC/PKCS5PBE-5-56", "Java");
      encrypter.setSalt (salt, 0, salt.length);

      key = encrypter.getBlankKey ();
      char[] password = new char[20];
     // String response = caller.getResponse("Enter private key password");
      passwordStr.getChars (0, (passwordStr.length() > 20 ? 20 : passwordStr.length ()), password, 0);
     // key.setPassword (password, 0, password.length);
      key.setPassword (password, 0, passwordStr.length());

      encrypter.encryptInit (key, null);
      privateKeyData =
        encrypter.wrapPrivateKey (privateKey, true);
      return (privateKeyData);
    } catch (Exception anyException) {
      anyException.printStackTrace();
      CertificateLogger.log("Exception in writePKCS8PrivateKey "+anyException.getMessage());
      return (null);
    }
  }

  /**
   * Read a JSAFE_PrivateKey object from a file.  The input data needs
   * to be stored in EncryptedPrivateKeyInfo format, as defined in
   * PKCS #8.  The private key data will be decrypted using PKCS #5
   * DES PBE.  This function will query the user for the decryption
   * password.
   *
   * @param passwordStr The password string to encrypt the password file.
   * @param fileName The name of the file to load the encrypted key
   * from.
   * @return A JSAFE_PrivateKey object containing the key data, or null if failed. */
  public static JSAFE_PrivateKey loadPKCS8PrivateKeyFile(String passwordStr, String fileName)
  {
    return loadPKCS8PrivateKeyData(passwordStr, loadFileToByte(fileName));
  }

  /**
   * Read a JSAFE_PrivateKey object from memory.  The input data needs
   * to be stored in EncryptedPrivateKeyInfo format, as defined in
   * PKCS #8.  The private key data will be decrypted using PKCS #5
   * DES PBE.
   *
   * @param passwordStr The password string to encrypt the password file.
   * @param fileName The name of the file to load the encrypted key
   * from.
   * @return A JSAFE_PrivateKey object containing the key data, or null if failed. */

  public static JSAFE_PrivateKey loadPKCS8PrivateKeyData(String passwordStr, byte[] keyData )
  {
    char[] password = new char[passwordStr.length()];
    passwordStr.getChars (0, passwordStr.length(), password, 0);
    return loadPKCS8PrivateKeyData(password, keyData );
  }

  public static JSAFE_PrivateKey loadPKCS8PrivateKeyData(char[] password, byte[] keyData )
  {
    JSAFE_SymmetricCipher decrypter = null;
    JSAFE_SecretKey key = null;
    JSAFE_PrivateKey privateKey = null;

    try {
      decrypter = JSAFE_SymmetricCipher.getInstance (keyData, 0, "Java");

      key = decrypter.getBlankKey ();
     // String response = caller.getResponse("Enter private key password");
//      char[] password = new char[passwordStr.length()];
//      passwordStr.getChars (0, passwordStr.length(), password, 0);
      key.setPassword (password, 0, password.length);

      decrypter.decryptInit (key, null);

      privateKey =
        decrypter.unwrapPrivateKey (keyData, 0, keyData.length, true);
      return (privateKey);
    } catch (Exception anyException) {
      anyException.printStackTrace ();
      return (null);
    }
  }

  public static JSAFE_PrivateKey loadPKCS8PrivateKeyData(char[] password, String pkkeyData)
  {
    return loadPKCS8PrivateKeyData(password,decode(pkkeyData));
  }



  /**
   * Load a public key from a file.  The file is expected to contain
   * BER encoded key data.  If the key is loaded, return the
   * JSAFE_PublicKey object, otherwise return <code>null</code>.
   *
   * @param fileName The file name containing the public key data.
   * @return A JSAFE_PublicKey object or <code>null</code>.
   */
  public static JSAFE_PublicKey loadPublicKey (String fileName)
  {
    return loadPublicKeyFromByte(loadFileToByte(fileName));
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
                                        JSAFE_PublicKey publicKey)
  {
    byte[] data = writePublicKeyToByte(publicKey);
    return writeByteTOFile(fileName,data, data.length);
  }

  /**
   * Save an X509Certificate object to the specified file.  If the
   * operation is successful, return <code>true</code>, otherwise,
   * return <code>false</code>.
   *
   * @param fileName The name of the file to store the certificate in.
   * @param certificate The <code>X509Certificate</code> object to store.
   * @return <code>true</code> if the certificate was saved
   * successfully, <code>false</code> otherwise.  */
  static public byte[] writeX509Certificate(X509Certificate certificate)
    {
      try
      {
        byte[] certificateData = new byte[certificate.getDERLen (0)];
        certificate.getDEREncoding (certificateData, 0, 0);
        return certificateData;
      }
      catch(Exception e)
      {
        return null;
      }
  }

  static public Hashtable getX500Constants(X500Name name)
  {
    Hashtable keyConstants = new Hashtable();
    int count = name.getRDNCount();
    AttributeValueAssertion ava = null;
    RDN theRDN = null;

    for (int i = 0 ; i < count ; i++) {
      try {
        theRDN = name.getRDN (i);
        int attributeCount = theRDN.getAttributeCount();

        for (int j = 0 ; j < attributeCount ; j++) {
          try {
            ava = theRDN.getAttributeByIndex (j);
          } catch (NameException nameException) {
            continue;
          }

          switch (ava.getAttributeType()) {
          case AttributeValueAssertion.COUNTRY_NAME:
            keyConstants.put(IX500Name.COUNTRY,
                             ava.getStringAttribute());
            //buf.append("   Country:              ");
            break;
          case AttributeValueAssertion.STATE_NAME:
            keyConstants.put(IX500Name.STATE,
                             ava.getStringAttribute());
            //buf.append("   State:                ");
            break;
          case AttributeValueAssertion.ORGANIZATION_NAME:
            keyConstants.put(IX500Name.ORGANIZATION,
                             ava.getStringAttribute());
            //buf.append("   Organization:         ");
            break;
          case AttributeValueAssertion.LOCALITY_NAME:
            keyConstants.put(IX500Name.LOCALITY,
                             ava.getStringAttribute());
            //buf.append("   Locality:             ");
            break;
          case AttributeValueAssertion.ORGANIZATIONAL_UNIT_NAME:
            keyConstants.put(IX500Name.ORGANIZATIONAL_UNIT,
                             ava.getStringAttribute());
            //buf.append("   Organizational Unit:  ");
            break;
          case AttributeValueAssertion.STREET_ADDRESS:
            keyConstants.put(IX500Name.STREET_ADDRESS,
                             ava.getStringAttribute());
            //buf.append("   Street Address:       ");
            break;
          case AttributeValueAssertion.COMMON_NAME:
            keyConstants.put(IX500Name.COMMAN_NAME,
                             ava.getStringAttribute());
            //buf.append("   Common name:          ");
            break;
          case AttributeValueAssertion.TITLE:
            keyConstants.put(IX500Name.TITLE,
                             ava.getStringAttribute());
            //buf.append("   Title:                ");
            break;
          case AttributeValueAssertion.EMAIL_ADDRESS:
            keyConstants.put(IX500Name.EMAIL_ADDRESS,
                             ava.getStringAttribute());
            //buf.append("   Email Address:        ");
            break;
          case AttributeValueAssertion.BUSINESS_CATEGORY:
            keyConstants.put(IX500Name.BUSINESS_CATEORY,
                             ava.getStringAttribute());
            //buf.append("   Business Category:    ");
            break;
          case AttributeValueAssertion.TELEPHONE_NUMBER:
            keyConstants.put(IX500Name.TELEPHONE_NUMBER,
                             ava.getStringAttribute());
            //buf.append("   Telephone Number:     ");
            break;
          case AttributeValueAssertion.POSTAL_CODE:
            keyConstants.put(IX500Name.POSTAL_CODE,
                             ava.getStringAttribute());
            //buf.append("   Postal Code:          ");
            break;
          case AttributeValueAssertion.UNKNOWN_ATTRIBUTE_TYPE:
            keyConstants.put(IX500Name.UNKOWN_ATTRIBUTE_TYPE,
                             ava.getStringAttribute());
            //buf.append("   Unknown:              ");
            break;
          }

        }

      } catch (Exception nameException) {
//        buf.append("Error retrieving RDN.");
        continue;
      }
    }

    return keyConstants;
  }


  public static boolean writeX509Certificate (String fileName,
                                              X509Certificate certificate)
  {
    byte[] data = writeX509Certificate(certificate);
    return writeByteTOFile(fileName, data,data.length);
  }

  /**
   * Load an X509Certificate object from a specified file.  If this
   * function is able to load the certificate from the file, return
   * the object, otherwise, return <code>null</code>.
   *
   * @param fileName The name of the file containing the X.509 Certificate.
   * @return An <code>X509Certificate</code> object containing the
   * certificate, or <code>null</code>. */
  public static X509Certificate loadX509Certificate (String fileName)
    throws com.rsa.certj.cert.CertificateException,Exception
  {
    return loadX509Certificate(loadFileToByte(fileName));
  }


  /**
   * Load an X509Certificate object from a specified file.  If this
   * function is able to load the certificate from the file, return
   * the object, otherwise, return <code>null</code>.
   *
   * @param fileName The name of the file containing the X.509 Certificate.
   * @return An <code>X509Certificate</code> object containing the
   * certificate, or <code>null</code>. */
  public static X509Certificate loadX509Certificate (File certFile)
    throws com.rsa.certj.cert.CertificateException,Exception
  {
    return loadX509Certificate(loadFileToByte(certFile));
  }

  public static X509Certificate loadX509CertificateByString(String certificateData)
  {
    return loadX509Certificate(decode(certificateData));
  }

  public static X509Certificate loadX509Certificate (byte[] certificateData)
  {
    try
    {
      /* 040526NSL this method only support DER-encoded format.
      X509Certificate cert = new X509Certificate (certificateData, 0, 0);
      return (cert); */
	  return JavaKeyStoreHelper.convertCertificate(JavaKeyStoreHandler.loadCertificate(certificateData));
    }
    catch (Exception ex)
    {
      ex.printStackTrace ();
    }
    return null;
  }

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
  public static PKCS10CertRequest loadPKCS10CertRequest (String fileName) {
    try {
      FileInputStream inputStream = new FileInputStream (fileName);

      /* Create a byte array that is big enough to hold the contents
       * of the file. */
      byte[] certificateRequest = new byte[inputStream.available()];

      /* Read the data from the file. */
      inputStream.read (certificateRequest);
      inputStream.close ();

      /* Get an instance of a PKCS10CertRequest object using the data
       * just loaded. */
      PKCS10CertRequest request =
        new PKCS10CertRequest (certificateRequest, 0, 0);

      return (request);
    } catch (Exception anyException) {
      return (null);
    }
  }


  /**
   * Save a <code>PKCS10CertRequest</code> object to a specified file.
   *
   * @param fileName The name of the file to store the encoded
   * certificate request in.
   * @param request The <code>PCKS10CertRequest</code> object that
   * should be encoded and stored.  */
  public static boolean writePKCS10CertRequest (String fileName,
                                                PKCS10CertRequest request) {
    try {
      FileOutputStream outputStream = new FileOutputStream (fileName);

      /* Create a byte array big enough to hold the encoding of the
       * certificate request object. */
      byte[] certificateRequest = new byte[request.getDERLen(0)];

      /* DER encode the certificate request.  The encoding will be
       * stored in certificateRequest starting at offset 0. */
      request.getDEREncoding (certificateRequest, 0, 0);

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
  public static boolean writeX509CRL (String fileName, X509CRL crl) {
    try {
      FileOutputStream outputStream = new FileOutputStream (fileName);

      /* Get a byte array that is big enough to hold the CRL data. */
      byte[] crlData = new byte[crl.getDERLen (0)];

      /* Get the DER encoding of the CRL. */
      crl.getDEREncoding (crlData, 0, 0);

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
  public static X509CRL loadX509CRL (String fileName)
  {
    return loadX509CRL(loadFileToByte(fileName));
  }

  public static X509CRL loadX509CRL (byte[] crlData)
  {
    try
    {
        X509CRL theCRL = new X509CRL (crlData, 0, 0);
        return (theCRL);
    }
    catch (Exception anyException)
    {
      return (null);
    }
  }

  public static byte[] getMD5(File inputFile) //throws GNException
  {
    JSAFE_MessageDigest digester = null;
    int blockSize = 4096;
    byte [] inputBlock = new byte[ blockSize ];
    FileInputStream fis = null;


    //Initialize the MD5 digester
    try
    {
      digester = JSAFE_MessageDigest.getInstance( "MD5", "Java" );
    }
    catch (Exception ex)
    {
//      GNException.throwEx(EX_MD5, "Error in initializing MD5 digester", ex);
    }

    //Open the file
    try
    {
      fis = new FileInputStream( inputFile );
    }
    catch (Exception ex)
    {
    //  GNException.throwEx(EX_MD5, "Error reading file", ex);
    }

    byte [] digest = null;
    try
    {
      digester.digestInit( );
      int bytesRead = 0;

      while((bytesRead = fis.read( inputBlock )) != -1 )
      {
        digester.digestUpdate( inputBlock, 0, bytesRead );
      }
      digest = digester.digestFinal();

    }
    catch (Exception ex)
    {
      //GNException.throwEx(EX_MD5, "Error in digesting data", ex);
    }
    finally
    {
      if(digester != null)
        digester.clearSensitiveData();
    }

    return digest;
  }

  public static byte[] getMessageDigest(String digestAlgorithm, byte[] data) //throws GNException
  {
    JSAFE_MessageDigest digester = null;
    //Initialize digester
    try
    {
      digester = JSAFE_MessageDigest.getInstance(digestAlgorithm, "Java");
    }
    catch (Exception ex)
    {
    }

    byte[] digest = null;
    try
    {
      digester.digestInit();
      digester.digestUpdate(data, 0, data.length);

      digest = digester.digestFinal();

    } catch (Exception ex) {
    }
    finally
    {
      if(digester != null)
        digester.clearSensitiveData();
    }

    return digest;
  }

  public static byte[] getMD5(byte[] data) //throws GNException
  {
    JSAFE_MessageDigest digester = null;

    //Initialize the MD5 digester
    try
    {
      digester = JSAFE_MessageDigest.getInstance("MD5", "Java");
    }
    catch (Exception ex)
    {
//      println("[MD5]  Exception caught while " +
//                         "initializing the digester object." );
//      println("[MD5]  " + anyException.toString() );
//      anyException.printStackTrace (new PrintStream (this.getOutputStream ()));
//      System.exit(2);
//      GNException.throwEx(EX_MD5, "Error in initializing MD5 digester", ex);
    }

    byte[] digest = null;
    try
    {
      digester.digestInit();
      digester.digestUpdate(data, 0, data.length);

      digest = digester.digestFinal();

    } catch (Exception ex) {
//      println("%MD5-F-NCRYPT; Exception caught while " +
//                         "digesting." );
//      println("; " + anyException.toString() );
//      anyException.printStackTrace (new PrintStream (this.getOutputStream ()));
//      System.exit(4);
     // GNException.throwEx(EX_MD5, "Error in digesting data", ex);
    }
    finally
    {
      if(digester != null)
        digester.clearSensitiveData();
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

  public static X509Certificate loadCertificateFromString(String certificateData)
  {
    return loadX509Certificate(loadByteArrayFromString(certificateData));
  }

  public static X509Certificate loadCertificateFromByte (byte[] certificateData)
  {
    return loadX509Certificate(certificateData);
  }

  public static JSAFE_PrivateKey loadPrivateKeyFromString(String keyData)
  {
    return loadPrivateKeyFromByte(loadByteArrayFromString(keyData));
  }

  public static JSAFE_PrivateKey loadPrivateKeyFromByte(byte[] keyData)
   {
    return loadPKCS8PrivateKeyData(getPrivatePassword(), keyData);
  }
  
  private static String getPrivatePassword()
  {
  	SecurityDB secDB = null;
  	SecurityDBManager secDBManager = SecurityDBManager.getInstance(); //TWX 07082006
  	try
  	{
  		secDB = secDBManager.getSecurityDB();
  		return secDB.getPrivatePassword();
  	}
  	finally
  	{
  		if(secDB != null)
  		{
  			secDBManager.releaseSecurityDB(secDB);
  		}
  	}
  }
  
  public static String hexEncode (byte value)
  {
    char[] hex = new char[2];
    int lower = value & 0xf;
    if (lower < 0) lower+=16;
    int higher = value >> 4;
    if (higher < 0) higher+=16;
    if (lower < 10)
      hex[1] = (char)('0'+lower);
    else
      hex[1] = (char)('A'+ (lower - 10));
    if (higher < 10)
      hex[0] = (char)('0'+higher);
    else
      hex[0] = (char)('A'+ (higher - 10));
    return (new String (hex));
  }

  static public String writePrivateKeyToString(JSAFE_PrivateKey privateKey)
  {
    return writeByteArrayToString(writePrivateKeyToByte(privateKey));
  }

  static public byte[] writePrivateKeyToByte(JSAFE_PrivateKey privateKey)
  {
    return writePKCS8PrivateKeyData(getPrivatePassword(), privateKey);
  }

  static public X509CRL loadCRLFromString(String crlData)
  {
    return loadCRLFromByte(loadByteArrayFromString(crlData));
  }

  static public X509CRL loadCRLFromByte(byte[] crlData)
  {
      return loadX509CRL(crlData);
  }

   static public byte[] writeX509CRLToByte (X509CRL crl)
    {
      try
      {
        byte[] crlData = new byte[crl.getDERLen (0)];
        crl.getDEREncoding (crlData, 0, 0);
        return crlData;
      }
      catch(Exception e)
      {
        return null;
      }
   }


   static public JSAFE_PublicKey loadPublicKeyFromString(String keyData)
   {
    return loadPublicKeyFromByte(loadByteArrayFromString(keyData));
   }

   static public JSAFE_PublicKey loadPublicKeyFromByte (byte[] keyData)
    {
      JSAFE_PublicKey publicKey = null;
      try
      {
        publicKey = JSAFE_PublicKey.getInstance (keyData, 0, "Java");
        return publicKey;
      }
      catch(Exception e)
      {
        return null;
      }
  }

  static public String writeCertificateToString(X509Certificate certificate)
  {
    return writeByteArrayToString(writeCertificateToByte(certificate));
  }

 static public byte[] writeCertificateToByte (X509Certificate certificate)
  {
    return writeX509Certificate(certificate);
  }

  static public String writePublicKeyToString(JSAFE_PublicKey publicKey)
  {
    return writeByteArrayToString(writePublicKeyToByte(publicKey));
  }

  static public byte[] writePublicKeyToByte (JSAFE_PublicKey publicKey)
  {
      try
      {
        byte[][] keyData = publicKey.getKeyData ("RSAPublicKeyBER");
        return keyData[0];
      }
      catch(Exception e)
      {
        return null;
      }
  }

  static public String writeIssuerNameToString(X500Name issuerName)
  {
    try
    {
      return encode(issuerName.toString().getBytes());
    }
    catch(Exception e)
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

  static public boolean exportJavaKeyStore(String entryname, com.rsa.certj.cert.X509Certificate[] cert, JSAFE_PrivateKey jsafekey)
  {
    try
    {
      JavaKeyStoreHandler handler = new JavaKeyStoreHandler();
      handler.open();
      handler.insert(entryname,JavaKeyStoreHelper.convertPrivateKey(jsafekey),JavaKeyStoreHelper.convertCertificateChain(cert));
      //HTTPTransportHandler http = new HTTPTransportHandler();
      HTTPTransportHandler.sendCMD_SetKeyStore(handler.writeToByteArray());
      return true;
    }
    catch (Exception ex)
    {
      return false;
    }

  }

  static public boolean exportTrustedJavaKeyStore(String entryname, com.rsa.certj.cert.X509Certificate cert)
  {
    try
    {
      JavaKeyStoreHandler handler = new JavaKeyStoreHandler();
      handler.open();
      handler.insert(entryname,JavaKeyStoreHelper.convertCertificate(cert));
      //HTTPTransportHandler http = new HTTPTransportHandler();
      HTTPTransportHandler.sendCMD_SetTrustStore(handler.writeToByteArray());
      return true;
    }
    catch (Exception ex)
    {
      return false;
    }
  }

  static public String getTructStoreName()
  {
    return JavaKeyStoreHandler.getTrustStoreName("");
  }

  static public String getTructStorePassword()
  {
    return JavaKeyStoreHandler.getTrustStorePassword("");
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
