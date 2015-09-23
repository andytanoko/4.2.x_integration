/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SMimeSecurityInfo
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 14-June-2002    Jagadeesh           Created.
 * 28-DEC-2002     Jagadeesh           Added: CertJ,EncryptionAlgorithm,
 *                                     EncryptionLevel.
 * 08 July 2009    Tam Wei Xiang       #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib
 */

package com.gridnode.pdip.base.security.helpers;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import com.gridnode.pdip.base.security.mime.*;

public class SMimeSecurityInfo implements Serializable
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7009706011148924990L;
	/**
   * This Class collects a list of common parameters and state variables. Among the
   * items tracked are the list of currently registered service providers.
   */
	//private CertJ _certJ;
  //private CertPathCtx _pathCtx;
  //private CertJ _certJ;

  private String _encryptionAlgorithm;
  private String _digestAlgorithm;

  private int _encryptionLevel;

  private X509Certificate _partnerCertificate;
  private X509Certificate _signerCertificate;
  private X509Certificate _ownCertificate;

  private PrivateKey _privateKey;

  private IMailpart _partToEncrypt;
  private IPart _parttoDecrypt;

  private IMailpart _parttoSign;

  private IMime _parttoVerify;
  private IMime _verifiedPart;

  private byte[] _dataToEncrypt;
  private byte[] _encryptedData;

  private byte[] _dataToDecrypt;
  private byte[] _decryptedData;

  private byte[] _datatoSign;
  private byte[] _signedData;

  private byte[] _dataToVerify;
  private byte[] _signatureToVerify;

  private com.gridnode.pdip.base.security.mime.IMailpart _partToCompress;
  private com.gridnode.pdip.base.security.mime.IPart _parttoDeCompress;
  private byte[] _dataToCompress;
  private byte[] _compressedData;
  private byte[] _dataToDeCompress;
  private byte[] _deCompressedData;
  private int _compressMethod;
  private int _compressLevel;

  

  public void setOwnCertificate(X509Certificate ownCertificate)
  {
    _ownCertificate = ownCertificate;
  }

  public void setEncryptionAlgoritm(String encryptAlgorithm)
  {
    _encryptionAlgorithm = encryptAlgorithm;
  }

  public void setDigestAlgorithm(String digestAlgorithm)
  {
    _digestAlgorithm = digestAlgorithm;
  }

  public void setEncryptionLevel(int encryptLevel)
  {
    _encryptionLevel = encryptLevel;
  }

  public void setPrivateKey(PrivateKey privateKey)
  {
    _privateKey = privateKey;
  }

//  public void setCertPathContext(CertPathCtx pathContext)
//  {
//    _pathCtx = pathContext;
//  }

  public void setReceipentCertificate(X509Certificate receipentCertificate)
  {
    _partnerCertificate = receipentCertificate;
  }


  public void setDataToDecrypt(byte[] dataToDecrypt)
  {
    _dataToDecrypt = dataToDecrypt;
  }

  public void setDataToEncrypt(byte[] dataToEncrypt)
  {
    _dataToEncrypt = dataToEncrypt;
  }

  public void setDataToSign(byte[] datatosign)
  {
      _datatoSign = datatosign;
  }

  public void setIMailpartToEncrypt(IMailpart parttoEncrypt)
  {
    _partToEncrypt = parttoEncrypt;
  }

  public void setIPartToDecrypt(IPart parttoDecrypt)
  {
    _parttoDecrypt = parttoDecrypt;
  }

  public void setIMailpartToSign(IMailpart parttoSign)
  {
    _parttoSign = parttoSign;
  }

  public void setEncryptedData(byte[] encryptedData)
  {
    _encryptedData =  encryptedData;
  }

  public void setDecryptedData(byte[] decryptedData)
  {
   _decryptedData = decryptedData;
  }

  public void setSignerCertificate(X509Certificate signerCertificate)
  {
    _signerCertificate = signerCertificate;
  }

  public void setSignedData(byte[] signedData)
  {
    _signedData = signedData;
  }

  public void setPartToVerify(IMime parttoVerify)
  {
    _parttoVerify = parttoVerify;
  }

  public void setDataToVerify(byte[] dataToVerify)
  {
    _dataToVerify = dataToVerify;
  }

  public void setSignatureToVerify(byte[] signatureToVerify)
  {
    _signatureToVerify = signatureToVerify;
  }

  public IMime getPartToVerify()
  {
    return _parttoVerify;
  }

  public void setVerifiedPart(IMime verifiedPart)
  {
    _verifiedPart = verifiedPart;
  }

  public IMime getVerifiedPart()
  {
    return _verifiedPart;
  }

  public IMailpart getPartToSign()
  {
    return _parttoSign;
  }

  public byte[] getDataToSign()
  {
    return _datatoSign;
  }


//  public JSAFE_PrivateKey getPrivateKey()
//  {
//    return _privateKey;
//  }

  public X509Certificate getOwnCertificate()
  {
    return _ownCertificate;
  }

  public X509Certificate getSignerCertificate()
  {
    return _signerCertificate;
  }

  public byte[] getDecryptedData()
  {
    return _decryptedData;
  }
  public byte[] getEncryptedData()
  {
    return _encryptedData;
  }

  public IPart getIPartToDecrypt()
  {
    return _parttoDecrypt;
  }

  public byte[] getSignedData()
  {
    return _signedData;
  }

  public byte[] getDataToVerify()
  {
    return _dataToVerify;
  }

  public byte[] getSignatureToVerify()
  {
    return _signatureToVerify;
  }

  public IPart getEncryptedPart()
  {
      IPart pkcsBodyPart = null;
      if(_encryptedData != null)
      {
         try
         {
             pkcsBodyPart = new PKCS7BodyPart();
             pkcsBodyPart.setContent(_encryptedData,null);
             pkcsBodyPart.setContentType("application/pkcs7-mime");
             pkcsBodyPart.setParameter("smime-type", "enveloped-data");
             pkcsBodyPart.setParameter("name", "smime.p7m");
         }
         catch(Exception ex)
         {
           ex.printStackTrace();
         }
      }
      return pkcsBodyPart;
  }

  public IMailpart getDecryptedPart()
  {
     IMailpart part = null;
     try
     {
          part = GNMimeUtility.generatePart(_decryptedData);
     }
    catch(Exception e)
    {
       e.printStackTrace();
    }
    return part;
  }


  public IMime getSignedPart()
  {
    GNMimepart    mimepart = new GNMimepart();
    GNBodypart pkcs7BodyPart = new PKCS7BodyPart();
    try
    {
        if( _signedData != null  && _parttoSign != null)
        {
           pkcs7BodyPart.setContent(_signedData,null);
           pkcs7BodyPart.setContentType("application/pkcs-7signature");
           pkcs7BodyPart.setParameter("name", "smime.p7s");
           pkcs7BodyPart.setFilename("smime.p7s");
           mimepart.addPart(_parttoSign);
           mimepart.addPart(pkcs7BodyPart);
           mimepart.setSubType("signed");
           mimepart.setParameter("protocol", "application/pkcs7-signature");
           mimepart.setParameter("micalg", "sha1");
           return mimepart;
       }
     }
     catch(Exception ex)
     {
       ex.printStackTrace();
     }
    return null;
  }

//  public CertJ getCertJ()
//  {
//    return _certJ;
//  }

  public byte[] getDataToEncrypt()
  {
    return _dataToEncrypt;
  }

  public byte[] getDataToDecrypt()
  {
    return _dataToDecrypt;
  }

  public IMailpart getIMailpartToEncrypt()
  {
    return _partToEncrypt;
  }

  public X509Certificate getReceipentCertificate()
  {
    return _partnerCertificate;
  }

//  public CertPathCtx getCertPathContext()
//  {
//    return _pathCtx;
//  }
//
//  public CertJ getCertJ()
//  {
//    return _certJ;
//  }

  public String getEncryptionAlgorithm()
  {
    return _encryptionAlgorithm;
  }

  public String getDigestAlgorithm()
  {
    return _digestAlgorithm;
  }

  public int getEncryptionLevel()
  {
    return _encryptionLevel;
  }

  public void setIMailPartToCompress(com.gridnode.pdip.base.security.mime.IMailpart partToCompress)
  {
    this._partToCompress = partToCompress;
  }

  public com.gridnode.pdip.base.security.mime.IMailpart getIMailPartToCompress()
  {
    return _partToCompress;
  }

  public void setIParttoDeCompress(com.gridnode.pdip.base.security.mime.IPart parttoDeCompress)
  {
    this._parttoDeCompress = parttoDeCompress;
  }

  public com.gridnode.pdip.base.security.mime.IPart getIParttoDeCompress()
  {
    return _parttoDeCompress;
  }

  public void setDataToCompress(byte[] dataToCompress)
  {
    this._dataToCompress = dataToCompress;
  }

  public byte[] getDataToCompress()
  {
    return _dataToCompress;
  }

  public void setCompressedData(byte[] compressedData)
  {
    this._compressedData = compressedData;
  }

  public byte[] getCompressedData()
  {
    return _compressedData;
  }

  public void setDataToDeCompress(byte[] dataToDeCompress)
  {
    this._dataToDeCompress = dataToDeCompress;
  }

  public byte[] getDataToDeCompress()
  {
    return _dataToDeCompress;
  }

  public void setDeCompressedData(byte[] deCompressedData)
  {
    this._deCompressedData = deCompressedData;
  }

  public byte[] getDeCompressedData()
  {
    return _deCompressedData;
  }

  public void setCompressMethod(int compressMethod)
  {
    this._compressMethod = compressMethod;
  }

  public int getCompressMethod()
  {
    return _compressMethod;
  }

  public void setCompressLevel(int compressLevel)
  {
    this._compressLevel = compressLevel;
  }

  public int getCompressLevel()
  {
    return _compressLevel;
  }
  //  public IPart getIPart

  public PrivateKey getPrivateKey()
  {
    return _privateKey;
  }
  

  public SMimeSecurityInfo()
  {
  }

}