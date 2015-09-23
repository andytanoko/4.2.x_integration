/**
 * This software is the proprietary information of CrimsonLogic eTrade Services Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2006-2009 (C) CrimsonLogic eTrade Services Pte Ltd. All Rights Reserved.
 *
 * File: TestSecurityServiceBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 3, 2009   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.security.facade.ejb;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.net.URL;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertPathBuilder;
import java.security.cert.CertStore;
import java.security.cert.CollectionCertStoreParameters;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.TrustAnchor;
import java.security.cert.X509CertSelector;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.security.auth.x500.X500Principal;
import javax.security.auth.x500.X500PrivateCredential;

import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.cms.CMSEnvelopedData;
import org.bouncycastle.cms.CMSEnvelopedDataGenerator;
import org.bouncycastle.cms.CMSProcessable;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.x509.X509V1CertificateGenerator;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;

import com.gridnode.pdip.base.certificate.helpers.SecurityDB;
import com.gridnode.pdip.base.certificate.helpers.SecurityDBManager;
import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;
import com.gridnode.pdip.base.security.helpers.GridCertUtilities;
import com.gridnode.pdip.base.security.helpers.PKCS12Reader;
import com.gridnode.pdip.base.security.helpers.SMimeSecurityInfo;
import com.gridnode.pdip.base.security.helpers.SecurityLogger;
//import com.rsa.certj.CertJ;
//import com.rsa.certj.DatabaseService;
//import com.rsa.certj.cert.Certificate;
//import com.rsa.certj.cert.X509Certificate;
//import com.rsa.certj.pkcs7.ContentInfo;
//import com.rsa.certj.pkcs7.Data;
//import com.rsa.certj.pkcs7.EnvelopedData;
//import com.rsa.certj.pkcs7.SignedData;
//import com.rsa.certj.pkcs7.SignerInfo;
//import com.rsa.certj.spi.path.CertPathCtx;

/**
 * @author Tam Wei Xiang
 * @version GT4.2.1
 * @since GT4.2.1
 */
public class TestSecurityServiceBean
{

  /**
   * @param args
   */
  public static void main(String[] args) throws Exception
  {
    //test encrypt/decrypt with BC, and use own cert....ok
    //testBCEncrypt();
    
    //test encrypt via RSA impl, and decrypt via BC Impl
    
    //test whether RSA include the certificate of the sign cert into the signature...
    //compare the size of the final signature
    testBCSign();
    
    //System.out.println("SHA: "+CMSSignedDataGenerator.DIGEST_MD5);
  }

  private static void testBCSign() throws Exception
  {
    GridCertUtilities.getSecurityProvider();
    
    //Signer 1-- >sign with BC, verify by RSA
    KeyStore credentials = Utils.createCredentials();
    PrivateKey key = (PrivateKey)credentials.getKey(Utils.END_ENTITY_ALIAS, Utils.KEY_PASSWD);
    java.security.cert.Certificate[] chain = credentials.getCertificateChain(Utils.END_ENTITY_ALIAS);
    int i = 0;
    for(java.security.cert.Certificate c : chain)
    {
      
      com.gridnode.pdip.base.certificate.helpers.GridCertUtilities.writeByteTOFile(i+".cer", c.getEncoded(), c.getEncoded().length);
      ++i;
      System.out.println(i+" Certificate: "+c);
    }
    
    CertStore certsAndCRLs = CertStore.getInstance("Collection", new CollectionCertStoreParameters(Arrays.asList(chain[0])), "BC");
    java.security.cert.X509Certificate cert = (java.security.cert.X509Certificate)chain[0];
    
    String publicKeyInStr = com.gridnode.pdip.base.certificate.helpers.GridCertUtilities.writeByteArrayToString(cert.getSerialNumber().toByteArray());
    System.out.println("X509Cert info: "+cert);
    System.out.println("X509Cert serial Base 64: "+publicKeyInStr);
    System.out.println("serial num: "+cert.getSerialNumber());
    System.out.println("Issuer name: "+cert.getIssuerX500Principal());
    
    //Signer 2 -- Multi-signer scenario
    File ownCertPrivateInov = new File("data/pkcs12/inovQATest.p12");
    PKCS12Reader reader = new PKCS12Reader(ownCertPrivateInov.getAbsolutePath(), "changeit".toCharArray());
    reader.read();
    
    
    
    //generator
    CMSSignedDataGenerator gen = new CMSSignedDataGenerator();
    gen.addSigner(key, cert, CMSSignedDataGenerator.DIGEST_SHA1);
    gen.addSigner(reader.getPrivateKey(), reader.getCertificate(), CMSSignedDataGenerator.DIGEST_SHA1);
    gen.addCertificatesAndCRLs(certsAndCRLs);
    
    //signedData obj
    byte[] content = "Hihi".getBytes();
    CMSProcessable data = new CMSProcessableByteArray(content);
    CMSSignedData signed = gen.generate(data, "BC");
    byte[] signature = signed.getEncoded();
    
    //System.out.println("Signed data:"+signed.g);
    
    SMimeSecurityInfo info = new SMimeSecurityInfo();
    info.setDataToVerify(content);
    info.setSignatureToVerify(signature);
    
    File inovCert = new File("data/pkcs12/inovQA.cer");
    X509Certificate inovTPCert = GridCertUtilities.loadX509Certificate(inovCert.getAbsolutePath());
    System.out.println("inovCert: "+inovTPCert);
    info.setReceipentCertificate(cert);
    
    SecurityServiceBean bean = new SecurityServiceBean();
    bean.verify(info);
    //bean.sign(signerCertificate, partToSign, privateKey)
    
  }
  
  private static void testBCEncrypt() throws Exception
  {
    //
    //test encrypt via BC impl, and decrypt via RSA
    //
    java.security.cert.X509Certificate ownCert = com.gridnode.pdip.base.certificate.helpers.GridCertUtilities.loadX509Certificate(new File("data/pkcs12/inovQA.cer"));
    java.security.cert.X509Certificate receiverCert = com.gridnode.pdip.base.certificate.helpers.GridCertUtilities.loadX509Certificate(new File("data/pkcs12/testP12.cer"));
    
    
    byte[] encryptedContent = encryptJava((java.security.cert.X509Certificate)receiverCert, (java.security.cert.X509Certificate)ownCert, "haha".getBytes(), CMSEnvelopedDataGenerator.DES_EDE3_CBC, 168);
    System.out.println("Encrypted data: "+new String(encryptedContent));
    
    //
    //decrypt by RSA
    //
//    System.out.println("Start Decrypt process via RSA");
//    SecurityDB secDB = SecurityDBManager.getInstance().getSecurityDB(); //TWX 03082006
//    
//    CertJ certJ = secDB.getCertJ();
//    DatabaseService dbService = secDB.getDBService();
//    
//    Certificate[] tCerts = new Certificate[0];
    //Certificate ownCert1 = loadRSACert(new File("data/pkcs12/testP12.cer"));
    //Certificate receiverCert1 = loadRSACert(new File("data/pkcs12/inovQA.cer"));
    
    //test decrypt using sender own cert
//    Certificate ownCert1 = loadRSACert(new File("data/pkcs12/inovQA.cer"));
//    Certificate receiverCert1 = loadRSACert(new File("data/pkcs12/testP12.cer"));
//    
//    tCerts = addMoreTrustedCerts(tCerts, ownCert1);
//    tCerts = addMoreTrustedCerts(tCerts, receiverCert1);
//    
//    CertPathCtx pathCtx = new CertPathCtx(CertPathCtx.PF_IGNORE_REVOCATION,tCerts,null,new Date(),dbService);
//    byte[] decryptedContent = decrypt((X509Certificate)ownCert1, encryptedContent, certJ, pathCtx);
//    System.out.println("Decrypted content: "+new String(decryptedContent));
    
    //
    //decrypt via BC
    //load private key
    //File privateKeyFile = new File("data/pkcs12/testP12.p12");
    File privateKeyFile = new File("data/pkcs12/inovQATest.p12"); //,  use ownCert
    
    PKCS12Reader reader = new PKCS12Reader(privateKeyFile.getAbsolutePath(), "changeit".toCharArray());
    reader.read();
    PrivateKey pKey = reader.getPrivateKey();
    
    SecurityServiceBean bean = new SecurityServiceBean();
    byte[] decrypted = bean.decrypt(encryptedContent, ownCert, pKey);
    System.out.println("Decrypted content:"+new String(decrypted));
  }
  
//  private static Certificate[] addMoreTrustedCerts(Certificate[] certs, Certificate newCert)
//  {
//    Certificate[] newCerts = new Certificate[certs.length + 1];
//    System.arraycopy(certs, 0, newCerts, 0, certs.length);
//    newCerts[certs.length] = newCert;
//    return newCerts;
//  }
  
//  private static X509Certificate loadRSACert(File cert) throws Exception
//  {
//    return  com.gridnode.pdip.base.certificate.helpers.GridCertUtilities.loadX509Certificate(cert);
//  }
  
  private static java.security.cert.X509Certificate loadJavaCert(File cert)
  {
    return com.gridnode.pdip.base.certificate.helpers.GridCertUtilities.loadX509Certificate(cert);
  }
  
  public static byte[] encryptJava(
                            java.security.cert.X509Certificate partnerCertificate,
                            java.security.cert.X509Certificate ownCertificate,
                            byte[] contentToEncry,
                            String encryptionAlgorithm,
                            int encryptionLevel)
                            throws SecurityServiceException
      {
        try
        {
          CMSProcessable content = createDataMessageJava(contentToEncry);
          CMSEnvelopedDataGenerator gen = new CMSEnvelopedDataGenerator();
          gen.addKeyTransRecipient(partnerCertificate);

          SecurityLogger.debug("Recipent Information "
                                    + partnerCertificate.getIssuerX500Principal()
                                    + "Serail No "
                                    + partnerCertificate.getSerialNumber()
                                    + "Length    "
                                    + partnerCertificate.getSerialNumber().toByteArray().length);
          
          //TWX 24072006: We can decrypt the document we encrypted. RSA support multiple recipient
          gen.addKeyTransRecipient(ownCertificate);
          SecurityLogger.debug("Self As Recipent Information "
                                                       + ownCertificate.getIssuerX500Principal()
                                                       + "Serail No "
                                                       + ownCertificate.getSerialNumber()
                                                       + "Length    "
                                                       + ownCertificate.getSerialNumber().toByteArray().length);
          
          CMSEnvelopedData enveloped = gen.generate(content, encryptionAlgorithm, GridCertUtilities.getSecurityProvider());
          return enveloped.getEncoded();
        }
        catch (Exception e)
        {
          SecurityLogger.log("Exception in encryptData  " + e.getMessage());
          throw new SecurityServiceException("Encrypt exception ", e);
        }
     }
  
  private static CMSProcessable createDataMessageJava(byte[] content)
  {
    return new CMSProcessableByteArray(content);
  }
  
//  public static byte[] decrypt(
//                        X509Certificate recpCertificate,
//                        byte[] partToDecryped,
//                        CertJ certJ,
//                        CertPathCtx pathCtx)
//                        throws SecurityServiceException
//                      {
//                        byte[] decryptdata = null;
//                        try
//                        {
//                          byte[] contentInfoEncoding = partToDecryped;
//                          EnvelopedData data =
//                            (EnvelopedData) ContentInfo.getInstance(
//                              ContentInfo.ENVELOPED_DATA,
//                              certJ,
//                              pathCtx);
//                          data.readInit(contentInfoEncoding, 0, contentInfoEncoding.length);
//                          data.readFinal();
//                          ContentInfo content = data.getContent();
//
//                          if (content.getContentType() == ContentInfo.DATA)
//                          {
//                            decryptdata = ((Data) content).getData();
//                          }
//                          else
//                          {
//                            throw new SecurityServiceException("Wrong ContentType for decrypted data");
//                          }
//                        }
//                        catch (SecurityServiceException ex)
//                        {
//                          SecurityLogger.warn("Exception in Decrypt  ", ex);
//                          throw ex;
//                        }
//                        catch (Exception e)
//                        {
//                          SecurityLogger.warn("Exception in Decrypt ", e);
//                          throw new SecurityServiceException("Decrypt exception ", e);
//                        }
//                        return decryptdata;
//                      }
  
  private static byte[] loadFromFile(File input) throws Exception
  {
    FileInputStream in = new FileInputStream(input);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    byte[] buffer = new byte[512];
    int readSoFar = 0;
    while( (readSoFar = in.read(buffer)) > -1)
    {
      out.write(buffer, 0, readSoFar);
    }
    in.close();
    return out.toByteArray();
  }
  
  //RSA sign implementation
  //
  /*
  public SMimeSecurityInfo sign(SMimeSecurityInfo info)
  throws SecurityServiceException
{
  //SMimeSecurityInfo signInfo = null;
  SecurityDB secDB = null;
  
  try
  {
    X509Certificate ownCertificate = info.getOwnCertificate();
    String encryptionAlgorithm = info.getEncryptionAlgorithm();
    String digestAlgorithm = info.getDigestAlgorithm();
    byte[] partToSign = info.getDataToSign();
    //signInfo = new SMimeSecurityInfo();
    if (partToSign == null)
    {
      partToSign = info.getPartToSign().getContentByte(false);
      SecurityLogger.debug("[SecurityServiceBean][sign] Data from MailPart");
      //signInfo.setIMailpartToSign(info.getPartToSign());
    }
    SecurityLogger.debug("Begin of Sign ");
    SecurityLogger.debug(
      "ownCertificate " + new String(ownCertificate.getSerialNumber()));
    SecurityLogger.debug("EncryptionAlgorithm " + encryptionAlgorithm);
    SecurityLogger.debug("digestAlgorithm " + digestAlgorithm);
    
    secDB = SecurityDBManager.getInstance().getSecurityDB();//TWX 03082006
    
    CertJ certJ = secDB.getCertJ();
    DatabaseService dbService = secDB.getDBService();
    Certificate[] tCerts = new Certificate[0];
    tCerts = addMoreTrustedCerts(tCerts, ownCertificate);
    tCerts = addMoreTrustedCerts(tCerts, info.getReceipentCertificate());
    pathCtx =
      new CertPathCtx(
        CertPathCtx.PF_IGNORE_REVOCATION,
        tCerts,
        null,
        new Date(),
        dbService);

    byte[] signedData =
      sign(
        ownCertificate,
        partToSign,
        certJ,
        pathCtx,
        encryptionAlgorithm,
        digestAlgorithm);

    info.setSignedData(signedData);
    return info;
  }
  catch (Exception ex)
  {
    SecurityLogger.warn("Cannot Sign Data ", ex);
    throw new SecurityServiceException("Cannot Sign Data ", ex);
  }
  finally
  {
    if(secDB != null)
    {
      SecurityDBManager.getInstance().releaseSecurityDB(secDB); //TWX 03082006
    }
  }
}

public byte[] sign(
  X509Certificate signerCertificate,
  byte[] partToSign,
  CertJ certJ,
  CertPathCtx pathCtx,
  String encryptionAlgorithm,
  String digestAlgorithm)
  throws SecurityServiceException
{
  try
  {
    byte[] contentInfoEncoding = null;
    SecurityLogger.debug("[SecurityServiceBean][sign] begin");
    SignedData data =
      (SignedData) ContentInfo.getInstance(
        ContentInfo.SIGNED_DATA,
        certJ,
        pathCtx);
    ContentInfo content = createDataMessage(partToSign, "sign");
    SecurityLogger.debug(
      "[SecurityServiceBean][sign] After DataMessage Created");
    data.setContentInfo(content);
    SignerInfo signer = new SignerInfo();
    signer.setIssuerAndSerialNumber(
      signerCertificate.getIssuerName(),
      signerCertificate.getSerialNumber(),
      0,
      signerCertificate.getSerialNumber().length);
    SecurityLogger.debug("[SecurityServiceBean][sign] After Signer Created");
    //signer.setEncryptionAlgorithm (encryptionAlgorithm);
    signer.setEncryptionAlgorithm("RSA");
    signer.setDigestAlgorithm(digestAlgorithm);

    //signer.setDigestAlgorithm ("SHA1");
    data.addSignerInfo(signer);
    data.addCertificate(signerCertificate);
    data.createDetachedSignature();

    SecurityLogger.debug(
      "[SecurityServiceBean][sign] After Data Signature Created");

    int contentInfoEncodingLen = data.getContentInfoDERLen();
    SecurityLogger.debug(
      "[SecurityServiceBean][sign] ContentInfo Encoding Len "
        + contentInfoEncodingLen);
    contentInfoEncoding = new byte[contentInfoEncodingLen];

    SecurityLogger.debug(
      "[SecurityServiceBean][sign] After ContentInfo Encoding");

    data.writeMessage(contentInfoEncoding, 0);

    SecurityLogger.debug("[SecurityServiceBean][sign] B4 Return...");
    return contentInfoEncoding;
  }
  catch (Exception e)
  {
    e.printStackTrace();
    SecurityLogger.log("Cannot Sign :  Sign Exception  ");
    throw new SecurityServiceException("Sign Exception", e);
  }

}  */
}

class Utils
{
  public static char[] KEY_PASSWD = "keyPassword".toCharArray();
  
  /**
   * Create a KeyStore containing the a private credential with
   * certificate chain and a trust anchor.
   */
  public static KeyStore createCredentials()
      throws Exception
  {
      KeyStore store = KeyStore.getInstance("JKS");

      store.load(null, null);
      
      X500PrivateCredential    rootCredential = createRootCredential();
      X500PrivateCredential    interCredential = createIntermediateCredential(rootCredential.getPrivateKey(), rootCredential.getCertificate());
      X500PrivateCredential    endCredential = createEndEntityCredential(interCredential.getPrivateKey(), interCredential.getCertificate());
      
      store.setCertificateEntry(rootCredential.getAlias(), rootCredential.getCertificate());
      store.setKeyEntry(endCredential.getAlias(), endCredential.getPrivateKey(), KEY_PASSWD, 
              new java.security.cert.Certificate[] { endCredential.getCertificate(), interCredential.getCertificate(), rootCredential.getCertificate() });

      return store;
  }
  
  /**
   * Build a path using the given root as the trust anchor, and the passed
   * in end constraints and certificate store.
   * <p>
   * Note: the path is built with revocation checking turned off.
   */
  public static PKIXCertPathBuilderResult buildPath(
      java.security.cert.X509Certificate  rootCert,
      X509CertSelector endConstraints,
      CertStore        certsAndCRLs)
      throws Exception
  {
      CertPathBuilder       builder = CertPathBuilder.getInstance("PKIX", "BC");
      PKIXBuilderParameters buildParams = new PKIXBuilderParameters(Collections.singleton(new TrustAnchor(rootCert, null)), endConstraints);
      
      buildParams.addCertStore(certsAndCRLs);
      buildParams.setRevocationEnabled(false);
      
      return (PKIXCertPathBuilderResult)builder.build(buildParams);
  }
  
  /**
   * Create a MIME message from using the passed in content.
   */
  public static MimeMessage createMimeMessage(
      String subject, 
      Object content, 
      String contentType) 
      throws MessagingException
  {
      Properties props = System.getProperties();
      Session session = Session.getDefaultInstance(props, null);

      Address fromUser = new InternetAddress("\"Eric H. Echidna\"<eric@bouncycastle.org>");
      Address toUser = new InternetAddress("example@bouncycastle.org");

      MimeMessage message = new MimeMessage(session);
      
      message.setFrom(fromUser);
      message.setRecipient(Message.RecipientType.TO, toUser);
      message.setSubject(subject);
      message.setContent(content, contentType);
      message.saveChanges();
      
      return message;
  }
  
  private static final int VALIDITY_PERIOD = 7 * 24 * 60 * 60 * 1000; // one week
  
  /**
   * Generate a sample V1 certificate to use as a CA root certificate
   */
  public static java.security.cert.X509Certificate generateRootCert(KeyPair pair)
      throws Exception
{
    X509V1CertificateGenerator  certGen = new X509V1CertificateGenerator();

    certGen.setSerialNumber(BigInteger.valueOf(1));
    certGen.setIssuerDN(new X500Principal("CN=Test CA Certificate"));
    certGen.setNotBefore(new Date(System.currentTimeMillis()));
    certGen.setNotAfter(new Date(System.currentTimeMillis() + VALIDITY_PERIOD));
    certGen.setSubjectDN(new X500Principal("CN=Test CA Certificate"));
    certGen.setPublicKey(pair.getPublic());
    certGen.setSignatureAlgorithm("SHA1WithRSAEncryption");

    return certGen.generateX509Certificate(pair.getPrivate(), "BC");
}
  
  /**
   * Generate a sample V3 certificate to use as an intermediate CA certificate
   */
  public static java.security.cert.X509Certificate generateIntermediateCert(PublicKey intKey, PrivateKey caKey, java.security.cert.X509Certificate caCert)
      throws Exception
  {
      X509V3CertificateGenerator  certGen = new X509V3CertificateGenerator();

      certGen.setSerialNumber(BigInteger.valueOf(1));
      certGen.setIssuerDN(caCert.getSubjectX500Principal());
      certGen.setNotBefore(new Date(System.currentTimeMillis()));
      certGen.setNotAfter(new Date(System.currentTimeMillis() + VALIDITY_PERIOD));
      certGen.setSubjectDN(new X500Principal("CN=Test Intermediate Certificate"));
      certGen.setPublicKey(intKey);
      certGen.setSignatureAlgorithm("SHA1WithRSAEncryption");
  
      certGen.addExtension(X509Extensions.AuthorityKeyIdentifier, false, new AuthorityKeyIdentifierStructure(caCert));
      certGen.addExtension(X509Extensions.SubjectKeyIdentifier, false, new SubjectKeyIdentifierStructure(intKey));
      certGen.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(0));
      certGen.addExtension(X509Extensions.KeyUsage, true, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyCertSign | KeyUsage.cRLSign));

      return certGen.generateX509Certificate(caKey, "BC");
  }
  
  /**
   * Generate a sample V3 certificate to use as an end entity certificate
   */
  public static java.security.cert.X509Certificate generateEndEntityCert(PublicKey entityKey, PrivateKey caKey, java.security.cert.X509Certificate caCert)
    throws Exception
    {
      X509V3CertificateGenerator  certGen = new X509V3CertificateGenerator();

      certGen.setSerialNumber(BigInteger.valueOf(1));
      certGen.setIssuerDN(caCert.getSubjectX500Principal());
      certGen.setNotBefore(new Date(System.currentTimeMillis()));
      certGen.setNotAfter(new Date(System.currentTimeMillis() + VALIDITY_PERIOD));
      certGen.setSubjectDN(new X500Principal("CN=Test End Certificate"));
      certGen.setPublicKey(entityKey);
      certGen.setSignatureAlgorithm("SHA1WithRSAEncryption");
      
      certGen.addExtension(X509Extensions.AuthorityKeyIdentifier, false, new AuthorityKeyIdentifierStructure(caCert));
      certGen.addExtension(X509Extensions.SubjectKeyIdentifier, false, new SubjectKeyIdentifierStructure(entityKey));
      certGen.addExtension(X509Extensions.BasicConstraints, true, new BasicConstraints(false));
      certGen.addExtension(X509Extensions.KeyUsage, true, new KeyUsage(KeyUsage.digitalSignature | KeyUsage.keyEncipherment));

      return certGen.generateX509Certificate(caKey, "BC");
  }
  
  /**
   * Create a random 1024 bit RSA key pair
   */
  public static KeyPair generateRSAKeyPair()
      throws Exception
  {
      KeyPairGenerator  kpGen = KeyPairGenerator.getInstance("RSA", "BC");
  
      kpGen.initialize(1024, new SecureRandom());
  
      return kpGen.generateKeyPair();
  }
  
  public static String ROOT_ALIAS = "root";
  public static String INTERMEDIATE_ALIAS = "intermediate";
  public static String END_ENTITY_ALIAS = "end";
  
  /**
   * Generate a X500PrivateCredential for the root entity.
   */
  public static X500PrivateCredential createRootCredential()
      throws Exception
  {
      KeyPair         rootPair = generateRSAKeyPair();
      java.security.cert.X509Certificate rootCert = generateRootCert(rootPair);
      
      return new X500PrivateCredential(rootCert, rootPair.getPrivate(), ROOT_ALIAS);
  }
  
  /**
   * Generate a X500PrivateCredential for the intermediate entity.
   */
  public static X500PrivateCredential createIntermediateCredential(
      PrivateKey      caKey,
      java.security.cert.X509Certificate caCert)
      throws Exception
  {
      KeyPair         interPair = generateRSAKeyPair();
      java.security.cert.X509Certificate interCert = generateIntermediateCert(interPair.getPublic(), caKey, caCert);
      
      return new X500PrivateCredential(interCert, interPair.getPrivate(), INTERMEDIATE_ALIAS);
  }
  
  /**
   * Generate a X500PrivateCredential for the end entity.
   */
  public static X500PrivateCredential createEndEntityCredential(
      PrivateKey      caKey,
      java.security.cert.X509Certificate caCert)
      throws Exception
  {
      KeyPair         endPair = generateRSAKeyPair();
      java.security.cert.X509Certificate endCert = generateEndEntityCert(endPair.getPublic(), caKey, caCert);
      
      return new X500PrivateCredential(endCert, endPair.getPrivate(), END_ENTITY_ALIAS);
  }
}
