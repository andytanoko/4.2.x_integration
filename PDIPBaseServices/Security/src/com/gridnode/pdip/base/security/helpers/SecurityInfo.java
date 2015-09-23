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
 * 29-May-2002    Jagadeesh           Created.
 * 19-Apr-2004    Guo Jianyu          Added encryptionAlgorithm and compressionLevel
 */

package com.gridnode.pdip.base.security.helpers;

import java.io.*;
/**
 * SecurityInfo is (implementation/reference) to the Value Object, which
 * encapsulates security information, insted of passing each value - parameter,
 * which tightly couples the remote mehtod and its implementation.
*/


public class SecurityInfo implements ISecurityInfo
{ 

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6189405952005403185L;
	private byte[] _receipentCertificate;
  private byte[] _senderCertificate;
  private byte[] _ownPrivateKey;
  private char[] _password;
  private String _digestAlgorithm;

  private int _keyLength;
  private boolean _sign = false;

  private String _fileToEncrypt;
  /* Encrypted Output File */
  private String _encrydecryptOutputFile;

  //private File _tempFile;

  /* File to Decrypt */
  private String _fileToDecrypt;

//  private File _outputFile;

  private String _certificateFile;

  private String _privateKeyFile;

 /* String to Encrypt */
  private String _stringToEncrypt;

/* byte[] represents Data To Encrypt */
  private byte[] _dataToEncrypt;

  private String _encryptionAlgorithm;

/* String[] to Encrypt */
  private String[] _stringArrayToEncrypt;

/* byte[] represents Data To Encrypt */

  private byte[] _dataToDecrypt;

 /* String to Decrypt */
  private String _stringToDecrypt;

 /* String[] to Decrypt */
  private  String[] _stringArrayToDecrypt;

   /* Encrypted Data */
  private byte[] _encryptedData;

  /* Decrypted Data */
  private byte[] _decryptedData;

 /*  encrypted String Array */
  private String[] _encryptedStringArray;

 /* Decrypted String Array */
  private String[] _decryptedStringArray;

  /* Security Level */
  /** @todo Consider to initilize to default security level i.e 1 */
  private int _securitylevel;

  private int _compressionLevel;

  public void setDigestAlgorithm(String digestAlgorithm)
  {
    _digestAlgorithm = digestAlgorithm;
  }

  public void setReceipentCertificate(byte[] recpcertificate)
  {
    _receipentCertificate = recpcertificate;
  }

  public void setSenderCertificate(byte[] sendcertificate)
  {
    _senderCertificate = sendcertificate;
  }

  public void setOwnPrivateKey(byte[] ownprivatekey)
  {
    _ownPrivateKey = ownprivatekey;
  }

  public void setPassword(char[] password)
  {
    _password = password;
  }

  public void setKeyLength(int keylength)
  {
    _keyLength = keylength;
  }

  public void setSign(boolean sign)
  {
    _sign = sign;
  }

  public void setFileToEncrypt(String toencryptfile)
  {
    _fileToEncrypt = toencryptfile;
  }

  public void setOutputFile(String outputFile)
  {
    _encrydecryptOutputFile = outputFile;
  }

  public void setStringToEncrypt(String toEncryptString)
  {
    _stringToEncrypt = toEncryptString;
  }

  public void setDataToEncrypt(byte[] dataToEncrypt)
  {
    _dataToEncrypt = dataToEncrypt;
  }

  public void setStringArryToEncrypt(String[] dataToEncrypt)
  {
    _stringArrayToEncrypt = dataToEncrypt;
  }

  public void setStringArryToDecrypt(String[] dataToDecrypt)
  {
    _stringArrayToDecrypt = dataToDecrypt;
  }

  public void setDataToDecrypt(byte[] dataToDecrypt)
  {
    _dataToDecrypt = dataToDecrypt;
  }

  public void setStringToDecrypt(String toDecryptString)
  {
    _stringToDecrypt = toDecryptString;
  }

/*  public void setTempFile(File tempfile)
  {
    _tempFile = tempfile;
  }
*/

  public void setFileToDecrypt(String decryptfile)
  {
    _fileToDecrypt = decryptfile;
  }

  public void setEncryptedData(byte[] encryptdata)
  {
    _encryptedData = encryptdata;
  }

  public void setEncryptedStringArray(String[] encryptedStringArray)
  {
    _encryptedStringArray = encryptedStringArray;
  }

  public void setEncryptionAlgorithm(String encryptionAlgorithm)
  {
    _encryptionAlgorithm = encryptionAlgorithm;
  }

  public void setReceipentCertificateFile(String certificatekeyFile)
  {
    _certificateFile = certificatekeyFile;
  }

  public void setSenderCertificateFile(String certificatekeyFile)
  {
    _certificateFile = certificatekeyFile;
  }

  public void setOwnPrivateKeyFile(String privatekeyFile)
  {
    _privateKeyFile = privatekeyFile;
  }

  public void setDecryptedData(byte[] decryptData)
  {
    _decryptedData = decryptData;
  }

  public void setDecryptedStringArray(String[] decryptedStringArray)
  {
    _decryptedStringArray = decryptedStringArray;
  }

  public void setSecuritylevel(int level)
  {
    _securitylevel = level;
  }

  public void setCompressionLevel(int level)
  {
    _compressionLevel = level;
  }

  public byte[] getDataToEncrypt()
  {
    return _dataToEncrypt;
  }

  public String[] getStringArrayToEncrypt()
  {
    return _stringArrayToEncrypt;
  }

  public byte[] getDataToDecrypt()
  {
    return _dataToDecrypt;
  }

  public String[] getStringArrayToDecrypt()
  {
    return _stringArrayToDecrypt;
  }

  public String getFileToDecrypt()
  {
    return _fileToDecrypt;
  }

  public int getCompressionLevel()
  {
    return _compressionLevel;
  }

  public String getDigestAlgorithm()
  {
    return _digestAlgorithm;
  }

  /**
   * returns Encrypted Data in byte array
   * in File Format.
   * @return
   */
  public File getEncryptedOutputFile()
  {
    try
    {
     if(_encrydecryptOutputFile != null)
     {
        File f = new File(_encrydecryptOutputFile);
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(_encryptedData);
        return f;
     }
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
      return null;
  }


  public String getStringToEncrypt()
  {
        return _stringToEncrypt;
  }


  public String getOutputFile()
  {
    return _encrydecryptOutputFile;
  }




  public String getFileToEncrypt()
  {
     return _fileToEncrypt;
  }


  /**
   * returns Decrypted Data in byte array
   * in String Format.
   * @return String
   */
  public String getEncryptedOutputString()
  {
      return new String(_encryptedData);
  }


  public byte[] getEncryptedOutputByte()
  {
    return _encryptedData;
  }

  public String[] getEncryptedStringArray()
  {
    return _encryptedStringArray;
  }

  public String getEncryptionAlgorithm()
  {
    return _encryptionAlgorithm;
  }
  /**
   * Returns Decrypted Data(in byte Array)
   * in
   * @return
   */

/*  public File getDecryptedFile()
  {
    return _decryptFile;
  }
*/
 /**
  * Returns Decrypted
  * @return
  */

  public String getStringToDecrypt()
  {
    return _stringToDecrypt;
  }

  public byte[] getDecryptedOutputByte()
  {
    return _decryptedData;
  }

  public String getDecryptedOutputString()
  {
    return new String(_decryptedData);
  }

  public File getDecryptedOutputFile()
  {
    try
    {
     if(_encrydecryptOutputFile != null)
     {
        File f = new File(_encrydecryptOutputFile);
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(_decryptedData);
        return f;
     }
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
    }
      return null;

  }

  public String[] getDecryptedStringArray()
  {
    return _decryptedStringArray;
  }

  public byte[] getReceipentCertificate()
  {
    return _receipentCertificate;
  }

  public String getReceipentCertificateFile()
  {
    return _certificateFile;
  }

  public String getSenderCertificateFile()
  {
    return _certificateFile;
  }

  public byte[] getSenderCertificate()
  {
    return _senderCertificate;
  }

  public byte[] getOwnPrivateKey()
  {
    return _ownPrivateKey;
  }

  public String getOwnPrivateKeyFile()
  {
    return _privateKeyFile;
  }

  public char[] getPassword()
  {
    return _password;
  }

  public int getKeyLength()
  {
    if(_keyLength == 0)
    {
      return ISecurityInfo.ENCRYPT_STRING_KEYLENGTH;
    }
    return _keyLength;
  }

  public boolean isSign()
  {
      return (_sign == true)? true : false;
  }

  public int getSecuritylevel()
  {
    return _securitylevel;
  }


  public SecurityInfo()
  {
  }


  }