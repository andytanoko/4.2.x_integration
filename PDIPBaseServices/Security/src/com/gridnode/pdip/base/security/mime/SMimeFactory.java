/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SMimeFactory.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 08 Aug 2001    LSH/ZQS             Initial creation GT 1.1
 * 05 Dec 2001    Lim Soon Hsiung     Create PKCS7 signature and verify.
 * 20 Mar 2002    Lim Soon Hsiung     Modify trusted certs to used all avaliable
 *                                    certs in the cert store(instead of just my
 *                                    cert and partner cert)
 *
 * 10 Dec 2002    Jagadeesh           Modified: Added Changes since GT 1.3.8
 *                                    Changed: SMIMEFactory Constructor.
 * 28 Dec 2002    Jagadeesh           Modified: Added getInstance method and made
 *                                    Constructors private.
 *
 * 13 Jan 2003    Jagadeesh           Modified: Added DigestAlgorithm with
 *                                    Constructor.
 *
 * 30 Jan 2003    Jagadeesh           Modified: JavaMail compliance.
 *
 * 21 Feb 2003    Jagadeesh           Modified: Verify.
 *
 * 09 Jun 2003    Jagadeesh           Modified: Fixed bug of PKCS7 Header, while sign.
 * 02 Apr 2004    Guo Jianyu          Added setOwnCert() and setPartnerCert()

 * 05 Nov 2003    Zou Qingsong        Enhancement for Compression
 * 10 Nov 2005    Neo Sok Lay         Use ServiceLocator instead of ServiceLookup
 * 04 Jan 2008    Tam Wei Xiang       Enhancement for compression that conform to the RNIF compression spec.
 * 08 July 2009   Tam Wei Xiang     #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib
 */

package com.gridnode.pdip.base.security.mime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.rmi.RemoteException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Vector;
import java.util.zip.ZipOutputStream;

import javax.ejb.CreateException;

import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;
import com.gridnode.pdip.base.security.facade.ejb.ISecurityServiceHome;
import com.gridnode.pdip.base.security.facade.ejb.ISecurityServiceObj;
import com.gridnode.pdip.base.security.helpers.GridCertUtilities;
import com.gridnode.pdip.base.security.helpers.ISecurityInfo;
import com.gridnode.pdip.base.security.helpers.SMimeSecurityInfo;
import com.gridnode.pdip.base.security.helpers.SecurityLogger;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;
//import com.rsa.certj.CertJ;
//import com.rsa.certj.DatabaseService;
//import com.rsa.certj.cert.Certificate;
//import com.rsa.certj.cert.X509Certificate;
//import com.rsa.certj.spi.path.CertPathCtx;
//import com.rsa.jsafe.JSAFE_PrivateKey;
//import com.rsa.jsafe.JSAFE_PublicKey;
import javax.mail.internet.MimeBodyPart;

import org.bouncycastle.cms.CMSEnvelopedDataGenerator;
import org.bouncycastle.cms.CMSSignedDataGenerator;

/**
 *
 * @author Lim Soon Hsiung
 * @version 1.1
 */
public final class SMimeFactory
{

  public static final String ENCODING_BASE64            = "base64";
  public static final String ENCODING_QUOTED_PRINTABLE  = "quoted-printable";
  public static final String ENCODING_7BIT              = "7bit";
  public static final String ENCODING_8BIT              = "8bit";
  public static final String ENCODING_BINARY            = "binary";
  public static final String ENCODING_UUENCODE          = "uuencode";
  public static final String PKCS7_SIGNATURE_CONTENT_TYPE = "application/pkcs7-signature";
  public static final String PKCS7_MIME_TYPE =  "application/pkcs7-mime";

//  public static final String DEFAULT_DIGEST_ALGORITHM = "SHA1";
//  public static final String DEFAULT_ENCRYPTION_ALGORITHM = "RC5-0x10-32-16/CBC/PKCS5Padding";
  private static final String DEFAULT_ENCRYPTION_ALGORITHM = CMSEnvelopedDataGenerator.RC2_CBC;
  private static final String DEFAULT_CMS_DIGEST_ALGORITHM = CMSSignedDataGenerator.DIGEST_SHA1; 
  public static final String DEFAULT_DIGEST_ALGO = "SHA1";//to be displayed in the message header
  
  public static final int DEFAULT_ENCRYPTION_LEVEL =  128;
  public static final int UNDEFINED_ENCRYPTION_LEVEL =  -1;
  public static final int DEFAULT_COMPRESS_METHOD = ZipOutputStream.DEFLATED;
  public static final int DEFAULT_COMPRESS_LEVEL = 9;
 
  private PrivateKey ownPrivateKey;
  private PublicKey partnerPublicKey;
  private PublicKey ownPublicKey;
  private X509Certificate ownCert;
  private X509Certificate partnerCert;
  private String encoding = ENCODING_BASE64;

  private String _encryptionAlgorithm=null;
  private String _digestAlgorithm=null;
  private int _encryptionLevel=UNDEFINED_ENCRYPTION_LEVEL;

  private int _compressLevel=DEFAULT_COMPRESS_LEVEL;
  private int _compressMethod=DEFAULT_COMPRESS_METHOD;

  //private static byte[] _signedData={};

  private ISecurityServiceObj _securityServiceFacade = null;
//  private SecurityServiceBean _securityServiceFacade1 = new SecurityServiceBean();


  /**
   * CertJ and DBService are not Assigned with this constructor. This is because
   * the above two object instances are not seralizable.
   *
   * @param ownCertificate
   * @param partnerCert
   * @param encryptionAlgorithm
   * @param encryptionLevel
   * @throws SecurityServiceException
   */

  private SMimeFactory(X509Certificate ownCertificate,
                       X509Certificate partnerCert,
                       String encryptionAlgorithm,
                       String digestAlgorithm,
                       int encryptionLevel
                       )throws SecurityServiceException
  {
    try
    {
     /* if(_securityServiceFacade == null)
        _securityServiceFacade = lookupSecurityMgr();
     */
      this.ownCert = ownCertificate;
      this.partnerCert = partnerCert;
      this._encryptionAlgorithm = encryptionAlgorithm;
      this._digestAlgorithm = digestAlgorithm;
      this._encryptionLevel = encryptionLevel;

      if ( partnerCert == null )
      {
        partnerCert = ownCert;
      }
    }
    catch(Exception ex)
    {
      SecurityLogger.warn("Cannot Create SMimeFactory ",ex);
      throw new SecurityServiceException(ex);
    }
  }


  private SMimeFactory(X509Certificate ownCertificate,X509Certificate partnerCert)
      throws SecurityServiceException
  {
    try
    {
     /*
      if(_securityServiceFacade == null)
        _securityServiceFacade = lookupSecurityMgr();
     */
      this.ownCert = ownCertificate;
      this.partnerCert = partnerCert;
      if ( partnerCert == null )
      {
        partnerCert = ownCert;
      }
    }
    catch(Exception ex)
    {
      throw new SecurityServiceException(ex);
    }

  }



  /*************  Methods added since 28/12/2002    **************/



  public static SMimeFactory newInstance(
   X509Certificate ownCert,
   X509Certificate partnerCert,
   String encryptionAlgorithm,
   String digestAlgorithm,
   int encryptionLevel
  )throws SecurityServiceException
  {
     SMimeFactory  factory = new SMimeFactory(ownCert,
                                              partnerCert,
                                              encryptionAlgorithm,
                                              digestAlgorithm,
                                              encryptionLevel);
     return factory;

  }

  public static SMimeFactory newInstance(
   X509Certificate ownCert,
   X509Certificate partnerCert
  )throws SecurityServiceException
  {

     SMimeFactory factory = new SMimeFactory(ownCert,partnerCert);
     return factory;
  }


  public void setEncryptionAlgorithm(String encryptionAlgorithm)
  {
    _encryptionAlgorithm = encryptionAlgorithm;
  }

  public void setDigestAlgorithm(String digestAlgorithm)
  {
    _digestAlgorithm = digestAlgorithm;
  }

  public void setEncryptionLevel(int encryptionLevel)
  {
    _encryptionLevel = encryptionLevel;
  }

  /*
  public void setPathCtx(CertPathCtx pathCtx)
  {
    this.pathCtx = pathCtx;
  }*/

  public IMime generateMime(File mimeFile)
    throws SecurityServiceException
  {
    GNMimepart mime = new GNMimepart(mimeFile);
    return mime;
  }

  public IMime createMime(String subType)
    throws SecurityServiceException
  {
    IMime mime = createMime();
    mime.setSubType(subType);
    return mime;
  }

  public IMime createMime()
  {
    return new GNMimepart();
  }

  public IPart createPart(String content, String contentType)
    throws SecurityServiceException
  {
    GNBodypart part = new GNBodypart();
    part.setContent(content, "text/plain");
    part.setContentType(contentType);
    return part;
  }

  public IPart createPart()
    throws SecurityServiceException
  {
    return new GNBodypart();
  }

  public IPart createPart(String content)
    throws SecurityServiceException
  {
    return createPart(content, "text/plain");
  }

  public IPart createPart(File content, String contentType, String encoding)
    throws SecurityServiceException
  {
    // based on params, generate concrete Part object
    return new GNBodypart(content, contentType, encoding);
  }

  public IPart createPart(File content)
    throws SecurityServiceException
  {
    return new GNBodypart(content, "application/octet-stream", null);
  }

  public IPart createPart(byte[] content, String contentType)
    throws SecurityServiceException
  {
    return new GNBodypart(content, contentType);
  }

  public IPart createPart(byte[] content)
    throws SecurityServiceException
  {
    return createPart(content, "application/octet-stream");
  }

  public String getEncryptionAlogirthm()
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

  private SMimeSecurityInfo signData(byte[] partToSign)
    throws SecurityServiceException
  {
    try
    {
      SecurityLogger.debug("SMimeFactory][signData()]"+"Partner Certificate "+
      partnerCert);
      SMimeSecurityInfo smimeInfo = new SMimeSecurityInfo();
      smimeInfo.setOwnCertificate(ownCert);
      smimeInfo.setReceipentCertificate(partnerCert);

      smimeInfo.setEncryptionAlgoritm( (_encryptionAlgorithm == null) ?  DEFAULT_ENCRYPTION_ALGORITHM  : _encryptionAlgorithm );
      smimeInfo.setEncryptionLevel((_encryptionLevel == UNDEFINED_ENCRYPTION_LEVEL) ? DEFAULT_ENCRYPTION_LEVEL : _encryptionLevel);
      smimeInfo.setDigestAlgorithm((_digestAlgorithm == null) ? DEFAULT_CMS_DIGEST_ALGORITHM : getLocalizedDigestAlgo(_digestAlgorithm));

      smimeInfo.setPrivateKey(ownPrivateKey);
      smimeInfo.setDataToSign(partToSign);
      _securityServiceFacade = lookupSecurityMgr();
      SMimeSecurityInfo returnInfo = _securityServiceFacade.sign(smimeInfo);
      SecurityLogger.debug("[SMimeFactory][sign] After Sign Success ");
      return returnInfo;
    }
    catch(Exception ex)
    {
      SecurityLogger.debug("[SMimeFactory][encrypt()] Cannot Perform Encrypt");
      ex.printStackTrace();
      throw new SecurityServiceException("[SMimeFactory][encrypt()]"+
      " Cannot Perform Encrypt",ex);
    }
  }


  public byte[] sign(byte[] partToSign) throws SecurityServiceException
  {
    try
    {
      SMimeSecurityInfo smimeInfo = null;
      if(partToSign != null)
      {
        smimeInfo = signData(partToSign);
      }
      else
      {
        throw new SecurityServiceException(
        "Unable To Sign: Part to Sign is Null");
      }
      byte[] signedPart =smimeInfo.getSignedData();
      if(signedPart == null)
      {
        throw new SecurityServiceException("Unable to Sign");
      }
      return signedPart;
    }
    catch(Exception ex)
    {
      ex.printStackTrace();
      SecurityLogger.debug("[SMimeFactory][encrypt()] Cannot Perform Encrypt");
      throw new SecurityServiceException("[SMimeFactory][encrypt()]"+
      " Cannot Perform Encrypt",ex);
    }
   }

  public IMime sign(IMailpart partToSign) throws SecurityServiceException
  {
    try
    {
      if(partToSign != null)
      {
        byte[] dataToSign = partToSign.getContentByte(false);

        //Commented (byte[])partToSign.getContent(IMailpart.OUTPUT_BYTE_ARRAY);

        SMimeSecurityInfo signedInfo = signData(dataToSign);
        GNMimepart    mimepart = new GNMimepart();
        if( signedInfo != null )
        {
          byte[] signedData = signedInfo.getSignedData();
          GNBodypart pkcs7BodyPart = new GNBodypart();
          if( signedData != null )
          {
             pkcs7BodyPart.setContent(signedData,null);
             pkcs7BodyPart.setContentType(SMimeFactory.PKCS7_SIGNATURE_CONTENT_TYPE);
             pkcs7BodyPart.setParameter("name", "smime.p7s");
             pkcs7BodyPart.setFilename("smime.p7s");
             mimepart.addPart(partToSign);
             mimepart.addPart(pkcs7BodyPart);
             mimepart.setSubType("signed");
             mimepart.setParameter("protocol", SMimeFactory.PKCS7_SIGNATURE_CONTENT_TYPE);
             String digetsAlgo = getDigestAlgorithm();
             if(digetsAlgo == null)
              digetsAlgo = SMimeFactory.DEFAULT_DIGEST_ALGO;
             else
              digetsAlgo = getExternalDigestAlgo(getDigestAlgorithm());
             mimepart.setParameter("micalg", digetsAlgo); //
             return mimepart;
           }
        }
        else
          throw new SecurityServiceException("Cannot Perform Sign");
      }
     }
     catch(Exception ex)
     {
       ex.printStackTrace();
       throw new SecurityServiceException("Cannot Perform Sign ",ex);
     }
      return null;
   }

/*    byte[] signature = PKCS7BodyPart.sign(certJ, pathCtx, ownCert, partToSign);
    return signature;
*/


  /**
   * Generate a PKCS7 signature.
   *
   * @param ContentToSign content to be signed
   * @return DER encoded signature
   *
   * @since 1.3
   */
  public byte[] getSignature(File contentToSign)
    throws SecurityServiceException
  {
    byte[] content = GridCertUtilities.loadFileToByte(contentToSign);
    return getSignature(content);
  }

  /**
   * Generate a PKCS7 signature.
   *
   * @param ContentToSign content to be signed
   * @return DER encoded signature
   *
   * @since 1.3
   */
  public byte[] getSignature(byte[] contentToSign)
   throws SecurityServiceException
  {
    return PKCS7BodyPart.sign(getPrivateKey(), ownCert, contentToSign);
  }

  public IMailpart verify(IMime partToVerify) throws SecurityServiceException
  {
    try
    {
      SecurityLogger.debug("SMimeFactory][verify()] Verify Begin");
      SMimeSecurityInfo smimeInfo = new SMimeSecurityInfo();
      smimeInfo.setOwnCertificate(ownCert);
      smimeInfo.setReceipentCertificate(partnerCert);
      smimeInfo.setEncryptionAlgoritm( (_encryptionAlgorithm == null) ?
        DEFAULT_ENCRYPTION_ALGORITHM  : _encryptionAlgorithm );
      smimeInfo.setEncryptionLevel((_encryptionLevel == UNDEFINED_ENCRYPTION_LEVEL) ?
        DEFAULT_ENCRYPTION_LEVEL : _encryptionLevel);
      smimeInfo.setDigestAlgorithm((_digestAlgorithm == null) ?
        DEFAULT_CMS_DIGEST_ALGORITHM : getLocalizedDigestAlgo(_digestAlgorithm));

      GNBodypart    bodyPart = (GNBodypart)partToVerify.getPart(0);

      ByteArrayOutputStream bos = new ByteArrayOutputStream();
      bodyPart.getBodyPart().writeTo(bos);
      byte[] contentInfoEncoding = bos.toByteArray();
     /*
      This piece of code dose not get the excat content from BodyPart.
      byte[] contentInfoEncoding = (byte[])bodyPart.getContent(
                                         IMailpart.OUTPUT_BYTE_ARRAY);

     */
      GNBodypart    signatureBodyPart = (GNBodypart)partToVerify.getPart(1);
      byte[] signature  = (byte[])signatureBodyPart.getContent(
                                           IMailpart.OUTPUT_BYTE_ARRAY);

      smimeInfo.setDataToVerify(contentInfoEncoding);
      smimeInfo.setSignatureToVerify(signature);

      /**
       * If Verified return original BodyPart. Else Throw Exception.
       */

      _securityServiceFacade = lookupSecurityMgr();
      //SMimeSecurityInfo returnInfo = 
      _securityServiceFacade.verify(smimeInfo);

      SecurityLogger.debug("SMimeFactory][verify()] Verify Success");
      return bodyPart;
    }
    catch(Exception ex)
    {
     SecurityLogger.debug("[SMimeFactory][verify()] Cannot Perform Verification");
     ex.printStackTrace();
     throw new SecurityServiceException("[SMimeFactory][verify()]"+
     " Cannot Perform Verification",ex);

    }
  }


  public IMailpart verify(byte[] content, byte[] signature)
    throws SecurityServiceException
  {
    try
    {
      SecurityLogger.debug("SMimeFactory][verify()] Verify Begin");
      SMimeSecurityInfo smimeInfo = new SMimeSecurityInfo();
      smimeInfo.setOwnCertificate(ownCert);
      smimeInfo.setReceipentCertificate(partnerCert);
      smimeInfo.setEncryptionAlgoritm( (_encryptionAlgorithm == null) ?
        DEFAULT_ENCRYPTION_ALGORITHM  : _encryptionAlgorithm );
      smimeInfo.setEncryptionLevel((_encryptionLevel == UNDEFINED_ENCRYPTION_LEVEL) ?
        DEFAULT_ENCRYPTION_LEVEL : _encryptionLevel);
      smimeInfo.setDigestAlgorithm((_digestAlgorithm == null) ?
        DEFAULT_CMS_DIGEST_ALGORITHM : getLocalizedDigestAlgo(_digestAlgorithm));

      smimeInfo.setDataToVerify(content);
      smimeInfo.setSignatureToVerify(signature);

      _securityServiceFacade = lookupSecurityMgr();
      SMimeSecurityInfo returnInfo = _securityServiceFacade.verify(smimeInfo);
      IMailpart originalPart = returnInfo.getVerifiedPart();
      SecurityLogger.debug("SMimeFactory][verify()] Verify Success");
      return originalPart;
    }
    catch(Exception ex)
    {
     SecurityLogger.debug("[SMimeFactory][verify()] Cannot Perform Verification");
     ex.printStackTrace();
     throw new SecurityServiceException("[SMimeFactory][verify()]"+
     " Cannot Perform Verification",ex);

    }
  }


  public IMailpart decrypt(IPart partToDecrypt) throws SecurityServiceException
  {
   try
   {
     byte[] toDecryptPart = (byte[])partToDecrypt.getContent(
                                        IMailpart.OUTPUT_BYTE_ARRAY);
     byte[] decryptPart = {};
     if(toDecryptPart != null)
     {
       decryptPart = decrypt(toDecryptPart);
     }
     else
     {
       throw new SecurityServiceException("Unable To decrypt : Part To Encrypt Null");
     }
      return GNMimeUtility.generatePart(decryptPart);

   }
   catch(Exception ex)
   {
     ex.printStackTrace();
     SecurityLogger.debug("[SMimeFactory][encrypt()] Cannot Perform Encrypt");
     throw new SecurityServiceException("[SMimeFactory][encrypt()]"+
     " Cannot Perform Encrypt",ex);
   }

  }

  public byte[] encrypt(byte[] contentToEncrypt) throws SecurityServiceException
  {
   try
   {
      SecurityLogger.debug("SMimeFactory][encrypt()] Encrypt Begin");
      SMimeSecurityInfo smimeInfo = new SMimeSecurityInfo();
      smimeInfo.setOwnCertificate(ownCert);
      smimeInfo.setReceipentCertificate(partnerCert);
      smimeInfo.setEncryptionAlgoritm( (_encryptionAlgorithm == null) ?
          DEFAULT_ENCRYPTION_ALGORITHM  : _encryptionAlgorithm );
      smimeInfo.setEncryptionLevel((_encryptionLevel == UNDEFINED_ENCRYPTION_LEVEL) ?
          DEFAULT_ENCRYPTION_LEVEL : _encryptionLevel);
      smimeInfo.setDataToEncrypt(contentToEncrypt);
      _securityServiceFacade = lookupSecurityMgr();
      SMimeSecurityInfo returnInfo = _securityServiceFacade.encrypt(smimeInfo);
      byte[] encryptedPart = returnInfo.getEncryptedData();
      if(encryptedPart == null)
      {
        throw new SecurityServiceException("Unable to encrypt mime part");
      }
      SecurityLogger.debug("SMimeFactory][encrypt()] Encrypt End");
      return encryptedPart;
   }
   catch(Exception ex)
   {
     ex.printStackTrace();
     SecurityLogger.debug("[SMimeFactory][encrypt()] Cannot Perform Encrypt");
     throw new SecurityServiceException("[SMimeFactory][encrypt()]"+
     " Cannot Perform Encrypt",ex);
   }

  }


  public IPart compress(IMailpart partToCompress) throws SecurityServiceException
  {
   try
   {
     byte[] toCompressPart = (byte[])partToCompress.getContentByte(false);
     byte[] compressedData = {};
     if(toCompressPart != null)
     {
       compressedData = compress(toCompressPart);
     }
     else
     {
       throw new SecurityServiceException("Unable To Encrypt : Part To Compress Null");
     }
     GNBodypart compressedPart = new GNBodypart();
     compressedPart.setContent(compressedData,null);
     compressedPart.setContentType(SMimeFactory.PKCS7_MIME_TYPE);
     compressedPart.setParameter("smime-type", "compressed-data");
     compressedPart.setParameter("name", "smime.p7z");
     compressedPart.setFilename("smime.p7z");
     return compressedPart;
   }
   catch(Exception ex)
   {
     ex.printStackTrace();
     SecurityLogger.debug("[SMimeFactory][compress()] Cannot Perform Compress");
     throw new SecurityServiceException("[SMimeFactory][compress()]"+
     " Cannot Perform Compress",ex);
   }
 }
  
  /**
   * TWX 4 Jan 2008: Compress the passed in part partToCompress part content (with Content-Id, content, etc)
   * @param partToCompress
   * @return the IPart that content the partToCompress content in compressed form
   * @throws SecurityServiceException
   */
  public IPart compressPartContent(IMailpart partToCompress) throws SecurityServiceException
  {
   try
   {
	 ByteArrayOutputStream out = new ByteArrayOutputStream();
	 partToCompress.writeContentToStream(out);
	 
     byte[] toCompressPart = out.toByteArray();
     byte[] compressedData = {};
     if(toCompressPart != null)
     {
       compressedData = compress(toCompressPart);
     }
     else
     {
       throw new SecurityServiceException("Unable To Encrypt : Part To Compress Null");
     }
     GNBodypart compressedPart = new GNBodypart();
     compressedPart.setContent(compressedData,null);
     compressedPart.setContentType(SMimeFactory.PKCS7_MIME_TYPE);
     compressedPart.setParameter("smime-type", "compressed-data");
     compressedPart.setParameter("name", "smime.p7z");
     compressedPart.setFilename("smime.p7z");
     
     SecurityLogger.debug("[SMimeFactory][compress2()] complete");
     
     return compressedPart;
   }
   catch(Exception ex)
   {
     ex.printStackTrace();
     SecurityLogger.debug("[SMimeFactory][compress()] Cannot Perform Compress");
     throw new SecurityServiceException("[SMimeFactory][compress()]"+
     " Cannot Perform Compress",ex);
   }
 } 
  
  public byte[] compress(byte[] contentToCompress)
    throws SecurityServiceException
 {
   try
   {
      SecurityLogger.debug("SMimeFactory][compress()] Compress Begin");
      SMimeSecurityInfo smimeInfo = new SMimeSecurityInfo();
      smimeInfo.setDataToCompress(contentToCompress);
      smimeInfo.setCompressMethod(getCompressMethod());
      smimeInfo.setCompressLevel(getCompressLevel());
      _securityServiceFacade = lookupSecurityMgr();
      SMimeSecurityInfo returnInfo = _securityServiceFacade.compress(smimeInfo);
//      SMimeSecurityInfo returnInfo = _securityServiceFacade1.compress(smimeInfo);
      byte[] compressedPart = returnInfo.getCompressedData();
      if(compressedPart == null)
      {
        throw new SecurityServiceException("Unable to compress mime part");
      }
      SecurityLogger.debug("SMimeFactory][compress()] Compress End");
      return compressedPart;
   }
   catch(Exception ex)
   {
     ex.printStackTrace();
     SecurityLogger.debug("[SMimeFactory][encrypt()] Cannot Perform Compress");
     throw new SecurityServiceException("[SMimeFactory][compress()]"+
     " Cannot Perform Compress",ex);
   }
  }


  public IMailpart deCompress(IPart partToDeCompress) throws SecurityServiceException
  {
   try
   {
     SecurityLogger.debug("SMimeFactory][deCompress()] DeCompress Begin");
     
     byte[] toDeCompressPart = (byte[])partToDeCompress.getContent(IMailpart.OUTPUT_BYTE_ARRAY);
     byte[] deCompressPart = {};
     if(toDeCompressPart != null)
     {
       deCompressPart = deCompress(toDeCompressPart);
     }
     else
     {
       throw new SecurityServiceException("Unable To deCompress : Part To DeCompress Null");
     }
      return GNMimeUtility.generatePart(deCompressPart);
   }
   catch(Exception ex)
   {
     ex.printStackTrace();
     SecurityLogger.debug("[SMimeFactory][encrypt()] Cannot Perform DeCompress");
     throw new SecurityServiceException("[SMimeFactory][deCompress()]"+
     " Cannot Perform DeCompress",ex);
   }
  }
  
  //TWX 4 Jan 2008: decompress the content of the IPart
  public IPart deCompressPartContent(IPart partToDeCompress) throws SecurityServiceException
  {
   try
   {
     SecurityLogger.debug("SMimeFactory][deCompressPartContent()] DeCompress Begin");
     
     byte[] toDeCompressPart = (byte[])partToDeCompress.getContent(IMailpart.OUTPUT_BYTE_ARRAY);
     byte[] deCompressPart = {};
     if(toDeCompressPart != null)
     {
       deCompressPart = deCompress(toDeCompressPart);
     }
     else
     {
       throw new SecurityServiceException("Unable To deCompress : Part To DeCompress Null");
     }
     
     SecurityLogger.debug("SMIMEFactory: bodyPart: "+ new String(deCompressPart));
     
     MimeBodyPart bodyPart = new MimeBodyPart(new ByteArrayInputStream(deCompressPart));
     return new GNBodypart(bodyPart);
   }
   catch(Exception ex)
   {
     ex.printStackTrace();
     SecurityLogger.debug("[SMimeFactory][encrypt()] Cannot Perform DeCompress");
     throw new SecurityServiceException("[SMimeFactory][deCompress()]"+
     " Cannot Perform DeCompress",ex);
   }
  }
  
  public byte[] deCompress(byte[] contentToDeCompress)
    throws SecurityServiceException
 {
   try
   {
      SecurityLogger.debug("SMimeFactory][deCompress()] DeCompress Begin");
      SMimeSecurityInfo smimeInfo = new SMimeSecurityInfo();
      smimeInfo.setDataToDeCompress(contentToDeCompress);
      _securityServiceFacade = lookupSecurityMgr();
      SMimeSecurityInfo returnInfo = _securityServiceFacade.deCompress(smimeInfo);
//      SMimeSecurityInfo returnInfo = _securityServiceFacade1.deCompress(smimeInfo);
      byte[] deCompressedPart = returnInfo.getDeCompressedData();
      if(deCompressedPart == null)
      {
        throw new SecurityServiceException("Unable to deCompress mime part");
      }
      SecurityLogger.debug("SMimeFactory][DeCompress()] DeCompress End");
      return deCompressedPart;
   }
   catch(Exception ex)
   {
     ex.printStackTrace();
     SecurityLogger.debug("[SMimeFactory][deCompress()] Cannot Perform DeCompress");
     throw new SecurityServiceException("[SMimeFactory][deCompress()]"+
     " Cannot Perform DeCompress",ex);
   }
  }
  
  public IPart encrypt(IMailpart partToEncrypt) throws SecurityServiceException
  {
   try
   {
     GNBodypart encryptedPart = new GNBodypart();
     byte[] toEncryptPart = (byte[])partToEncrypt.getContentByte(false);
     //Comment... As partToEncrypt.getContent(GNBodypart.OUTPUT_BYTE_ARRAY);

     byte[] encyPart = {};
     if(toEncryptPart != null)
     {
       encyPart = encrypt(toEncryptPart);
     }
     else
     {
       throw new SecurityServiceException("Unable To Encrypt : Part To Encrypt Null");
     }
     encryptedPart.setContent(encyPart,null);
     encryptedPart.setContentType(SMimeFactory.PKCS7_MIME_TYPE);
     encryptedPart.setParameter("smime-type", "enveloped-data");
     encryptedPart.setParameter("name", "smime.p7m");
     encryptedPart.setFilename("smime.p7m");
     return encryptedPart;
   }
   catch(Exception ex)
   {
     ex.printStackTrace();
     SecurityLogger.debug("[SMimeFactory][encrypt()] Cannot Perform Encrypt");
     throw new SecurityServiceException("[SMimeFactory][encrypt()]"+
     " Cannot Perform Encrypt",ex);
   }
  }


  public byte[] decrypt(byte[] contentToDecrypt)
    throws SecurityServiceException
  {
   try
   {
      SMimeSecurityInfo smimeInfo = new SMimeSecurityInfo();
      smimeInfo.setOwnCertificate(ownCert);
      smimeInfo.setReceipentCertificate(partnerCert);
      smimeInfo.setEncryptionAlgoritm( (_encryptionAlgorithm == null) ?
          DEFAULT_ENCRYPTION_ALGORITHM  : _encryptionAlgorithm );
      smimeInfo.setEncryptionLevel((_encryptionLevel == UNDEFINED_ENCRYPTION_LEVEL) ?
          DEFAULT_ENCRYPTION_LEVEL : _encryptionLevel);
      smimeInfo.setDataToDecrypt(contentToDecrypt);
      _securityServiceFacade = lookupSecurityMgr();
      smimeInfo.setPrivateKey(ownPrivateKey);

      SMimeSecurityInfo returnInfo = _securityServiceFacade.decrypt(smimeInfo);
      byte[] decryptedPart = returnInfo.getDecryptedData();
      if(decryptedPart == null)
        throw new SecurityServiceException("Unable to encrypt mime part");
      return decryptedPart;
    }
    catch(Exception ex)
    {
      SecurityLogger.debug("[SMimeFactory][decrypt()] Cannot Perform decrypt");
      ex.printStackTrace();
      throw new SecurityServiceException("[SMimeFactory][decrypt()]"+
      " Cannot Perform decrypt",ex);
    }
  }


/*  public IMailpart decrypt(IPart partToDecrypt) throws SecurityServiceException
  {
    return new GNMimepart();
  }
*/
  /*
  public void clearSensitiveData()
  {
    // clear sensitive data
    if (ownPrivateKey != null)
      ownPrivateKey.clearSensitiveData();
    if (ownPublicKey != null)
      ownPublicKey.clearSensitiveData();
    if (partnerPublicKey != null)
      partnerPublicKey.clearSensitiveData();
  }*/

  public void setEncoding(String encoding)
  {
    this.encoding = encoding;
  }
  /*
  private JSAFE_PrivateKey createPrivateKey(byte[] privateKeyData, String password)
  {
    return GridCertUtilities.loadPKCS8PrivateKeyData(password, privateKeyData);
  }*/

  public static X509Certificate createCert(byte[] certData)
      throws SecurityServiceException
  {
    X509Certificate c = null;
    try
    {
      c = GridCertUtilities.loadX509Certificate(certData);
    }
    catch (Exception ex)
    {
      //err("createCert", "Unable to generate Certificate", ex);
      throw new SecurityServiceException("Unable to generate Certificate", ex);
    }

    return c;
  }

  Certificate[] addMoreTrustedCerts (Certificate[] certs,Certificate newCert)
  {
    Certificate[] newCerts = new Certificate[certs.length + 1];
    System.arraycopy(certs, 0 , newCerts, 0, certs.length);
    newCerts[certs.length] = newCert;

    return newCerts;
  }
  /*
  private void debug(String methodName, String msg)
  {
    //SLogger.debug("[SMimeFactory." + methodName + "] " + msg);
  }

  private void err(String methodName, String msg, Exception ex)
  {
   //SLogger.err("[SMimeFactory." + methodName + "] " + msg, ex);
  }*/

  /*
  public X509Certificate getSenderCert() throws Exception
  {
    return (X509Certificate)ownCert.clone();
  }

  public X509Certificate getRecipientCert() throws Exception
  {
    return (X509Certificate)partnerCert.clone();
  }*/

/*  public ICryptor createCryptor()
  {
    return new PKCS7Cryptor(certJ, pathCtx, ownCert, partnerCert);
  }
*/

  private ISecurityServiceObj lookupSecurityMgr()
      throws ServiceLookupException,CreateException,RemoteException
  {
    //ISecurityServiceHome securityManagerHome = (ISecurityServiceHome)ServiceLookup.getInstance(
    //                                            ServiceLookup.LOCAL_CONTEXT).getHome(
    //                                            ISecurityServiceHome.class);
    //return securityManagerHome.create();
  	return (ISecurityServiceObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(ISecurityServiceHome.class.getName(),
  	                                                                                          ISecurityServiceHome.class,
  	                                                                                          new Object[0]);
  }

  public static ICertificateManagerObj lookupCertificateMgr()
      throws ServiceLookupException,CreateException,RemoteException
  {
    //ICertificateManagerHome certificateManagerHome = (ICertificateManagerHome)ServiceLookup.getInstance(
    //                                            ServiceLookup.CLIENT_CONTEXT).getHome(
    //                                            ICertificateManagerHome.class);
    //return certificateManagerHome.create();
  	return (ICertificateManagerObj)ServiceLocator.instance(ServiceLocator.CLIENT_CONTEXT).getObj(ICertificateManagerHome.class.getName(),
  	                                                                                             ICertificateManagerHome.class,
  	                                                                                             new Object[0]);
  }

  public void setOwnCert(X509Certificate cert)
  {
    ownCert = cert;
  }

  public void setPartnerCert(X509Certificate cert)
  {
    partnerCert = cert;
  }

  public PrivateKey getPrivateKey()
  {
    return ownPrivateKey;
  }
  
  public void setPrivateKey(PrivateKey privateKey)
  {
    ownPrivateKey = privateKey;
  }
  
  /*
  public DatabaseService getDBService() {
	  return dbService;
  }*/
  
  public String getEncoding() {
	  return encoding;
  }
//  public static void main(String args[]) throws Exception
//  {
//
//     ICertificateManagerObj certObj = lookupCertificateMgr();
//     //com.gridnode.pdip.base.certificate.helpers.GridCertUtilities.loadX509Certificate(new File("c:/newsign.txt"));
////    certObj.importCertificate("abcdef","mtes.p12","abcde");
//
//     //certObj.importCertificate("","","");
//
////     certObj.importCertificate("","")
////    certObj.importCertificate("QingsongTest2","qown.p12","mysecret");
//  //  certObj.importCertificate("QingsongPartner","qpartner.der");
////    certObj.importCertificate("QingsongPartner","encryptkey-rsa1024-123456789.p12","mysecret");
//
//    /** Testing Encryption of SMIME */
////     com.gridnode.pdip.base.certificate.model.Certificate certifi = certObj.findCertificateByUID(new Long(49));
////     System.out.println(certifi.getCertificate());
////     X509Certificate ownCert =  com.gridnode.pdip.base.certificate.helpers.GridCertUtilities.loadX509CertificateByString(certifi.getCertificate());
//
//
////    X509Certificate ownCert = GridCertUtilities.loadX509Certificate(
////    "d:/security/data/mtes.cer");
////    X509Certificate partnerCert = GridCertUtilities.loadX509Certificate(
////    "D:/jb/jboss/jboss-3.0.0alpha/bin/gtas/data/sys/import/cert/987654321.der");
//    //SecurityDB.setPrivatepassword("xx");
//
////    SMimeFactory factory =
////    SMimeFactory.newInstance(ownCert,partnerCert,
////    "RC2/CBC/PKCS5Padding","SHA1",40);
////    "RC5-0x10-32-16/CBC/PKCS5Padding","SHA1",128);
//
//
//  //  byte[] encrypteByte = GridCertUtilities.getByteArrayFromFile(new File("d:/decryptnew.dat"));
//
//
//
//
///*    if(factory != null)
//    {
//     encrypteByte = factory.encrypt(encrypteByte);
//    }
// */
//
////    encrypteByte = GridCertUtilities.decode(new String(encrypteByte));
////    GNBodypart partm = new GNBodypart(encrypteByte,"");
//
////    System.out.println("Encrypted Bytes = "+new String(encrypteByte));
//
////    GNBodypart part1 = new GNBodypart(encrypteByte,"");
//    //factory.decrypt(part1);
//
//    //IMime decryptData  = (IMime)factory.decrypt(partm);
//    //IPart part1 =  decryptData.getPart(0);
//    //System.out.println(part1.getContentString());
//
//
//
//   // System.out.println("Decrypted Bytes = "+new String(decryptData));
//
//
//     /** Testing Decruption **/
///*    ownCert = GridCertUtilities.loadX509Certificate(
//    "d:/security/data/pp.cer");
//    partnerCert = GridCertUtilities.loadX509Certificate(
//    "d:/security/data/mtes.cer");
//
//    factory =
//    SMimeFactory.newInstance(ownCert,partnerCert,"RC5-0x10-32-16/CBC/PKCS5Padding","SHA1",128);
//    byte[] decryptData  = factory.decrypt(encrypteByte);
//    System.out.println("Decrypted Bytes = "+new String(decryptData));
//
//    /** Testing Signing of SMIME **/
///*    ownCert = GridCertUtilities.loadX509Certificate(
//    "d:/security/data/mtes.cer");
//    partnerCert = GridCertUtilities.loadX509Certificate(
//    "d:/security/data/pp.cer");
//    byte[] encrypteByte1 = GridCertUtilities.getByteArrayFromFile(new File("c:/a.txt"));
//    factory =
//    SMimeFactory.newInstance(ownCert,partnerCert);
//    //,"RC5-0x10-32-16/CBC/PKCS5Padding","SAH1",128);
//    byte[] signedData = factory.sign(encrypteByte1);
//    _signedData = signedData;
//    System.out.println("Signed Data "+new String(signedData));
////    GNBodypart signedPart = new GNBodypart();
//
//
//    factory =
//    SMimeFactory.newInstance(ownCert,partnerCert,"RC5-0x10-32-16/CBC/PKCS5Padding","SHA1",128);
//
//
//    GNMimepart mimePart = new GNMimepart();
//    GNBodypart bodyPart = new GNBodypart(encrypteByte1,"text/plain");
//    mimePart.addPart(bodyPart);
//
//    IMime signedPart = factory.sign(bodyPart);
//    FileOutputStream fos = new FileOutputStream(new File("c:/abc.txt"));
//    signedPart.writeToStream(fos);
//    fos.close();
//    //System.out.println("Data is "+new String(signedPart.getContentByte(false)));
//
//    ownCert = GridCertUtilities.loadX509Certificate(
//    "d:/security/data/pp.cer");
//    partnerCert = GridCertUtilities.loadX509Certificate(
//    "d:/security/data/mtes.cer");
//
//    factory =
//    SMimeFactory.newInstance(ownCert,partnerCert,"RC5-0x10-32-16/CBC/PKCS5Padding","SHA1",128);
//    //System.out.println("Data is for Verify PArt--> "+new String(verifyPart.getContentByte(false)));
//
///*    IMime verifyPart1 = factory.generateMime(new File("c:/abc.txt"));
//    System.out.println("Data is for Verify PArt--> "+new String(verifyPart1.getContentByte(false)));
//    GNMimepart verifyPart = new GNMimepart();
//    IPart bp1 = factory.createPart(encrypteByte1);
//    IPart bp2 = verifyPart1.getPart(1);
//    verifyPart.addPart(bp1);
//    verifyPart.addPart(bp2);
//    factory.verify(verifyPart);
//*/
//
////    IMime vmime =   factory.generateMime(new File("d:/UnPackage61364tmp.dat"));
////    IMime envelope= vmime.getPart(0).getMultipart();
//
////    IPart partdec= envelope.getPart(3);
//   // factory.decrypt(partdec);
////    byte[] data = GridCertUtilities.getByteArrayFromFile(new File("d:/msg.txt"));
////    byte[] sign = GridCertUtilities.getByteArrayFromFile(new File("d:/sigature.txt"));
////    sign = GridCertUtilities.decode(new String(sign));
////    factory.verify(data,sign);
//   // factory.verify(vmime);
////    factory.verify(encrypteByte1,signedData);
////    factory.verify(signedPart);
//
//
////    GNMultipart multiPart = new GNMultipart();
////    IPart bodyPart0 = factory.createPart(encrypteByte);
////
////    IPart bodyPart1 = factory.createPart(signedData);
////
////    SMimeSecurityInfo vinfo = new SMimeSecurityInfo();
////
////    vinfo.setOwnCertificate(ownCert);
////    vinfo.setReceipentCertificate(partnerCert);
////    IMime payLoad = factory.createMime();
////    payLoad.addPart(bodyPart0);
////    payLoad.addPart(bodyPart1);
////    vinfo.setPartToVerify(payLoad);
////    factory.verify(payLoad);
//
//
////     certifi = certObj.findCertificateByUID(new Long(49));
////     ownCert = com.gridnode.pdip.base.certificate.helpers.GridCertUtilities.loadX509CertificateByString(certifi.getCertificate());
//
////     ownCert = GridCertUtilities.loadX509Certificate("d:/123456789.cer");
//
//     com.gridnode.pdip.base.certificate.model.Certificate certifi = certObj.findCertificateByUID(new Long(63));
//     X509Certificate ownCert =  com.gridnode.pdip.base.certificate.helpers.GridCertUtilities.loadX509CertificateByString(certifi.getCertificate());
//     certifi = certObj.findCertificateByUID(new Long(62));
//     X509Certificate partnerCert = com.gridnode.pdip.base.certificate.helpers.GridCertUtilities.loadX509CertificateByString(certifi.getCertificate());
//
//     SMimeFactory factory1 = SMimeFactory.newInstance(ownCert,partnerCert, "RC2/CBC/PKCS5Padding","SHA1",40);
//
////     File f = new File("d:/sign.txt");
//     byte[] actualByte = GridCertUtilities.getByteArrayFromFile(new File("c:/qdata.txt"));
//     byte[] signature = GridCertUtilities.getByteArrayFromFile(new File("c:/qsign.txt"));
//
//     signature =  GridCertUtilities.decode(new String(signature));
//     //System.out.println("Signature = \n"+new String(signature));
//     //System.out.println("ActualtData = \n"+new String(actualByte));
//
//     IMime mime =
//     factory1.generateMime(new File("c:/qreceive.txt"));
//     //factory1.verify(mime);
//
//    /*     Tests conducted on 30-Jan-2003. */
//
//     SMimeFactory factory2 = SMimeFactory.newInstance(partnerCert,ownCert, "RC2/CBC/PKCS5Padding","SHA1",40);
//     IPart encPart =  factory2.encrypt(factory2.generateMime(new File("c:/qdata.txt")));
//
//     SMimeFactory factory3 = SMimeFactory.newInstance(ownCert,partnerCert,"RC2/CBC/PKCS5Padding","SHA1",40);
//     GNMimepart partDec = (GNMimepart) factory3.decrypt(encPart);
//
//
//     SMimeFactory factory4 = SMimeFactory.newInstance(ownCert,partnerCert,"RC2/CBC/PKCS5Padding","SHA1",40);
//     IMime mimeP1 = factory4.sign(factory4.generateMime(new File("c:/msg1.txt")));
//     mimeP1.writeToStream(new FileOutputStream(new File("c:/mysign.txt")));
//
//     GNBodypart    bodyParts = (GNBodypart)mimeP1.getPart(0);
//
//     ByteArrayOutputStream bos = new ByteArrayOutputStream();
//      bodyParts.getBodyPart().writeTo(bos);
//      byte[] contentInfoEncoding = bos.toByteArray();
//      FileOutputStream fos = new FileOutputStream("c:/xd.txt");
//      fos.write(contentInfoEncoding);
//      fos.flush();
//      fos.close();
//
//      GNBodypart bodyPart1 = (GNBodypart)mimeP1.getPart(1);
//      byte[] signature2  = (byte[])bodyPart1.getContent(
//                                           IMailpart.OUTPUT_BYTE_ARRAY);
//
//      FileOutputStream fos1 = new FileOutputStream("c:/signt.txt");
//      fos1.write(GridCertUtilities.encode(signature2).getBytes());
//      fos1.flush();
//      fos1.close();
//
//      //System.out.println("\nWhat is this \n"+new String(mimeP1.getContentByte(false)));
//
//     SMimeFactory factory5 = SMimeFactory.newInstance(partnerCert,ownCert,"RC2/CBC/PKCS5Padding","SHA1",40);
//     factory5.verify(mimeP1);
//     //factory5.verify(contentInfoEncoding,signature2);
//
//
////     IMime mime =
////     factory1.generateMime(new File("d:/gridtalk-sign.txt"));
////     GNMimeUtility.generatePart(GridCertUtilities.loadFileToByte(new File("d:/gridtalk-sign.txt")));
//
//    // System.out.println("Encrypted Byte "+new String(actualByte));
//
//
//    // actualByte,signedByte);
//
//
//  }

  public int getCompressLevel()
  {
    return _compressLevel;
  }

  public void setCompressLevel(int compressLevel)
  {
    this._compressLevel = compressLevel;
  }

  public int getCompressMethod()
  {
    return _compressMethod;
  }
  public void setCompressMethod(int compressMethod)
  {
    this._compressMethod = compressMethod;
  }
  
  /**
   * The digest algorithm representation we used in the CMS is not understandable by external program, 
   * we have to manually convert them to the standard format.
   * @param digestAlgo The CMS base digest algo (based on Bouncy Castle)
   * @return external represnetation of the digest algo
   */
  public static String getExternalDigestAlgo(String digestAlgo)
  {
    if(CMSSignedDataGenerator.DIGEST_MD5.equalsIgnoreCase(digestAlgo))
    {
      return ISecurityInfo.DIGEST_ALGORITHM_MD5;
    }
    else if(CMSSignedDataGenerator.DIGEST_SHA1.equalsIgnoreCase(digestAlgo))
    {
      return ISecurityInfo.DIGEST_ALGORITHM_SHA1;
    }
    return digestAlgo;
  }
  
  /**
   * The passed in digest algo will be converted into the format that is understand by the
   * CMS API we used.
   * @param digestAlgo external representation of the digest algo
   * @return internal representation of the digest algo.
   */
  public static String getLocalizedDigestAlgo(String digestAlgo)
  {
    if(ISecurityInfo.DIGEST_ALGORITHM_MD5.equalsIgnoreCase(digestAlgo))
    {
      return CMSSignedDataGenerator.DIGEST_MD5;
    }
    else if(ISecurityInfo.DIGEST_ALGORITHM_SHA1.equalsIgnoreCase(digestAlgo))
    {
      return CMSSignedDataGenerator.DIGEST_SHA1;
    }
    return digestAlgo;
  }
}