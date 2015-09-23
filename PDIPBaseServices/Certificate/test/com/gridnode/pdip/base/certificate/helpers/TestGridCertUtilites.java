/**
 * This software is the proprietary information of CrimsonLogic eTrade Services Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2006-2009 (C) CrimsonLogic eTrade Services Pte Ltd. All Rights Reserved.
 *
 * File: TestGridCertUtilites.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 9, 2009   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.certificate.helpers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.Reader;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509CRL;
import java.security.cert.X509CRLEntry;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Vector;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509ExtensionsGenerator;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.PrincipalUtil;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.openssl.PEMReader;

//import com.rsa.certj.cert.RevokedCertificates;
//import com.rsa.certj.cert.X500Name;
//import com.rsa.jsafe.JSAFE_PrivateKey;

/**
 * @author Tam Wei Xiang
 * @version GT4.2.1
 * @since GT4.2.1
 */
public class TestGridCertUtilites
{
  public static void main(String[] args) throws Exception
  {
    //Test load CRL
    
    //testLoadCRL();
    
    //testWrapPrivateKey();
    
    //loadX509Cert();
    
    //loadPrivateCert();
    
    //compareX509Extensions();
    
    //isCertValid();
    
    //loadCertRequestFromFile();
    
    //writePrivateKeyData();
    
    //writePublicKeyInStr();
    
    //writePrivateKeyInStr();
    
    File file = new File("C:/Gridnode/GridTalk/jboss-4.2.2.GA/server/default/deploy/jboss-web.deployer");
    System.out.println(""+file.getAbsoluteFile().getAbsolutePath());
  }
  
  private static void writePrivateKeyInStr() throws Exception
  {
    File privateKeyFile = new File("data/pkcs12/testP12.p12");
    PKCS12Reader reader = new PKCS12Reader(privateKeyFile.getAbsolutePath(), "changeit".toCharArray());
    reader.read();
    
    PrivateKey key = reader.getPrivateKey();
    String keyInStr = GridCertUtilities.writePrivateKeyToString(key);
    System.out.println("Public key in Str: "+keyInStr);
    
    key = GridCertUtilities.loadPrivateKeyFromString(keyInStr);
    System.out.println("Key algo:"+key.getAlgorithm());
  }
  
  private static void writePublicKeyInStr() throws Exception
  {
    File privateKeyFile = new File("data/pkcs12/testP12.p12");
    PKCS12Reader reader = new PKCS12Reader(privateKeyFile.getAbsolutePath(), "changeit".toCharArray());
    reader.read();
    
    PublicKey key = reader.getCertificate().getPublicKey();
    String keyInStr = GridCertUtilities.writePublicKeyToString(key);
    System.out.println("Public key in Str: "+keyInStr);
    
    key = GridCertUtilities.loadPublicKeyFromString(keyInStr);
    System.out.println("Key algo:"+key.getAlgorithm());
  }
  
  private static void writePrivateKeyData() throws Exception
  {
    
    System.out.println("Import certificate action");
    Provider[] providers = Security.getProviders();
    for(Provider prod : providers)
    {
      System.out.println("Provider: "+prod.getName()+" Info: "+prod.getInfo());
    }
    
    
    File privateKeyFile = new File("data/pkcs12/testP12.p12");
    PKCS12Reader reader = new PKCS12Reader(privateKeyFile.getAbsolutePath(), "changeit".toCharArray());
    reader.read();
    
    //output to pkcs8 format
    PrivateKey key = reader.getPrivateKey();
    byte[] pkcs8Key = GridCertUtilities.writePKCS8PrivateKeyData("xxx", key);
    
    //load from pkcs8
    key = GridCertUtilities.loadPKCS8PrivateKeyData("xxx".toCharArray(), pkcs8Key);
    System.out.println("Key info: "+key.getAlgorithm());
  }
  
  private static void loadCertRequestFromFile() throws Exception
  {
    Reader fileReader = new FileReader(new File("data/pkcs10/certRequest.txt"));
    PEMReader reader = new PEMReader(fileReader);
    PKCS10CertificationRequest certReq = (PKCS10CertificationRequest)reader.readObject();
    
    GridCertUtilities.writePKCS10CertRequest((new File("data/pkcs10/certReq.txt")).getAbsolutePath(), certReq);
//    byte[] buffer = new byte[512];
//    int lenght = 0;
//    byte[] certReqByte = certReq.getEncoded();
//    ByteArrayInputStream in = new ByteArrayInputStream(certReqByte);
//    FileOutputStream out = new FileOutputStream("data/pkcs10/certReq.txt");
//    while( (lenght = in.read(buffer)) > -1 )
//    {
//      out.write(buffer, 0, lenght);
//    }
//    out.close();
    
    
    File certReqFile = new File("data/pkcs10/certReq.txt");
    String fileName = certReqFile.getAbsolutePath();
    GridCertUtilities.loadPKCS10CertificationRequest(fileName);
    System.out.println("Load cert request ok");
  }
  
  private static void testLoadCRL() throws Exception
  {
    // create CA keys and certificate
    KeyPair         caPair = TestUtil.generateRSAKeyPair();
    X509Certificate caCert = TestUtil.generateRootCert(caPair);
    BigInteger      revokedSerialNumber = BigInteger.valueOf(2);

    // create a CRL revoking certificate number 2
    X509CRL         crl = TestUtil.createCRL(
                       caCert, caPair.getPrivate(), revokedSerialNumber);

    byte[] encodedCRL = crl.getEncoded();
    X509CRL loadedCRL = GridCertUtilities.loadX509CRL(encodedCRL);
    
//  check if the CRL revokes certificate number 2
    X509CRLEntry entry = loadedCRL.getRevokedCertificate(revokedSerialNumber);
    System.out.println("Revocation Details:");
    System.out.println(" Certificate number: " + entry.getSerialNumber());
    System.out.println(" Issuer            : " +loadedCRL.getIssuerX500Principal());

    //output X509CRL
    GridCertUtilities.writeX509CRL("x509.crl", loadedCRL);
    
    //use RSA
    /*
    com.rsa.certj.cert.X509CRL rsaCRL = GridCertUtilities.loadX509CRL(crl.getEncoded());
    RevokedCertificates certs = rsaCRL.getRevokedCertificates();
    System.out.println(certs.getCertificateCount());
    */
  }
  
  private static void testWrapPrivateKey()
  {
    //GridCertUtilities.writePKCS8PrivateKeyData("", null);
  }
  
//  private static void loadX509Cert() throws Exception
//  {
//    File certFile = new File("c:/seagateE2open.cer");
//    com.rsa.certj.cert.X509Certificate cert = GridCertUtilities.loadX509Certificate(certFile);
//    X500Name name = cert.getIssuerName();
//    cert.getSerialNumber();
//    System.out.println("RSA X500: "+GridCertUtilities.writeIssuerNameToString(name));
//    System.out.println("RSA X500: "+name);
//    System.out.println("RSA Serial "+GridCertUtilities.writeByteArrayToString(cert.getSerialNumber()));
//    System.out.println("RSA Public key "+GridCertUtilities.writePublicKeyToString(cert.getSubjectPublicKey("Java")));
//    System.out.println("RSA StartD: "+cert.getStartDate()+" EndD:"+cert.getEndDate());
//    System.out.println("RSA Cert In String:"+GridCertUtilities.writeCertificateToString(cert));
//    System.out.println("RSA Issuer x500Constant: "+GridCertUtilities.getX500Constants(cert.getIssuerName()));
//    System.out.println("RSA Subject x500Constant: "+GridCertUtilities.getX500Constants(cert.getSubjectName()));
//    
//    X509Certificate javaCert = GridCertUtilities.loadX509CertificateJava(certFile);
//    System.out.println("Java X500: "+GridCertUtilities.writeIssuerNameToString(javaCert.getIssuerX500Principal()) + " "+javaCert.getIssuerX500Principal() );
//    System.out.println("Java X500: "+javaCert.getIssuerDN());
//    System.out.println("JAVA Serial "+GridCertUtilities.writeByteArrayToString(javaCert.getSerialNumber().toByteArray()));
//    System.out.println("Java Public key "+GridCertUtilities.writePublicKeyToString(javaCert.getPublicKey()));
//    System.out.println("Java startD:"+javaCert.getNotBefore()+" EndD:"+javaCert.getNotAfter());
//    
//    String certInStr = GridCertUtilities.writeCertificateToString(javaCert);
//    System.out.println("Java Cert In String:"+certInStr);
//    
//    //construct cert given cert str
//    X509Certificate javaCertFromStr = GridCertUtilities.loadX509CertificateByStringJava(certInStr);
//    X509Principal principal = PrincipalUtil.getIssuerX509Principal(javaCertFromStr);
//    System.out.println("Java load from Str: "+javaCertFromStr);
//
//    //Test getting X500ConstantsJava
//    System.out.println("Java Issuer X500 constant");
//    GridCertUtilities.getX500ConstantsJava(PrincipalUtil.getIssuerX509Principal(javaCertFromStr));
//    
//    System.out.println("Java Subject X500 constant");
//    GridCertUtilities.getX500ConstantsJava(PrincipalUtil.getSubjectX509Principal(javaCertFromStr));
//    
//    //test Load cert from byte
//    X509Certificate loadFromByte = GridCertUtilities.loadCertificateFromByteJava(GridCertUtilities.writeCertificateToByte(javaCert));
//    System.out.println("Java Cert Load From byte:"+loadFromByte);
//    
//    //comparing X500Principal
//    X500Principal subjectName = javaCert.getSubjectX500Principal();
//    X500Principal subjectFromByte = loadFromByte.getSubjectX500Principal();
//    System.out.println("Comparing x500 isMatched? "+subjectName.equals(subjectFromByte));
//  }
  
//  private static void loadPrivateCert() throws Exception
//  {
//    File p12 = new File("data/pkcs12/sample.p12");
//    char[] password = "password".toCharArray();
//    PKCS12Reader reader = new PKCS12Reader(p12.getAbsolutePath(), password);
//    reader.read();
//    JSAFE_PrivateKey privateKey = reader.getPrivateKey();
//    System.out.println("RSA Private key to sting:"+GridCertUtilities.writePrivateKeyToString(privateKey));
//    
//    reader.readBC();
//    PrivateKey privateKey2 = reader.getPrivateKeyBC();
//    System.out.println("Java PrivateKey to string:"+GridCertUtilities.writePrivateKeyToString(privateKey2));
//    
//  }
  
  private static void isCertValid()
  {
//  cert
    File cert = new File("data/x509/myCert.cer");
    X509Certificate x509Cert = GridCertUtilities.loadX509Certificate(cert);
    GridCertUtilities.isValid(x509Cert);
  }
  
  //TWX: Not yet completed
  private static void compareX509Extensions()
  {
    //cert
    System.out.println("Load from cert");
    File cert = new File("data/x509/myCert2.cer");
    X509Certificate x509Cert = GridCertUtilities.loadX509Certificate(cert);
    X509Extensions extensionsFromCertFile2 = GridCertUtilities.getX509ExtensionsFromCert(x509Cert);
    Enumeration oids = extensionsFromCertFile2.oids();
    while(oids.hasMoreElements())
    {
      Object oid = oids.nextElement();
      System.out.println("BC OID: "+oid + " value:"+extensionsFromCertFile2.getExtension((DERObjectIdentifier)oid).getValue());
    }
    
    //cert
    System.out.println("Load from cert");
    cert = new File("data/x509/myCert.cer");
    x509Cert = GridCertUtilities.loadX509Certificate(cert);
    X509Extensions extensionsFromCertFile = GridCertUtilities.getX509ExtensionsFromCert(x509Cert);
    oids = extensionsFromCertFile.oids();
    while(oids.hasMoreElements())
    {
      Object oid = oids.nextElement();
      System.out.println("BC OID: "+oid + " value:"+extensionsFromCertFile.getExtension((DERObjectIdentifier)oid).getValue());
    }
    
    
    if(extensionsFromCertFile2.equivalent(extensionsFromCertFile))
    {
      System.out.println("X509 extension is equivalent");
    }
    else
    {
      System.out.println("X509 extension is not match");
    }
  }
  
  private static X509KeyUsage createKeyUsageExtension()
  {
    // the KeyUsage extension determines what can be done with a given key.
    // In this case, the key can be used for encrypting and signing.
    return new X509KeyUsage(X509KeyUsage.dataEncipherment | 
                               X509KeyUsage.digitalSignature);
  }
  
}
