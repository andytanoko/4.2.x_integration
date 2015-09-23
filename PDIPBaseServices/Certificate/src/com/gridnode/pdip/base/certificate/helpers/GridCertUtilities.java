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
 * 08 Jun 2009      Tam Wei Xiang     #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib
 */

package com.gridnode.pdip.base.certificate.helpers;

import com.gridnode.pdip.base.certificate.exceptions.ILogErrorCodes;
import com.gridnode.pdip.base.certificate.model.IX500Name;
import com.gridnode.pdip.base.transport.handler.HTTPTransportHandler;
import com.gridnode.pdip.base.transport.helpers.JavaKeyStoreHandler;
//import com.rsa.certj.cert.*;
//import com.rsa.jsafe.*;

import javax.crypto.Cipher;
import javax.crypto.EncryptedPrivateKeyInfo;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.mail.internet.MimeUtility;
import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509ExtensionsGenerator;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.PrincipalUtil;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.security.AlgorithmParameters;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

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
                                                       pbeAlgorithm, getSecurityProvider());
      Cipher             cipher = Cipher.getInstance(pbeAlgorithm, getSecurityProvider());
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
      CertificateLogger.error(ILogErrorCodes.PKCS8_PRIVATE_KEY, "Error writting PKCS8 Private Key", ex);
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
      AlgorithmParameters param = privateKeyInfo.getAlgParameters();
      
      SecretKeyFactory   secretKeyFact = SecretKeyFactory.getInstance(param.getAlgorithm(), getSecurityProvider());
      Cipher cipher = Cipher.getInstance(param.getAlgorithm(), getSecurityProvider());
      cipher.init(Cipher.DECRYPT_MODE, secretKeyFact.generateSecret(pbeKeySpec), privateKeyInfo.getAlgParameters());
      
      PKCS8EncodedKeySpec pkcs8Spec = privateKeyInfo.getKeySpec(cipher);
      
      KeyFactory          keyFact = KeyFactory.getInstance("RSA", getSecurityProvider());
      PrivateKey          privKey = keyFact.generatePrivate(pkcs8Spec);
      
      return privKey;
    }
    catch(Exception ex)
    {
      CertificateLogger.error(ILogErrorCodes.PKCS_PRIVATE_KEY_LOAD, "Error in loading the PKCS8 Private Key", ex);
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
  public static PublicKey loadPublicKeyJava (String fileName)
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
                                        PublicKey publicKey)
  {
    byte[] data = writePublicKeyToByte(publicKey);
    return writeByteTOFile(fileName,data, data.length);
  }

  /**
   * Save an X509Certificate object to a byte array
   *
   * @param fileName The name of the file to store the certificate in.
   * @param certificate The <code>X509Certificate</code> object to store.
   * @return X509Cert in byte form 
   * 
   */
  public static byte[] writeX509Certificate(java.security.cert.X509Certificate certificate)
    {
      try
      {
        return certificate.getEncoded();
      }
      catch(Exception e)
      {
        CertificateLogger.error(ILogErrorCodes.X509_CERT_WRITE, "Error writing X509 cert to byte array.", e);
        return null;
      }
  }
  
  private static Number getCorrespondIX500Name(DERObjectIdentifier oid, Hashtable<DERObjectIdentifier, Number> x500ConstantFields)
  {
    return x500ConstantFields.get(oid);
  }
  
  private static Hashtable<DERObjectIdentifier, Number> getX500Fields()
  {
    Hashtable<DERObjectIdentifier, Number> derOIDs = new Hashtable<DERObjectIdentifier, Number>();
    derOIDs.put(X509Name.C, IX500Name.COUNTRY);
    derOIDs.put(X509Name.ST, IX500Name.STATE);
    derOIDs.put(X509Name.O, IX500Name.ORGANIZATION);
    derOIDs.put(X509Name.L, IX500Name.LOCALITY);
    derOIDs.put(X509Name.OU, IX500Name.ORGANIZATIONAL_UNIT);
    derOIDs.put(X509Name.STREET, IX500Name.STREET_ADDRESS);
    derOIDs.put(X509Name.CN, IX500Name.COMMAN_NAME);
    derOIDs.put(X509Name.T, IX500Name.TITLE);
    derOIDs.put(X509Name.EmailAddress, IX500Name.EMAIL_ADDRESS);
    derOIDs.put(X509Name.BUSINESS_CATEGORY, IX500Name.BUSINESS_CATEORY);
    derOIDs.put(X509Name.TELEPHONE_NUMBER, IX500Name.TELEPHONE_NUMBER);
    derOIDs.put(X509Name.POSTAL_CODE, IX500Name.POSTAL_CODE);
    return derOIDs;
  }
  
  public static X509Principal getX509IssuerPrincipal(X509Certificate cert) throws CertificateEncodingException
  {
    return PrincipalUtil.getIssuerX509Principal(cert);
  }
  
  public static X509Principal getX509SubjectPrincipal(X509Certificate cert) throws CertificateEncodingException
  {
    return PrincipalUtil.getSubjectX509Principal(cert);
  }
  
  public static Hashtable getX500Constants(X509Principal bcPrincipal) throws Exception
  {
    Vector oidList = bcPrincipal.getOIDs();
    Hashtable<DERObjectIdentifier, Number> x500ConstantFields = getX500Fields();
    Hashtable<Number, String> keyConstants = new Hashtable<Number, String>();
    
    for(Iterator i = oidList.iterator(); oidList != null && oidList.size() > 0 && i.hasNext() ;)
    {
      DERObjectIdentifier oid = (DERObjectIdentifier)i.next();
      Number x500NameField = getCorrespondIX500Name(oid, x500ConstantFields);
      
      if(x500NameField != null)
      {
        Vector oidValues = bcPrincipal.getValues(oid);
        if(oidValues != null && oidValues.size() > 0)
        {
          String oidValue = (String)oidValues.iterator().next();
          keyConstants.put(x500NameField, oidValue);
        }
      }
    }
    return keyConstants;
  }
  
  public static boolean writeX509Certificate (String fileName,
                                              java.security.cert.X509Certificate certificate)
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
  public static java.security.cert.X509Certificate loadX509Certificate (String fileName)
    throws Exception
  {
    return loadX509Certificate(loadFileToByte(fileName));
  }
  
  /**
   * 
   * Load an X509Certificate object from a specified file.  If this
   * function is able to load the certificate from the file, return
   * the object, otherwise, return <code>null</code>.
   *
   * @param fileName The name of the file containing the X.509 Certificate.
   * @return An <code>X509Certificate</code> object containing the
   * certificate, or <code>null</code>. */
  public static java.security.cert.X509Certificate loadX509Certificate (File certFile)
  {
    return loadX509Certificate(loadFileToByte(certFile));
  }

  public static java.security.cert.X509Certificate loadX509CertificateByString(String certificateData)
  {
    return loadX509Certificate(decode(certificateData));
  }
  
  public static java.security.cert.X509Certificate loadX509Certificate (byte[] certificateData)
  {
    try
    {
      /* 040526NSL this method only support DER-encoded format.
      X509Certificate cert = new X509Certificate (certificateData, 0, 0);
      return (cert); */
    return JavaKeyStoreHandler.loadCertificate(certificateData);
    }
    catch (Exception ex)
    {
      ex.printStackTrace ();
    }
    return null;
  }

  //TWX bc edition
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
  public static java.security.cert.X509CRL loadX509CRL (String fileName)
  {
    return loadX509CRL(loadFileToByte(fileName));
  }

  //BC version
  public static java.security.cert.X509CRL loadX509CRL (byte[] crlData)
  {
    try
    {
      ByteArrayInputStream input = new ByteArrayInputStream(crlData);
      CertificateFactory   fact = CertificateFactory.getInstance("X.509", getSecurityProvider());

      return (java.security.cert.X509CRL)fact.generateCRL(input);

    }
    catch (Exception ex)
    {
      CertificateLogger.error(ILogErrorCodes.X509CRL_INVOKE, "Error in loading X509CRL", ex);
      return (null);
    }
  }

  public static byte[] getMessageDigest(String digestAlgorithm, byte[] data) //throws GNException
  {
    MessageDigest digester = null;
    try
    {
      digester = MessageDigest.getInstance(digestAlgorithm, getSecurityProvider());
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
    
    byte[] digest = null;
    
    try
    {
      digester.update(data, 0, data.length);
      digest = digester.digest();
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
    
    return digest;
  }
  
  public static byte[] getMD5(byte[] data) //throws GNException
  {
    //JSAFE_MessageDigest digester = null;
    MessageDigest digester = null;
    
    //Initialize the MD5 digester
    try
    {
      digester = MessageDigest.getInstance("MD5", getSecurityProvider());
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
//      println("%MD5-F-NCRYPT; Exception caught while " +
//                         "digesting." );
//      println("; " + anyException.toString() );
//      anyException.printStackTrace (new PrintStream (this.getOutputStream ()));
//      System.exit(4);
     // GNException.throwEx(EX_MD5, "Error in digesting data", ex);
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
  
  public static java.security.cert.X509Certificate loadCertificateFromString(String certificateData)
  {
    return loadX509Certificate(loadByteArrayFromString(certificateData));
  }

  public static java.security.cert.X509Certificate loadCertificateFromByte (byte[] certificateData)
  {
    return loadX509Certificate(certificateData);
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

  public static String writePrivateKeyToString(PrivateKey privateKey)
  {
    return writeByteArrayToString(writePrivateKeyToByte(privateKey));
  }
  
  public static byte[] writePrivateKeyToByte(PrivateKey privateKey)
  {
    return writePKCS8PrivateKeyData(getPrivatePassword(), privateKey);
  }
  
  static public java.security.cert.X509CRL loadCRLFromString(String crlData)
  {
    return loadCRLFromByte(loadByteArrayFromString(crlData));
  }

  static public java.security.cert.X509CRL loadCRLFromByte(byte[] crlData)
  {
      return loadX509CRL(crlData);
  }

   public static byte[] writeX509CRLToByte (java.security.cert.X509CRL crl)
    {
      try
      {
        byte[] crlData = crl.getEncoded();
        return crlData;
      }
      catch(Exception e)
      {
        CertificateLogger.error(ILogErrorCodes.X509CRL_INVOKE, "Error writing X509CRL to byte", e);
        return null;
      }
   }

   
   public static PublicKey loadPublicKeyFromString(String keyData)
   {
    return loadPublicKeyFromByte(loadByteArrayFromString(keyData));
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
                                                   getSecurityProvider());
       PublicKey pubKey = keyFact.generatePublic(x509Spec);
       
       return pubKey;
     }
     catch(Exception e)
     {
       CertificateLogger.error(ILogErrorCodes.PUBLIC_KEY_LOAD, "Error in loading public key", e);
       return null;
     }
   }
  
  public static String writeCertificateToString(java.security.cert.X509Certificate certificate)
  {
    return writeByteArrayToString(writeCertificateToByte(certificate));
  }

 
  static public byte[] writeCertificateToByte(java.security.cert.X509Certificate certificate)
  {
   return writeX509Certificate(certificate);
  }
  
  public static String writePublicKeyToString(PublicKey publicKey)
  {
    return writeByteArrayToString(writePublicKeyToByte(publicKey));
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

  
  static public String writeIssuerNameToString(X500Principal issuerName)
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
      Cipher cipher = Cipher.getInstance("RSA/NONE/OAEPWithMD5AndMGF1Padding", getSecurityProvider());
      cipher.init(Cipher.ENCRYPT_MODE, publicKey);
      cipherOut = cipher.doFinal(encryptMe);
      
    } 
    catch (Exception ex) 
    {
      CertificateLogger.error(ILogErrorCodes.KEY_PAIR_MATCHING, "Error in performing encryption for matching key", ex);
    }

    // Now that we have some encrypted data, let's decrypt it with the
    // private key and compare the recovered plaintext with the original.
    try
    {
      Cipher cipher = Cipher.getInstance("RSA/NONE/OAEPWithMD5AndMGF1Padding", getSecurityProvider());
      cipher.init(Cipher.DECRYPT_MODE, privateKey);
      recoveredText = cipher.doFinal(cipherOut);
      
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
      CertificateLogger.error(ILogErrorCodes.KEY_PAIR_MATCHING, "Error in recovering plain text for matching key", ex);
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

  //TWX BC version
  static public boolean exportJavaKeyStore(String entryname, java.security.cert.X509Certificate[] cert, Key key)
  {
    try
    {
      JavaKeyStoreHandler handler = new JavaKeyStoreHandler();
      handler.open();
      handler.insert(entryname,key,cert);
      //HTTPTransportHandler http = new HTTPTransportHandler();
      HTTPTransportHandler.sendCMD_SetKeyStore(handler.writeToByteArray());
      return true;
    }
    catch (Exception ex)
    {
      return false;
    }

  }

//TWX BC version
  public static boolean exportTrustedJavaKeyStore(String entryname, java.security.cert.X509Certificate cert)
  {
    try
    {
      JavaKeyStoreHandler handler = new JavaKeyStoreHandler();
      handler.open();
      handler.insert(entryname,cert);
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

//  public static void main(String[] args)
//  {
////    com.gridnode.security.mime.PKCS7BodyPart.setDebug(true);
//    byte[] salt = {
//      (byte)0xFF, (byte)0x12, (byte)0x34, (byte)0x56,
//      (byte)0xAF, (byte)0x4B, (byte)0x9A, (byte)0x77,
//      (byte)0x78, (byte)0x9A, (byte)0xBC, (byte)0xDE,
//      (byte)0xCD, (byte)0xAB, (byte)0x81
//    };
//
//    System.out.println("Test byte[] : ");
//    printBuffer(salt);
//try
//{
//    String encodedStr = encode(salt);
//    System.out.println("Encoded string: " + encodedStr);
//
//
//    byte[] decodeData = decode(encodedStr);
//    System.out.println("Decoded data");
//    printBuffer(decodeData);
//    
//    
//    //TWX test getMD5 31052009
//    String digest = GridCertUtilities.encode((GridCertUtilities.getMD5("haha2".getBytes())));
//    System.out.println("MD5 digest: "+digest);
//    
//    digest = GridCertUtilities.encode((GridCertUtilities.getMD5BC("haha2".getBytes())));
//    System.out.println("BC MD5 digest: "+digest);
//    
//    //twx test load PKCS#8 encrypted private key
//    FileInputStream in = new FileInputStream(new File("c:/encodedData.txt"));
//    ByteArrayOutputStream out = new ByteArrayOutputStream();
//    byte[] buffer = new byte[512];
//    int readSoFar = 0;
//    while( (readSoFar = in.read(buffer)) > -1)
//    {
//      out.write(buffer, 0, readSoFar);
//    }
//    //byte[] privateKeyBase64 = out.toByteArray();
//    //byte[] privateKey = GridCertUtilities.decode(new String(privateKeyBase64));
//    GridCertUtilities.loadPKCS8PrivateKeyDataBC("hello".toCharArray(), out.toByteArray());
//    
//    //TWX test load Public Key
//    in = new FileInputStream(new File("c:/publicKey.txt"));
//    out = new ByteArrayOutputStream();
//    buffer = new byte[512];
//    readSoFar = 0;
//    while( (readSoFar = in.read(buffer)) > -1)
//    {
//      out.write(buffer, 0, readSoFar);
//    }
//    byte[] publicKeyBase64 = out.toByteArray();
//    byte[] publicKey = GridCertUtilities.decode(new String(publicKeyBase64));
//    GridCertUtilities.loadPublicKeyFromByteBC(publicKey);
//    
//}
//catch (Exception ex)
//{
//  ex.printStackTrace();
//}
//
//  }

  static protected void printBuffer( byte[] byteArray)
  {
//    com.gridnode.security.mime.PKCS7BodyPart.printBuffer(byteArray);
  }
  
  public static X509Extensions getX509ExtensionsFromCert(java.security.cert.X509Certificate cert)
  {
    Set<String> nonCriticalExtensionsOID = cert.getNonCriticalExtensionOIDs();
    Set<String> criticalExtensionsOID = cert.getCriticalExtensionOIDs();
    X509ExtensionsGenerator gen = new X509ExtensionsGenerator();
    
    if(nonCriticalExtensionsOID != null && nonCriticalExtensionsOID.size() > 0)
    {
      for(String oid : nonCriticalExtensionsOID)
      {
        byte[] extensionValue = cert.getExtensionValue(oid);
        gen.addExtension(new DERObjectIdentifier(oid), false, extensionValue);
        System.out.println("Non-critical X509Extensions: OID"+oid+" value:"+new String(extensionValue));
      }
    }
    
    if(criticalExtensionsOID != null && criticalExtensionsOID.size() > 0)
    {
      for(String oid : criticalExtensionsOID)
      {
        byte[] extensionValue = cert.getExtensionValue(oid);
        gen.addExtension(new DERObjectIdentifier(oid), true, extensionValue);
        System.out.println("Critical X509Extensions: OID"+oid+" value:"+new String(extensionValue));
      }
    }
    
    return gen.generate();
  }

  //TWX move from CertificateEntityHandler
  public static boolean isValid(java.security.cert.X509Certificate cert)
  {
    if (cert == null) return false;
    
    try
    {
      cert.checkValidity();
      return true;
    }
    catch(CertificateExpiredException ex)
    {
      CertificateLogger.warn("Certificate is expired", ex);
      return false;
    }
    catch(CertificateNotYetValidException ex)
    {
      CertificateLogger.warn("Certificate is not valid", ex);
      return false;
    }
    
     
  }
  
  /**
   * TWX 26 Nov 2008 get the cert path related to the target cert we passed in
   * @param certSelector the criteria which we can get a list of cert 
   * @param isEnableRevocationCheck the CRL checking
   * @param targetCert the end cert entity in the cert path
   * @param provider the JCA provider
   * @return PKIXCertPathBuilderResult
   * @throws Exception
   */
  public static PKIXCertPathBuilderResult getCertPathBuilderResult(X509CertSelector certSelector, boolean isEnableRevocationCheck,
                                                                   java.security.cert.X509Certificate targetCert, String provider) 
    throws Exception
  {
    JavaKeyStoreHandler handler = new JavaKeyStoreHandler();
    handler.open(getTructStoreName(), getTructStorePassword());
    KeyStore ks = handler.getKs();
    return JavaKeyStoreHandler.getPKIXCertPathBuilder(ks, certSelector, isEnableRevocationCheck, targetCert, provider);
  }
  
}
