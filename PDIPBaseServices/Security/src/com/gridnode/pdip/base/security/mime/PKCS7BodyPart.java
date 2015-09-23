/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: PKCS7BodyPart.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 10 Aug 2001    Lim Soon Hsiung     Initial creation GT 1.1
 * 04 Nov 2003    Zou Qingsong        Enhancement for Compression
 * 17 Jul 2009    Tam Wei Xiang       #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib
 */

package com.gridnode.pdip.base.security.mime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.X509CertSelector;

import org.bouncycastle.cms.CMSEnvelopedData;
import org.bouncycastle.cms.CMSEnvelopedDataGenerator;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.RecipientId;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.RecipientInformationStore;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;

import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;
import com.gridnode.pdip.base.security.helpers.GridCertUtilities;
import com.gridnode.pdip.base.security.helpers.SecurityLogger;
//import com.rsa.certj.CertJ;
//import com.rsa.certj.cert.X509Certificate;
//import com.rsa.certj.pkcs7.*;
//import com.rsa.certj.spi.path.CertPathCtx;
/**
 * Title:        GridNode Security
 * Description:  GridNode Security Module
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode Pte Ltd
 * @author Lim Soon Hsiung
 * @version 1.1
 */
public class PKCS7BodyPart extends GNBodypart //implements IMimeExceptionValue
{
  static private boolean  bDebug = false;
  static private boolean  bException = false;

  static public void setDebug(boolean bDebug)
  {
    PKCS7BodyPart.bDebug = bDebug;
  }

  static public void setException(boolean bException)
  {
    PKCS7BodyPart.bException = bException;
  }

  public PKCS7BodyPart()
  {

  }

  static public void print(String message)
  {
    if(bDebug)
      System.out.print(message);
  }

  static public void println(String message)
  {
    if(bDebug)
      System.out.println(message);
  }

  static public void println()
  {
    if(bDebug)
      System.out.println("");
  }

  public static IPart encrypt(java.security.cert.X509Certificate partnerCertificate, IMailpart partToEncryped)
    throws SecurityServiceException
  {
    try
    {
      PKCS7BodyPart pkcs7BodyPart = new PKCS7BodyPart();
      byte[] contentInfoEncoding = encrypt(partnerCertificate, partToEncryped.getContentByte(false));
      pkcs7BodyPart.setContent(contentInfoEncoding,null);
      pkcs7BodyPart.setContentType("application/pkcs7-mime");
      pkcs7BodyPart.setParameter("smime-type", "enveloped-data");
      pkcs7BodyPart.setParameter("name", "smime.p7m");
      pkcs7BodyPart.setFilename("smime.p7m");
      return pkcs7BodyPart;
    }
    catch(SecurityServiceException gnEx)
    {
      throw gnEx;
    }
    catch(Exception e)
    {
      if(bException)
        e.printStackTrace();
  
      throw new SecurityServiceException("Encrypt exception ", e);
  //    GNException.throwEx(EX_ENCRYPT, "Encrypt exception ", e);
  //    return null;
    }
  }
  
//  static public IPart encrypt(CertJ certJ, CertPathCtx pathCtx , X509Certificate partnerCertificate, IMailpart partToEncryped)
//    throws SecurityServiceException
//  {
//    try
//    {
//      PKCS7BodyPart pkcs7BodyPart = new PKCS7BodyPart();
//      byte[] contentInfoEncoding = encrypt(certJ, pathCtx, partnerCertificate, partToEncryped.getContentByte(false));
//      pkcs7BodyPart.setContent(contentInfoEncoding,null);
////      pkcs7BodyPart.setContentType("application/pkcs7-mime;smime-type=enveloped-data;name=something.p7m" );
//      pkcs7BodyPart.setContentType("application/pkcs7-mime");
//      pkcs7BodyPart.setParameter("smime-type", "enveloped-data");
//      pkcs7BodyPart.setParameter("name", "smime.p7m");
////      pkcs7BodyPart.addHeader("Content-Disposition","attachment;filename=something.p7m");
//      pkcs7BodyPart.setFilename("smime.p7m");
//      return pkcs7BodyPart;
//    }
//    catch(SecurityServiceException gnEx)
//    {
//      throw gnEx;
//    }
//    catch(Exception e)
//    {
//      if(bException)
//        e.printStackTrace();
//
//      throw new SecurityServiceException("Encrypt exception ", e);
////      GNException.throwEx(EX_ENCRYPT, "Encrypt exception ", e);
////      return null;
//    }
//  }

  static public IMailpart decrypt(PrivateKey privateKey, java.security.cert.X509Certificate cert, IPart partToDecryped)
    throws SecurityServiceException
  {
    IMailpart part = null;
    try
    {
      byte[] contentInfoEncoding = (byte[])partToDecryped.getContent(IMailpart.OUTPUT_BYTE_ARRAY);
      byte[] contentData = decrypt(privateKey, cert, contentInfoEncoding);
      part = GNMimeUtility.generatePart(contentData);
    }
    catch(SecurityServiceException gnEx)
    {
      throw gnEx;
    }
    catch(Exception e)
    {
      if(bException)
        e.printStackTrace();
      throw new SecurityServiceException("Decrypt exception ", e);
  //    GNException.throwEx(EX_DECRYPT, "Decrypt exception ", e);
  //    return null;
    }
  
    return part;
  }
  
//  static public IMailpart decrypt(CertJ certJ, CertPathCtx pathCtx, IPart partToDecryped)
//    throws SecurityServiceException
//  {
//    IMailpart part = null;
//    try
//    {
//      byte[] contentInfoEncoding = (byte[])partToDecryped.getContent(IMailpart.OUTPUT_BYTE_ARRAY);
//      byte[] contentData = decrypt(certJ, pathCtx, contentInfoEncoding);
//      part = GNMimeUtility.generatePart(contentData);
//    }
//    catch(SecurityServiceException gnEx)
//    {
//      throw gnEx;
//    }
//    catch(Exception e)
//    {
//      if(bException)
//        e.printStackTrace();
//      throw new SecurityServiceException("Decrypt exception ", e);
////      GNException.throwEx(EX_DECRYPT, "Decrypt exception ", e);
////      return null;
//    }
//
//    return part;
//  }

  public static IMime sign(PrivateKey privateKey, java.security.cert.X509Certificate signerCertificate, IMailpart partToSign)
  throws SecurityServiceException
{
  try
  {
     GNMimepart    mimepart = new GNMimepart();
     PKCS7BodyPart pkcs7BodyPart = new PKCS7BodyPart();

     byte[] contentInfoEncoding = sign(privateKey, signerCertificate, partToSign.getContentByte(false));
     pkcs7BodyPart.setContent(contentInfoEncoding,null);
//     pkcs7BodyPart.setContentType("application/pkcs-7signature;name=something.p7s" );
    pkcs7BodyPart.setContentType("application/pkcs-7signature");
    pkcs7BodyPart.setParameter("name", "smime.p7s");

//     pkcs7BodyPart.addHeader("Content-Desposition","attachment;filename=something.p7s");
    pkcs7BodyPart.setFilename("smime.p7s");
     mimepart.addPart(partToSign);
     mimepart.addPart(pkcs7BodyPart);
     //mimepart.setSubType("signed;protocol=application/pkcs7-signature;micalg=sha1");
     mimepart.setSubType("signed");
     mimepart.setParameter("protocol", "application/pkcs7-signature");
     mimepart.setParameter("micalg", "sha1");
//     mimepart.setDescription("This is a Signed RosettaNet Business Message");
     return mimepart;
  }
  catch(SecurityServiceException gnEx)
  {
    throw gnEx;
  }
  catch(Exception e)
  {
    if(bException)
      e.printStackTrace();

    throw new SecurityServiceException("Sign Exception", e);
//    return null;
  }
}

  
//  static public  IMime sign(CertJ certJ, CertPathCtx pathCtx, X509Certificate signerCertificate, IMailpart partToSign)
//    throws SecurityServiceException
//  {
//    try
//    {
//       GNMimepart    mimepart = new GNMimepart();
//       PKCS7BodyPart pkcs7BodyPart = new PKCS7BodyPart();
//
//       byte[] contentInfoEncoding = sign(certJ, pathCtx, signerCertificate, partToSign.getContentByte(false));
//       pkcs7BodyPart.setContent(contentInfoEncoding,null);
////       pkcs7BodyPart.setContentType("application/pkcs-7signature;name=something.p7s" );
//      pkcs7BodyPart.setContentType("application/pkcs-7signature");
//      pkcs7BodyPart.setParameter("name", "smime.p7s");
//
////       pkcs7BodyPart.addHeader("Content-Desposition","attachment;filename=something.p7s");
//      pkcs7BodyPart.setFilename("smime.p7s");
//       mimepart.addPart(partToSign);
//       mimepart.addPart(pkcs7BodyPart);
//       //mimepart.setSubType("signed;protocol=application/pkcs7-signature;micalg=sha1");
//       mimepart.setSubType("signed");
//       mimepart.setParameter("protocol", "application/pkcs7-signature");
//       mimepart.setParameter("micalg", "sha1");
////       mimepart.setDescription("This is a Signed RosettaNet Business Message");
//       return mimepart;
//    }
//    catch(SecurityServiceException gnEx)
//    {
//      throw gnEx;
//    }
//    catch(Exception e)
//    {
//      if(bException)
//        e.printStackTrace();
//
//      throw new SecurityServiceException("Sign Exception", e);
////      return null;
//    }
//  }

  public static IMailpart verify(IMime partToVerify, java.security.cert.X509Certificate partnerCert)
    throws SecurityServiceException
  {
    try
    {
      GNBodypart    bodyPart = (GNBodypart)partToVerify.getPart(0);
      IMime mimepart = bodyPart.getMultipart();
      byte[] contentInfoEncoding = (byte[])mimepart.getContentByte(false);
      GNBodypart    signatureBodyPart = (GNBodypart)partToVerify.getPart(1);
      byte[] signature  = (byte[])signatureBodyPart.getContent(IMailpart.OUTPUT_BYTE_ARRAY);
      if(verify(contentInfoEncoding, signature, partnerCert))
        return mimepart;
      else
        throw new SecurityServiceException("Verify failed");
    }
    catch(SecurityServiceException gnEx)
    {
      throw gnEx;
    }
    catch(Exception e)
    {
      if(bException)
        e.printStackTrace();
  
      throw new SecurityServiceException("Verify Exception", e);
    }
  }
  
//  static public IMailpart verify(CertJ certJ, CertPathCtx pathCtx, IMime partToVerify)
//    throws SecurityServiceException
//  {
//    try
//    {
//      GNBodypart    bodyPart = (GNBodypart)partToVerify.getPart(0);
//      IMime mimepart = bodyPart.getMultipart();
//      byte[] contentInfoEncoding = (byte[])mimepart.getContentByte(false);
//      GNBodypart    signatureBodyPart = (GNBodypart)partToVerify.getPart(1);
//      byte[] signature  = (byte[])signatureBodyPart.getContent(IMailpart.OUTPUT_BYTE_ARRAY);
//      if(verify(certJ, pathCtx, contentInfoEncoding, signature))
//        return mimepart;
//      else
//        throw new SecurityServiceException("Verify failed");
////        GNException.throwEx(EX_VERIFY, "Verify failed");
//    }
//    catch(SecurityServiceException gnEx)
//    {
//      throw gnEx;
//    }
//    catch(Exception e)
//    {
//      if(bException)
//        e.printStackTrace();
//
//      throw new SecurityServiceException("Verify Exception", e);
////      GNException.throwEx(EX_VERIFY, "Verify Exception", e);
////      return null;
//    }
////    return null;
//  }

  public static  byte[] encrypt(java.security.cert.X509Certificate partnerCertificate, byte[] contentToEncry)
    throws SecurityServiceException
  {
    try
    {
      SecurityLogger.log("--------------------Encrypt Raw Data: Start--------------------");
      SecurityLogger.log("Encrypt Raw Data: RecipientCert IssuerName " + partnerCertificate.getIssuerX500Principal().getName());
      SecurityLogger.log("Encrypt Raw Data: RecipientCert IssuerNumber ");
      //printBuffer(partnerCertificate.getSerialNumber());
  
      //data.setEncryptionAlgorithm ("RC5-0x10-32-16/CBC/PKCS5Padding", 128);
        CMSProcessable content = new CMSProcessableByteArray(contentToEncry);
        CMSEnvelopedDataGenerator gen = new CMSEnvelopedDataGenerator();
        gen.addKeyTransRecipient(partnerCertificate);
        
        SecurityLogger.log(
                           " Recipent Information "
                             + partnerCertificate.getIssuerX500Principal()
                             + " "
                             + partnerCertificate.getSerialNumber()
                             + " "
                             + partnerCertificate.getSerialNumber().toByteArray().length);
        
        //data.setEncryptionAlgorithm("RC5-0x10-32-16/CBC/PKCS5Padding", 128);
        //TWX 20090716 CertJ IMPL is using the RC5 algo for generating the symmetrix key; but we could not find
        //the replacement in BC impl. Such method is refered by SMIMESecurityServiceFacade.encrypt(...)
        //and the SMIMESecurityServiceFacade.encrypt(...) is not refered by anyone else.
        
        CMSEnvelopedData enveloped = gen.generate(content, CMSEnvelopedDataGenerator.DES_EDE3_CBC, 168, GridCertUtilities.getSecurityProvider());
        return enveloped.getEncoded();
    }
    catch(Exception e)
    {
      println("Encrypt Raw Data: Exception ");
      println("--------------------Encrypt Raw Data: End--------------------");
      if(bException)
        e.printStackTrace();
  
      throw new SecurityServiceException("Encrypt exception ", e);
  //    GNException.throwEx(EX_ENCRYPT, "Encrypt exception ", e);
  //    return null;
    }
  }
  
//  static public byte[] encrypt(CertJ certJ, CertPathCtx pathCtx, X509Certificate partnerCertificate, byte[] contentToEncry)
//    throws SecurityServiceException
//  {
//    try
//    {
//      println("--------------------Encrypt Raw Data: Start--------------------");
////      println("Encrypt Raw Data: RawData ");
////      printBuffer(contentToEncry);
//      println("Encrypt Raw Data: RecipientCert IssuerName " + partnerCertificate.getIssuerName().toString());
//      println("Encrypt Raw Data: RecipientCert IssuerNumber ");
//      printBuffer(partnerCertificate.getSerialNumber());
//
//      //PKCS7BodyPart pkcs7BodyPart = new PKCS7BodyPart();
//      byte[] contentInfoEncoding = null;
//      EnvelopedData data = (EnvelopedData)ContentInfo.getInstance(ContentInfo.ENVELOPED_DATA, certJ, pathCtx);
//      ContentInfo content = createDataMessage (contentToEncry);
//      data.setContentInfo (content);
//      RecipientInfo recipient = new RecipientInfo();
//      recipient.setIssuerAndSerialNumber
//          (partnerCertificate.getIssuerName(),
//           partnerCertificate.getSerialNumber(), 0,
//           partnerCertificate.getSerialNumber().length);
//      recipient.setEncryptionAlgorithm ("RSA");
//      data.addRecipientInfo (recipient);
//      data.setEncryptionAlgorithm ("RC5-0x10-32-16/CBC/PKCS5Padding", 128);
//      int contentInfoEncodingLen = data.getContentInfoDERLen ();
//      contentInfoEncoding = new byte [contentInfoEncodingLen];
//      data.writeMessage (contentInfoEncoding, 0);
////      println("Encrypt Raw Data: Encrypted RawData ");
////      printBuffer(contentInfoEncoding);
//      println("--------------------Encrypt Raw Data: End--------------------");
//      return contentInfoEncoding;
//    }
//    catch(Exception e)
//    {
//      println("Encrypt Raw Data: Exception ");
//      println("--------------------Encrypt Raw Data: End--------------------");
//      if(bException)
//        e.printStackTrace();
//
//      throw new SecurityServiceException("Encrypt exception ", e);
////      GNException.throwEx(EX_ENCRYPT, "Encrypt exception ", e);
////      return null;
//    }
//  }

  public static byte[] decrypt(PrivateKey privateKey, java.security.cert.X509Certificate ownCert, byte[] partToDecryped)
    throws SecurityServiceException
  {
      try
      {
        CMSEnvelopedData envelop = new CMSEnvelopedData(partToDecryped);
        
        RecipientId recID = new RecipientId();
        recID.setIssuer(ownCert.getIssuerX500Principal().getEncoded());
        recID.setSerialNumber(ownCert.getSerialNumber());
        
        RecipientInformationStore recInfoStore = envelop.getRecipientInfos();
        RecipientInformation recInfo = recInfoStore.get(recID);
        
        if(recInfo != null)
        {
          return recInfo.getContent(privateKey, GridCertUtilities.getSecurityProvider());
        }
        else
        {
          throw new SecurityServiceException("No appropriate cert can be found for decryption ");
        }
      }
      catch(SecurityServiceException ex)
      {
        SecurityLogger.warn("Exception in Decrypt ", ex);
        throw ex;  
      }
      catch(Exception ex)
      {
        SecurityLogger.warn("Exception in Decrypt ", ex);
        throw new SecurityServiceException("Error in decrypting the content. Error is "+ex.getMessage(), ex);
      }
  }
  
//  static public byte[] decrypt(CertJ certJ, CertPathCtx pathCtx, byte[] partToDecryped)
//    throws SecurityServiceException
//  {
//    byte[] decryptdata = null;
//    try
//    {
//      println("--------------------Decrypt Raw Data: Start--------------------");
////      println("Decrypt Raw Data: RawData ");
////      printBuffer(partToDecryped);
//
//      byte[] contentInfoEncoding = partToDecryped;
//      EnvelopedData data = (EnvelopedData)ContentInfo.getInstance(ContentInfo.ENVELOPED_DATA, certJ, pathCtx);
//      data.readInit (contentInfoEncoding, 0, contentInfoEncoding.length);
//      data.readFinal ();
//      ContentInfo content = data.getContent ();
//
//      if(bDebug)
//      {
//        Vector recipientVector = data.getRecipientInfos ();
//        RecipientInfo[] recipients = new RecipientInfo[recipientVector.size()];
//        for (int index = 0 ; index < recipients.length ; index++)
//          recipients[index] = (RecipientInfo)recipientVector.elementAt (index);
//        for (int i = 0 ; i < recipients.length ; i++)
//        {
//          println("Decrypt Raw Data: RecipientCert IssuerName " + recipients[i].getIssuerName().toString());
//          println("Decrypt Raw Data: RecipientCert IssuerNumber ");
//          printBuffer(recipients[i].getSerialNumber());
//        }
//      }
//
//      if (content.getContentType() == ContentInfo.DATA)
//      {
//        decryptdata = ((Data)content).getData ();
////        println("Decrypt Raw Data: Decrypted RawData ");
////        printBuffer(decryptdata);
//        println("--------------------Decrypt Raw Data: End--------------------");
////        return decryptdata;
//      }
//      else
//        {
//          println("Decrypt Raw Data: Wrong with Decrypte");
//          println("--------------------Decrypt Raw Data: End--------------------");
//          throw new SecurityServiceException("Wrong ContentType for decrypted data");
////          GNException.throwEx(EX_DECRYPT, "Wrong ContentType for decrypted data");
////          return null;
//        }
//    }
//    catch(SecurityServiceException gnEx)
//    {
//      throw gnEx;
//    }
//    catch(Exception e)
//    {
//      println("Decrypt Raw Data: Exception ");
//      if(bException)
//        e.printStackTrace();
//        throw new SecurityServiceException("Decrypt exception ", e);
////      GNException.throwEx(EX_DECRYPT, "Decrypt exception ", e);
////      return null;
//    }
//
//    return decryptdata;
//  }

  public static byte[] sign(PrivateKey privateKey, java.security.cert.X509Certificate ownCertificate, byte[] partToSign) throws SecurityServiceException
  {
    try
    {
      CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
      gen.addSigner(privateKey, ownCertificate, CMSSignedDataGenerator.DIGEST_SHA1);
      
      //As per RFC3852, certs and CRLs are optional in the SignedData
      //TODO: further testing is needed. Compare the output of the RSA impl in both RNIF/AS2 Impl
      //gen.addCertificatesAndCRLs(certsAndCRLs);
      
      //Generate detach signed data
      CMSProcessable data = new CMSProcessableByteArray(partToSign);
      CMSSignedData signedData = gen.generate(data, GridCertUtilities.getSecurityProvider()); 
      
      return signedData.getEncoded();
    }
    catch(Exception ex)
    {
      if(bException)
      {
        ex.printStackTrace();
      }
      throw new SecurityServiceException("Error in signing: "+ex.getMessage(), ex);
    }
  }
  
//  static public  byte[] sign(CertJ certJ, CertPathCtx pathCtx, X509Certificate signerCertificate, byte[] partToSign)
//    throws SecurityServiceException
//  {
//    try
//    {
//       byte[] contentInfoEncoding = null;
//       println("--------------------Sign Raw Data: Start--------------------");
////       println("Sign Raw Data: RawData ");
////       printBuffer(partToSign);
//       SignedData data = (SignedData)ContentInfo.getInstance(ContentInfo.SIGNED_DATA, certJ, pathCtx);
//       ContentInfo content = createDataMessage (partToSign);
//       data.setContentInfo (content);
//       SignerInfo signer = new SignerInfo();
//       signer.setIssuerAndSerialNumber
//          (signerCertificate.getIssuerName(),
//           signerCertificate.getSerialNumber(), 0,
//           signerCertificate.getSerialNumber().length);
//        println("Sign Raw Data: SignerCert IssuerName " + signerCertificate.getIssuerName().toString());
//        println("Sign Raw Data: SignerCert IssuerNumber ");
//        printBuffer(signerCertificate.getSerialNumber());
//
//       signer.setEncryptionAlgorithm ("RSA");
//       signer.setDigestAlgorithm ("SHA1");
//       data.addSignerInfo (signer);
//       data.createDetachedSignature ();
//
//       int contentInfoEncodingLen = data.getContentInfoDERLen ();
//       contentInfoEncoding = new byte [contentInfoEncodingLen];
//       data.writeMessage (contentInfoEncoding, 0);
////       println("Sign Raw Data: Signature ");
////       printBuffer(contentInfoEncoding);
//
//       println("--------------------Sign Raw Data: End--------------------");
//       return contentInfoEncoding;
//     }
//    catch(Exception e)
//    {
//      println("Sign Raw Data: Exception");
//      if(bException)
//        e.printStackTrace();
//      println("--------------------Sign Raw Data: End--------------------");
//
//      throw new SecurityServiceException("Sign Exception", e);
////      GNException.throwEx(EX_SIGN, "Sign Exception", e);
////      return null;
//    }
//  }

  public static  boolean verify(byte[] contentInfoEncoding, byte[] signature, java.security.cert.X509Certificate partnerCert )
    throws SecurityServiceException
  {
    
    try
    {
      SecurityLogger.debug("Verifying CMS content");     
      CMSProcessable data = null;
      
      if(contentInfoEncoding != null)
      {
        data = new CMSProcessableByteArray(contentInfoEncoding);
      }
      
      CMSSignedData signedData = null;
      if(data == null) //content is part of the signature
      {
        SecurityLogger.debug("Signature encapsulated case");
        signedData = new CMSSignedData(signature);
      }
      else //signature detached case : signature and content to be verified is seperated
      {
        signedData = new CMSSignedData(data, signature);
      }
      
      SignerInformationStore signInfoStore = signedData.getSignerInfos();      
      
      if(signInfoStore != null && signInfoStore.getSigners() != null && signInfoStore.getSigners().size() > 0 )
      {
        SignerInformation signerInfo = (SignerInformation)signInfoStore.getSigners().iterator().next();
        X509CertSelector signerConstraint = signerInfo.getSID();
        SecurityLogger.debug("Signer certificate info: "+signerInfo.getSID());
        
        PKIXCertPathBuilderResult certPathBuildResult = com.gridnode.pdip.base.certificate.helpers.GridCertUtilities.
                                                                  getCertPathBuilderResult(signerConstraint, false, partnerCert, GridCertUtilities.getSecurityProvider());
        return signerInfo.verify(certPathBuildResult.getPublicKey(), GridCertUtilities.getSecurityProvider());  

        
        //remove following
//        PKIXCertPathBuilderResult certPathBuildResult = com.gridnode.pdip.base.certificate.helpers.GridCertUtilities.
//        getCertPathBuilderResult(signerConstraint, false, partnerCert, GridCertUtilities.SEC_PROVIDER_BC);
        //return signerInfo.verify(partnerCert, GridCertUtilities.SEC_PROVIDER_BC); 
      }
      else
      {
        SecurityLogger.log("No signer information found !");
        return false;
      }
    }
    catch(Exception ex)
    {
      if(bException)
      {
        ex.printStackTrace();
      }
      
      throw new SecurityServiceException("Can't verify CMS content: "+ex.getMessage(), ex);
    }
  }
  
//  static public boolean verify(CertJ certJ, CertPathCtx pathCtx, byte[] contentInfoEncoding, byte[] signature)
//    throws SecurityServiceException
//  {
//    try
//    {
//      println("--------------------Verify Raw Data: Start--------------------");
////      println("Verify Raw Data: RawData ");
////      printBuffer(contentInfoEncoding);
//
////      println("Verify Raw Data: Signature ");
////      printBuffer(signature);
//
//      SignedData data = (SignedData)ContentInfo.getInstance(ContentInfo.SIGNED_DATA, certJ, pathCtx);
//      data.setContentInfo(createDataMessage (contentInfoEncoding));
//      data.readInit (signature, 0, signature.length);
//      data.readFinal ();
//
//      Vector signerVector = data.getSignerInfos ();
//      SignerInfo[] signers = new SignerInfo[signerVector.size()];
//      for (int index = 0 ; index < signers.length ; index++)
//      {
//        signers[index] = (SignerInfo)signerVector.elementAt (index);
//      }
//      for (int i = 0 ; i < signers.length ; i++)
//      {
//        println("Verify Raw Data: SignCert IssuerName " + signers[i].getIssuerName().toString());
//        println("Verify Raw Data: SignCert IssuerNumber ");
//        printBuffer(signers[i].getSerialNumber());
//      }
//
//      println("Verify Raw Data: TRUE");
//      println("--------------------Verify Raw Data: End--------------------");
//      return true;
//    }
//    catch(Exception e)
//    {
//      if(bException)
//        e.printStackTrace();
//      println("Verify Raw Data: Exception");
//      println("Verify Raw Data: FALSE");
//      println("--------------------Verify Raw Data: End--------------------");
//
//      throw new SecurityServiceException("Verify failed");
////      GNException.throwEx(EX_VERIFY, "Verify failed");
////      return false;
//    }
//  }

//  static private ContentInfo createDataMessage (byte[] content)
//  {
//    ContentInfo data = null;
//    try
//    {
//      data = ContentInfo.getInstance (ContentInfo.DATA, null, null);
//      ((Data)data).setContent(content, 0, content.length);
//      return data;
//    }
//    catch (PKCS7Exception anyException)
//    {
//      return null;
//    }
//  }

    static public void printBuffer( byte[] byteArray)
    {
      StringBuffer textLine = new StringBuffer("                ");
      int length = byteArray.length;
      int offset = 0;
      print("  0000: ");
      for(int i=0; i < length; i++) {
        if( ( (i%16) == 0 ) && i != 0 ) {
          println( "[" + textLine + "]");
          print ("  " + hexString (i, 4) + ": ");
          for(int j=0; j<16; j++) {
            textLine.setCharAt(j,' ');
          }
        }
        print (hexString( (int) byteArray[i+offset], 2 ) + " ");
        if( (byteArray[i+offset] < 32) ||
            (byteArray[i+offset] > 127) ||
            (byteArray[i+offset] == 0x7f) ) {
          textLine.setCharAt(i%16,'.');
        } else {
          textLine.setCharAt(i%16,(char)byteArray[i+offset]);
        }
      }
      if( ( ( length % 16 ) != 0 ) || ( length == 0 ) ) {
        for( int i = 0 ; i < 16 - ( length % 16); i++) {
          print( "   " );
        }
      }
      println( "[" + textLine + "]");
  }

    static private String hexString ( int value, int padding )
    {
    String hexString = new String("0123456789ABCDEF");
    StringBuffer tempString = new StringBuffer
      ("                                                                              ".substring (0, padding));
    int offset = padding - 1;

    for(int i = 0 ; i < padding; i++) {
      tempString.setCharAt (offset - i,
                            hexString.charAt((value >> (i*4)) & 0xF));
    }
    return (tempString.toString());
  }


  public static byte[] getBytes(InputStream inputstream) throws IOException
  {
    int bufferSize = 1024;
    byte[] data;
    if (inputstream instanceof ByteArrayInputStream)
    {
      bufferSize = inputstream.available();
      data = new byte[bufferSize];
      inputstream.read(data, 0, bufferSize);
    }
    else
    {
      ByteArrayOutputStream bytearrayoutputstream
          = new ByteArrayOutputStream();
      data = new byte[bufferSize];
      int b;
      while ((b = inputstream.read(data, 0, bufferSize)) != -1)
          bytearrayoutputstream.write(data, 0, b);
      data = bytearrayoutputstream.toByteArray();
    }
    return data;
   }

//  public static void main(String[] args)
//  {
//    String dbPath = "C:/User/Qingsong/certj10/sample/db";
//    String memdbpath = "C:/User/Qingsong/db";
//    try
//    {
//
//    //X509Certificate cert = CertUtilities.loadX509Certificate(memdbpath + "/test.cer");
//    //JSAFE_PublicKey  publickey = CertUtilities.loadPublicKey(memdbpath + "/test.pub");
//
//
//    GNMimepart    mimepart = new GNMimepart();
//    GNBodypart    part = new GNBodypart();
//    part.setContent("This is a test content", "text/plain");
//    mimepart.addPart(part);
//
//    part = new GNBodypart();
//    part.setContent("This is a test content 2", "text/plain");
//    mimepart.addPart(part);
//
//    println(new String(mimepart.getContentByte(false)));
//
//   //load cert from file
///*    FileInputStream inputStream = new FileInputStream (dbPath + "/flatfile/certs/0010647B.cer");
//    byte[] certificateData = new byte[inputStream.available()];
//    inputStream.read (certificateData);
//    inputStream.close ();
//    X509Certificate cert = new X509Certificate (certificateData, 0, 0);
//*/
//    char[] password = { 'p', 'a', 's', 's', 'w', 'o', 'r', 'd' };
//
//    //Provider nativeProvider = null;
//    Provider ffProvider = null;
//    Provider pkixCertPathProvider = null;
//    Provider certStatusProvider = null;
//    Provider memProvider = new MemoryDB("Test Memory DB");
//
///*    nativeProvider = new NativeDB ("Native Database",
//                                   dbPath + "/native", "sampledb",
//                                   password, false);
//*/
//    ffProvider = new FlatFileDB ("FlatFile Database",
//                                 dbPath + "/flatfile", password);
//
//    pkixCertPathProvider = new PKIXCertPath ("PKIX Cert Path Verifier");
//    certStatusProvider = new CRLCertStatus ("Cert Status Provider");
//
//    Provider[] providers =
//    {
////        memProvider
////        nativeProvider,
//        ffProvider
//      ,pkixCertPathProvider
//       , certStatusProvider
//    };
//
//    Provider[] providers1 =
//    {
//        memProvider
////        nativeProvider, ffProvider
//      ,pkixCertPathProvider
//       ,certStatusProvider
//    };
//
//    CertJ certJ = new CertJ (providers);
//    DatabaseService dbService =
//        (DatabaseService)certJ.bindServices (CertJ.SPT_DATABASE);
//
//    X509Certificate cert = GridCertUtilities.loadX509Certificate(memdbpath + "/test.cer");
//    //JSAFE_PublicKey  publickey = cert.getSubjectPublicKey("Java");
//    JSAFE_PrivateKey privatekey = dbService.selectPrivateKeyByCertificate(cert);
//
//    GridCertUtilities.writePKCS8PrivateKeyFile("password",memdbpath+"/test.prv",privatekey);
////    GridCertUtilities.writePKCS8PrivateKey(memdbpath + "/test.prv", privatekey, "password");
//    privatekey = GridCertUtilities.loadPKCS8PrivateKeyFile("password",memdbpath+"/test.prv");
//
//
//
//    certJ.unbindService(dbService);
//    certJ.unregisterAll();
//
//    CertJ certJ1 = new CertJ (providers1);
//
//    DatabaseService dbService1 =
//        (DatabaseService)certJ1.bindServices(CertJ.SPT_DATABASE);
//
//
//    dbService1.insertCertificate(cert);
//    dbService1.insertPrivateKeyByCertificate(cert, privatekey);
//
//    Certificate [] certs = new Certificate[1];
//    certs[0] = cert;
//
//
//    CertPathCtx pathCtx = new CertPathCtx (CertPathCtx.PF_IGNORE_REVOCATION,
//                                 certs,
//                                 null, new Date(), dbService1);
//
//    /*CertPathCtx pathCtx = new CertPathCtx (CertPathCtx.PF_IGNORE_REVOCATION,
//                                 null,
//                                 null, new Date(), dbService);
//*/
///*
//    CertUtilities.writeX509Certificate(memdbpath + "/test.cer", cert);
//    CertUtilities.writePKCS8PrivateKey(memdbpath + "/test.prv", dbService.selectPrivateKeyByCertificate(cert), "password");
//    CertUtilities.writePublicKey(memdbpath + "/test.pub",cert.getSubjectPublicKey("Java"));
//*/
//
//        /* Get the certificate and print the necessary information. */
//
//
//    String content = "This is the message to do ...";
//    String content1 = "This is the message to do ....";
//
//  //  CertJ certJ = new CertJ ();
//    byte[] original_message = content.getBytes();
//    byte[] original_message1 = content1.getBytes();
//    byte[] encrypt_message = encrypt(certJ1, pathCtx, cert , original_message);
//    byte[] decrypt_message = decrypt(certJ1, pathCtx, encrypt_message);
//    byte[] signature_message = sign(certJ1, pathCtx, cert, original_message);
////    byte[] signature_message    = CertUtilities.getByteArrayFromFile(new File("c:/temp.sig"));
//
////    File file = CertUtilities.getFileFromByteArray(signature_message);
// //   System.out.println(file.getPath());
//    boolean verified = verify(certJ1 , pathCtx , original_message, signature_message);
///*    println("original message:" + new String(original_message));
//    println("encrypt message:" + new String(encrypt_message));
//    println("decrypt message:" + new String(decrypt_message));
//    println("Signature message:" + new String(signature_message));
//    */
//    if(verified)
//      println("verify message: true");
//    else
//      println("verify message: false");
//
///*    IMime  pkcs7 = PKCS7BodyPart.sign(certJ, pathCtx, cert, mimepart);
//    IMailpart part1 = PKCS7BodyPart.verify(certJ, pathCtx, pkcs7);
//*/
//  /*  if(part1 != null)
//    {
//      println(">>>>>>Verify Part Start:");
//      println(new String(part1.getContentByte(false)));
//      println(">>>>>>Verify Part End:");
//    }
//    else
//    {
//      println("Cannot verify");
//    }
//*/
// //   IPart  veri = PKCS7BodyPart.verify(certJ, pathCtx, pkcs7);
//
///*    IPart pkcs7 = PKCS7BodyPart.encrypt(certJ, pathCtx, cert, mimepart);
//    System.out.println("Pkcs7 encrypted content: \n" + new String(pkcs7.getContentByte(false)));
//    System.out.println("Pkcs7 encrypted end---------------------------------");
//
//    IMailpart   body1 = PKCS7BodyPart.decrypt(certJ, pathCtx, pkcs7);
//    System.out.println("Pkcs7 decrypted content: \n" + new String(body1.getContentByte(false)));
//    System.out.println("Pkcs7 decrypted end---------------------------------");
//*/
//    }
//    catch(Exception e)
//    {
//      e.printStackTrace();
//    }
//  }
}