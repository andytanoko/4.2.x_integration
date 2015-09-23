package com.gridnode.pdip.base.security.facade.ejb;

import java.io.File;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Date;

import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;
import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;
import com.gridnode.pdip.base.security.helpers.PKCS12Reader;
import com.gridnode.pdip.base.security.helpers.SMIMESecurityServiceFacade;
import com.gridnode.pdip.base.security.helpers.SMimeSecurityInfo;
import com.gridnode.pdip.base.security.helpers.SecurityLogger;
import com.gridnode.pdip.base.security.mime.GNBodypart;
import com.gridnode.pdip.base.security.mime.IMailpart;
import com.gridnode.pdip.base.security.mime.IMime;
import com.gridnode.pdip.base.security.mime.IPart;
//import com.rsa.certj.CertJ;
//import com.rsa.certj.DatabaseService;
//import com.rsa.certj.Provider;
//import com.rsa.certj.cert.Certificate;
//import com.rsa.certj.pkcs7.ContentInfo;
//import com.rsa.certj.pkcs7.Data;
//import com.rsa.certj.pkcs7.EnvelopedData;
//import com.rsa.certj.pkcs7.PKCS7Exception;
//import com.rsa.certj.pkcs7.RecipientInfo;
//import com.rsa.certj.pkcs7.SignedData;
//import com.rsa.certj.pkcs7.SignerInfo;
//import com.rsa.certj.provider.db.MemoryDB;
//import com.rsa.certj.provider.path.PKIXCertPath;
//import com.rsa.certj.spi.path.CertPathCtx;
//import com.rsa.jsafe.JSAFE_PrivateKey;

public class TestSMIMESecurityServiceFacade
{

//  private static CertJ certJ;
//  private static Provider memProvider;
//  private static CertPathCtx pathCtx;
  
  /**
   * @param args
   */
  public static void main(String[] args) throws Exception
  {
    //encrypt via RSA, decrypt via BC
    //rsaEncryptBCDecrypt(); //RSA impl logic seem like having issue. ignore
    
   //encrypt via BC, decrypt via BC
    rsaEncryptBCDecrypt();
    
   //sign via RSA, verify via BC
   //rsaSignBCVerify(); 
    
   //sign via BC, verify via RSA
   //bcSignRSAVerify();
    
  }
  
  private static void rsaEncryptBCDecrypt() throws Exception
  {
    File f = new File("c:/3C3_33_1.xml");
    byte[] partToEncrypt = GridCertUtilities.loadFileToByte(f);
    SecurityServiceBean bean = new SecurityServiceBean();
    
    //BC encrypt
    File ownCertPrivateInov = new File("data/pkcs12/inovQATest.p12");
    File tpCertFile = new File("data/pkcs12/testP12.cer");
    X509Certificate tpCert = GridCertUtilities.loadX509Certificate(tpCertFile);
    PKCS12Reader reader = new PKCS12Reader(ownCertPrivateInov.getAbsolutePath(), "changeit".toCharArray());
    reader.read();
    
    
    byte[] partToDecryped = bean.encrypt(tpCert, partToEncrypt, reader.getCertificate(), reader.getPrivateKey());
    
    //BC decrypt
    File ownCertPrivate = new File("data/pkcs12/testP12.p12");
    PKCS12Reader reader2 = new PKCS12Reader(ownCertPrivate.getAbsolutePath(), "changeit".toCharArray());
    reader2.read();
    
    byte[] decrypted = bean.decrypt(partToDecryped, reader2.getCertificate(), reader2.getPrivateKey());
    GridCertUtilities.writeByteTOFile(new File("c:/decryptedContent.txt"), decrypted, decrypted.length);
    
  }
  
//  private static void rsaSignBCVerify() throws Exception
//  {
//    File f = new File("c:/3C3_33_1.xml");
//    File ownCertPrivate = new File("data/pkcs12/testP12.p12");
//    
//    
//    //RSA Sign
//    com.gridnode.pdip.base.certificate.helpers.PKCS12Reader reader = new com.gridnode.pdip.base.certificate.helpers.PKCS12Reader(ownCertPrivate.getAbsolutePath(), "changeit".toCharArray());
//    reader.read();
//    
//    JSAFE_PrivateKey privateKey = reader.getPrivateKey();
//    byte[] partToSign = GridCertUtilities.loadFileToByte(f);
//    com.rsa.certj.cert.X509Certificate certInJava = reader.getCertificate();
//    
//    byte[] rsaSignature = sign(certInJava, partToSign, privateKey);
//    
//    //BC to verify
//    File tpCertFile = new File("data/pkcs12/testP12.cer");
//    X509Certificate tpCert = com.gridnode.pdip.base.security.helpers.GridCertUtilities.loadX509CertificateJava(tpCertFile.getAbsolutePath());
//    File ownCertPrivateInov = new File("data/pkcs12/inovQATest.p12");
//    PKCS12Reader reader2 = new PKCS12Reader(ownCertPrivateInov.getAbsolutePath(), "changeit".toCharArray());
//    reader2.read();
//    
//    SecurityServiceBean service = new SecurityServiceBean();
//    service.verify(partToSign, rsaSignature, tpCert, reader2.getPrivateKey());
//    System.out.println("Verify successful");
//  }
  
//  private static void bcSignRSAVerify() throws Exception
//  {
//    File f = new File("c:/3C3_33_1.xml");
//    byte[] partToSign = GridCertUtilities.loadFileToByte(f);
//    
//    //BC signing
//    //File ownCertFile = new File("data/pkcs12/testP12.cer");
//    File ownCertPrivate = new File("data/pkcs12/testP12.p12");
//    PKCS12Reader reader = new PKCS12Reader(ownCertPrivate.getAbsolutePath(), "changeit".toCharArray());
//    reader.read();
//    
//    SecurityServiceBean service = new SecurityServiceBean();
//    byte[] signedContent = service.sign(reader.getCertificate(), partToSign, reader.getPrivateKey());
//    
//    //RSA verify
//    File tpCertFile = new File("data/pkcs12/testP12.cer");
//    com.rsa.certj.cert.X509Certificate tpCert = com.gridnode.pdip.base.security.helpers.GridCertUtilities.loadX509Certificate(tpCertFile.getAbsolutePath());
//    
//    File ownCertPrivateInov = new File("data/pkcs12/inovQATest.p12");
//    com.gridnode.pdip.base.certificate.helpers.PKCS12Reader readerRSA = new com.gridnode.pdip.base.certificate.helpers.PKCS12Reader(ownCertPrivateInov.getAbsolutePath(), "changeit".toCharArray());
//    readerRSA.read();
//    
//    verify(partToSign, signedContent, tpCert, readerRSA.getPrivateKey());
//    System.out.println("Verify success");
//  }

//  private static SMimeSecurityInfo encrypt(SMimeSecurityInfo info) throws SecurityServiceException
//  {
//    try
//    {
//        SecurityServiceBean remote = new SecurityServiceBean();
//        SecurityLogger.log("Encrypt Start of SMIME ");
//        //CertJ certJ = info.getCertJ();
//        com.rsa.certj.cert.X509Certificate recpCertificate = info.getRecipientCert();
//        JSAFE_PrivateKey privateKey = info.getPrivateKeyJsafe();
//        //initCertDBService(recpCertificate,privateKey);
//        byte[] partToEncrypt = info.getDataToEncrypt();
//
//        if(partToEncrypt == null)
//        {
//          SecurityLogger.log("Part to Encrypt From IMailpart ");
//          partToEncrypt = info.getIMailpartToEncrypt().getContentByte(false);
//        }
//        SecurityLogger.log("Before Calling Encrypt Byte Array "+partToEncrypt);
//        init();
//        byte[] encryptData = encrypt(recpCertificate,partToEncrypt,recpCertificate,privateKey);
//        SecurityLogger.log("After Encryption   "+encryptData);
//        SMimeSecurityInfo iinfo = new SMimeSecurityInfo();
//        iinfo.setEncryptedData(encryptData);
//        return iinfo;
//    }
//    catch(Exception ex)
//    {
//       SecurityLogger.log("Exception in Encrypt Main  " + ex.getMessage());
//       ex.printStackTrace();
//    }
//    return null;
//
//  }
  
  //Directly test on BC, as no impl for CertJ impl
  /*
  private static SMimeSecurityInfo decrypt(SMimeSecurityInfo info) throws SecurityServiceException
  {
    try
    {
          X509Certificate recpCertificate = info.getReceipentCertificate();
          PrivateKey privateKey = info.getPrivateKey();

          byte[] partToDecrypt = info.getDataToDecrypt();
          if(partToDecrypt == null)
          {
             IPart part = info.getIPartToDecrypt();
             partToDecrypt = (byte[])part.getContent(IMailpart.OUTPUT_BYTE_ARRAY);
         }

         init();
         
         SecurityServiceBean _remote = new SecurityServiceBean();
         byte[] decryptData =  _remote.decrypt(partToDecrypt,recpCertificate,privateKey);
         SecurityLogger.log("Decrypted Data  "+decryptData);
         SMimeSecurityInfo iinfo = new SMimeSecurityInfo();
         iinfo.setDecryptedData(decryptData);
         return iinfo;
    }
    catch(Exception ex)
    {
       ex.printStackTrace();
    }
    return null;

  }*/
  
//  private static SMimeSecurityInfo sign(SMimeSecurityInfo info) throws SecurityServiceException
//  {
//    SMimeSecurityInfo signInfo = null;
//    try
//    {
//        com.rsa.certj.cert.X509Certificate recpCertificate = info.getRecipientCert();
//        JSAFE_PrivateKey privateKey = info.getPrivateKeyJsafe();
//        byte[] partToSign = info.getDataToSign();
//        signInfo = new SMimeSecurityInfo();
//        if(partToSign == null)
//        {
//          partToSign = info.getPartToSign().getContentByte(false);
//          signInfo.setIMailpartToSign(info.getPartToSign());
//        }
//        SecurityLogger.log("Begin of Sign ");
//        init();
//        byte[] signedData = sign(recpCertificate,partToSign,privateKey);
//        signInfo.setSignedData(signedData);
//        return signInfo;
//    }
//    catch(Exception ex)
//    {
//        SecurityLogger.err("Cannot Sign Data ",ex);
//        throw new SecurityServiceException("Cannot Sign Data ",ex);
//    }
//
//  }

/*
  private static SMimeSecurityInfo verify(SMimeSecurityInfo info) throws SecurityServiceException
   {
     SMimeSecurityInfo vinfo=null;
     try
     {
        com.rsa.certj.cert.X509Certificate recpCertificate = info.getRecipientCert();
        JSAFE_PrivateKey privateKey = info.getPrivateKeyJsafe();
        IMime partToVerify = info.getPartToVerify();
        GNBodypart    bodyPart = (GNBodypart)partToVerify.getPart(0);
        IMime mimepart = bodyPart.getMultipart();
        byte[] contentInfo = (byte[])mimepart.getContentByte(false);
        GNBodypart    signatureBodyPart = (GNBodypart)partToVerify.getPart(1);
        byte[] signature  = (byte[])signatureBodyPart.getContent(IMailpart.OUTPUT_BYTE_ARRAY);
        init();
        if(verify(contentInfo, signature,recpCertificate,privateKey))
        {
           SecurityLogger.log(" Verified and Got It ... ");
           vinfo = new SMimeSecurityInfo();
           //vinfo.setVerifiedPart(mimepart);
           return vinfo;
        }
        else
        {
            throw new SecurityServiceException("Verify failed");
        }
    }
     catch(Exception ex)
     {
        SecurityLogger.err("Verification Failed  ",ex);
        throw new SecurityServiceException("Verification Failed  ");
     }

   }
   
  private static void initCertDBService2(com.rsa.certj.cert.X509Certificate certificate,JSAFE_PrivateKey prKey)
   {
     try
     {
         MemoryDB memProvider = new MemoryDB("Test Memory DB");
         Provider pkixCertPathProvider = new PKIXCertPath ("PKIX Cert Path Verifier");
         Provider[] providers ={ memProvider, pkixCertPathProvider };
         certJ = new CertJ (providers);
         //JSAFE_PublicKey  publickey = certificate.getSubjectPublicKey("Java");
         DatabaseService dbService =
               (DatabaseService)certJ.bindServices (CertJ.SPT_DATABASE);
//             PKCS12Reader reader = new PKCS12Reader("d:/security/classes/data/pp12.p12","xx".toCharArray());
//             reader.read();
//             JSAFE_PrivateKey prKey = reader.getPrivateKey();

          dbService.insertCertificate(certificate);
          dbService.insertPrivateKeyByCertificate(certificate,prKey);
          Certificate [] certs = new Certificate[1];
          certs[0] = certificate;
          //pathCtx = new CertPathCtx (CertPathCtx.PF_IGNORE_REVOCATION,
          //                            certs,
          //                            null, new Date(), dbService);


     }
     catch(Exception ex)
     {
         SecurityLogger.err("Cannot Instantiate CertJ Object  ",ex);
     }

   }
  
  
  private static void init() throws java.lang.Exception
   {

     SecurityLogger.log("TEST SecurityServiecs Setup");

     //_home = (ISecurityServiceHome)ServiceLookup.getInstance(
     //        ServiceLookup.CLIENT_CONTEXT).getHome(
     //         ISecurityServiceHome.class);
     //_remote = _home.create();
     //_remote = new SecurityServiceBean();

     SecurityLogger.log("Test SecurityServices Successful.");
   }
   
  private static byte[] encrypt(
                         com.rsa.certj.cert.X509Certificate partnerCertificate,
                         byte[] contentToEncry,
                         com.rsa.certj.cert.X509Certificate recpCertificate,
                         JSAFE_PrivateKey privateKey)
                         throws SecurityServiceException
                       {
                         try
                         {
                           byte[] contentInfoEncoding = null;
                           initCertDBService(recpCertificate, privateKey);
                           EnvelopedData data =
                             (EnvelopedData) ContentInfo.getInstance(
                               ContentInfo.ENVELOPED_DATA,
                               certJ,
                               pathCtx);
                           ContentInfo content = createDataMessage(contentToEncry);
                           data.setContentInfo(content);
                           RecipientInfo recipient = new RecipientInfo();
                           recipient.setIssuerAndSerialNumber(
                             partnerCertificate.getIssuerName(),
                             partnerCertificate.getSerialNumber(),
                             0,
                             partnerCertificate.getSerialNumber().length);
                           recipient.setEncryptionAlgorithm("RSA");
                           SecurityLogger.log(
                             " Recipent Information "
                               + partnerCertificate.getIssuerName()
                               + " "
                               + partnerCertificate.getSerialNumber()
                               + " "
                               + partnerCertificate.getSerialNumber().length);
                           data.addRecipientInfo(recipient);
                           data.setEncryptionAlgorithm("RC5-0x10-32-16/CBC/PKCS5Padding", 128);
                           int contentInfoEncodingLen = data.getContentInfoDERLen();
                           contentInfoEncoding = new byte[contentInfoEncodingLen];
                           data.writeMessage(contentInfoEncoding, 0);
                           return contentInfoEncoding;
                         }
                         catch (Exception e)
                         {
                           SecurityLogger.log("Exception in encryptData  " + e.getMessage());
                           throw new SecurityServiceException("Encrypt exception ", e);
                         }
                       }
   

  private static byte[] sign(
     com.rsa.certj.cert.X509Certificate signerCertificate,
     byte[] partToSign,
     JSAFE_PrivateKey privateKey)
     throws SecurityServiceException
   {
     try
     {
       byte[] contentInfoEncoding = null;
       initCertDBService(signerCertificate, privateKey);
       SignedData data =
         (SignedData) ContentInfo.getInstance(
           ContentInfo.SIGNED_DATA,
           certJ,
           pathCtx);
       ContentInfo content = createDataMessage(partToSign);
       data.setContentInfo(content);
       SignerInfo signer = new SignerInfo();
       signer.setIssuerAndSerialNumber(
         signerCertificate.getIssuerName(),
         signerCertificate.getSerialNumber(),
         0,
         signerCertificate.getSerialNumber().length);

       signer.setEncryptionAlgorithm("RSA");
       signer.setDigestAlgorithm("SHA1");
       data.addSignerInfo(signer);
       data.createDetachedSignature();

       int contentInfoEncodingLen = data.getContentInfoDERLen();
       contentInfoEncoding = new byte[contentInfoEncodingLen];
       data.writeMessage(contentInfoEncoding, 0);
       return contentInfoEncoding;
     }
     catch (Exception e)
     {
       SecurityLogger.log("Cannot Sign :  Sign Exception  ");
       throw new SecurityServiceException("Sign Exception", e);
     }
   }

   

  private static boolean verify(
     byte[] contentInfoEncoding,
     byte[] signature,
     com.rsa.certj.cert.X509Certificate recpCertificate,
     JSAFE_PrivateKey privateKey)
     throws SecurityServiceException
   {
     try
     {

       initCertDBService(recpCertificate, privateKey);
       SecurityLogger.log("Initilised ");
       SignedData data =
         (SignedData) ContentInfo.getInstance(
           ContentInfo.SIGNED_DATA,
           certJ,
           pathCtx);
       SecurityLogger.log("Created SignedData ");
       data.setContentInfo(createDataMessage(contentInfoEncoding));
       SecurityLogger.log("Set Content Info ");
       data.readInit(signature, 0, signature.length);
       SecurityLogger.log("Read Int Step 1");
       data.readFinal();
       SecurityLogger.log("Final Done ");
       //      Vector signerVector = data.getSignerInfos ();
       //      SignerInfo[] signers = new SignerInfo[signerVector.size()];
       //      for (int index = 0 ; index < signers.length ; index++)
       //      {
       //        signers[index] = (SignerInfo)signerVector.elementAt (index);
       //      }
       //      for (int i = 0 ; i < signers.length ; i++)
       //      {
       //        SecurityLogger.log("Verify Raw Data: SignCert IssuerName " + signers[i].getIssuerName().toString());
       //        SecurityLogger.log("Verify Raw Data: SignCert IssuerNumber ");
       //        SecurityLogger.log(signers[i].getSerialNumber());
       //      }
       SecurityLogger.log("Verify Raw Data: TRUE");
       return true;
     }
     catch (Exception e)
     {
       SecurityLogger.err("Verify Raw Data: Exception", e);
       throw new SecurityServiceException("Verify failed : Verify Raw Data: Exception");
     }
   }

   

  private static void initCertDBService(
     com.rsa.certj.cert.X509Certificate certificate,
     JSAFE_PrivateKey prKey)
   {
     try
     {
       memProvider = new MemoryDB("Test Memory DB");
       Provider pkixCertPathProvider =
         new PKIXCertPath("PKIX Cert Path Verifier");
       Provider[] providers = { memProvider, pkixCertPathProvider };
       certJ = new CertJ(providers);
       //JSAFE_PublicKey publickey = certificate.getSubjectPublicKey("Java");
       DatabaseService dbService =
         (DatabaseService) certJ.bindServices(CertJ.SPT_DATABASE);
       //            PKCS12Reader reader = new PKCS12Reader("d:/security/classes/data/pp12.p12","xx".toCharArray());
       //            reader.read();
       //            JSAFE_PrivateKey prKey = reader.getPrivateKey();

       dbService.insertCertificate(certificate);
       dbService.insertPrivateKeyByCertificate(certificate, prKey);
       Certificate[] certs = new Certificate[1];
       certs[0] = certificate;
       pathCtx =
         new CertPathCtx(
           CertPathCtx.PF_IGNORE_REVOCATION,
           certs,
           null,
           new Date(),
           dbService);

     }
     catch (Exception ex)
     {
       SecurityLogger.err(
         "[SecurityServiceBean][initCertDBService()]Cannot Start CertDB Service",
         ex);
     }

   }

  private static ContentInfo createDataMessage(byte[] content)
   {
     ContentInfo data = null;
     try
     {
       data = ContentInfo.getInstance(ContentInfo.DATA, null, null);
       ((Data) data).setContent(content, 0, content.length);
       return data;
     }
     catch (PKCS7Exception anyException)
     {
       SecurityLogger.log(
         "Cannot Create DataMessage " + anyException.getMessage());
       return null;
     }
   }
   */
}
