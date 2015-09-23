package com.gridnode.pdip.base.certificate.helpers;

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JavaKeyStoreHelper.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 28 April 2003  Qingsong            Initial creation for GTAS 2.0
 */

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.security.NoSuchAlgorithmException;

import com.gridnode.pdip.base.transport.helpers.JavaKeyStoreHandler;

public class JavaKeyStoreHelper {

  static public boolean emptyString(String str)
  {
    if(str == null || str.length() <= 0)
     return true;
    else
     return false;
  }

  static public String Exception2String(Throwable e)
  {
      ByteArrayOutputStream detailedError = new ByteArrayOutputStream();
      PrintStream outS = new PrintStream(detailedError);
      if(e != null)
        e.printStackTrace(outS);
      outS.flush();
      outS.close();
      return new String(detailedError.toByteArray());
  }

  /*
  static public PrivateKey convertPrivateKey(JSAFE_PrivateKey akey) throws JSAFE_UnimplementedException, NoSuchAlgorithmException, InvalidKeySpecException, IOException
  {
    if(akey == null)
       return null;
    return JavaKeyStoreHandler.loadPrivateKey(akey.getKeyData("RSAPrivateKeyBER")[0]);
  }*/

//  static public com.rsa.certj.cert.X509Certificate[] convertCertificateChain(java.security.cert.Certificate[] certchain) throws com.rsa.certj.cert.CertificateException, java.security.cert.CertificateException
//  {
//    if(certchain == null)
//       return null;
//    int size = certchain.length;
//    com.rsa.certj.cert.X509Certificate[] cc = new com.rsa.certj.cert.X509Certificate[size];
//    for(int i = 0; i < size; i++)
//      cc[i] = convertCertificate(certchain[i]);
//    return cc;
//  }

//  static public java.security.cert.X509Certificate[] convertCertificateChain(com.rsa.certj.cert.X509Certificate[] certchain) throws com.rsa.certj.cert.CertificateException, java.security.cert.CertificateException, IOException, NoSuchAlgorithmException
//  {
//    if(certchain == null)
//       return null;
//    int size = certchain.length;
//    java.security.cert.X509Certificate[] cc = new java.security.cert.X509Certificate[size];
//    for(int i = 0; i < size; i++)
//      cc[i] = convertCertificate(certchain[i]);
//    return cc;
//  }

//  static public com.rsa.certj.cert.X509Certificate convertCertificate(java.security.cert.Certificate cert) throws com.rsa.certj.cert.CertificateException, java.security.cert.CertificateException
//  {
//      if(cert == null)
//       return null;
//    return new com.rsa.certj.cert.X509Certificate(JavaKeyStoreHandler.writeCertificate(cert), 0, 0);
//  }

//  static public java.security.cert.X509Certificate convertCertificate(com.rsa.certj.cert.X509Certificate cert) throws com.rsa.certj.cert.CertificateException, java.security.cert.CertificateException, IOException, NoSuchAlgorithmException
//  {
//      if(cert == null)
//       return null;
//      byte[] certificateData = new byte[cert.getDERLen (0)];
//      cert.getDEREncoding (certificateData, 0, 0);
//      return JavaKeyStoreHandler.loadCertificate(certificateData);
//  }

  /*
  static public JSAFE_PrivateKey convertPrivateKey(Key akey) throws JSAFE_UnimplementedException, NoSuchAlgorithmException, InvalidKeySpecException
  {
    if(akey == null)
       return null;
    return JSAFE_PrivateKey.getInstance(JavaKeyStoreHandler.writePrivateKey(akey), 0, "Java");
  }*/

  public JavaKeyStoreHelper()
  {
  }

  public static void main(String[] args) throws Exception
  {
  /*
    JavaKeyStoreHelper javaKeyStoreHelper1 = new JavaKeyStoreHelper();
    //JSAFE_PrivateKey rsakey = javaKeyStoreHelper1.convertPrivateKey(handler.getKey("tomcat"));
    PKCS12Reader p12reader = new PKCS12Reader("C:\\TomcatRN\\webapps\\rosettanet\\repositoryV2.0\\tpa\\securitydata\\keys\\encryptkey-123456789.p12", "mysecret".toCharArray());
    p12reader.read();
    //exportTrustedJavaKeyStore("newentry", p12reader.getCertificate());
    //exportTomcatJavaKeyStore("c:\\tomcatkeystore", "changeit",)
    //GridCertUtilities.exportJavaKeyStore("c:\\tomcatstore","test",p12reader.getCertificate(), p12reader.getPrivateKey());

    PrivateKey key = convertPrivateKey(p12reader.getPrivateKey());
    JavaKeyStoreHandler handler = new JavaKeyStoreHandler();
    handler.open();
    //handler.insert("newkey", key, "changeit",convertCertificate(p12reader.getCertificate()));
    System.out.println(" " + handler);
    handler.setFilename("c:\\testkeystore");
    handler.write();
    */
  }
}