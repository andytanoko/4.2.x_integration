/**
 * This software is the proprietary information of CrimsonLogic eTrade Services Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2006-2009 (C) CrimsonLogic eTrade Services Pte Ltd. All Rights Reserved.
 *
 * File: TestCertGen.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 23, 2009   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.certificate.helpers;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.x509.X509Extensions;

import com.gridnode.pdip.base.certificate.rsa.RDNAttributes;
import com.gridnode.pdip.base.certificate.rsa.RSACertRequest;
import com.gridnode.pdip.base.certificate.rsa.RSAKeyPair;
import com.gridnode.pdip.base.certificate.rsa.RSASelfSignedCert;

/**
 * @author Tam Wei Xiang
 * @version GT4.2.1
 * @since GT4.2.1
 */
public class TestCertGen
{
  public static void main(String[] args)throws Exception
  {
    //generate self sign cert
    generateSelfSignCert();
  }
  
  private static void generateSelfSignCert() throws Exception
  {
//  create RSAKeyPair
    RSAKeyPair keyPair = new RSAKeyPair(2048, RSAKeyPair.PUBLIC_EXPONENT_65537);
    
    
    if (!keyPair.isKeyPairGenerated())
      throw new Exception("Unable to generate Public and Private key pair!");

    // create RDNAttributes
    RDNAttributes rdnAttr = new RDNAttributes(
                              "countryName",
                              "on",
                              "oun",
                              "cn");

    // create RSACertRequest
    RSACertRequest certReq = new RSACertRequest(rdnAttr, keyPair);
    
    
    // create RSASelfSignedCert
    Calendar c = Calendar.getInstance();
    c.setTimeInMillis((new Date()).getTime() + 1000*10000);
    
    
    RSASelfSignedCert cert = new RSASelfSignedCert(
                               rdnAttr, keyPair, certReq,
                               new Date(),
                               c.getTime());
    
    X509Certificate x509Cert = cert.getCertificate();
    X509Extensions extensionsFromCertFile = GridCertUtilities.getX509ExtensionsFromCert(x509Cert);
    Enumeration oids = extensionsFromCertFile.oids();
    while(oids.hasMoreElements())
    {
      Object oid = oids.nextElement();
      System.out.println("OIDS: "+oid+ " "+(extensionsFromCertFile.getExtension((DERObjectIdentifier)oid)).getValue());
    }
    
    System.out.println("Generated Cert:"+x509Cert);
    
    byte[] encoded = x509Cert.getEncoded();
    byte[] buffer = new byte[512];
    int i = 0;
    ByteArrayInputStream in = new ByteArrayInputStream(encoded);
    FileOutputStream out = new FileOutputStream(new File("c:/myCert.cer"));
    
    while( (i = in.read(buffer)) > -1)
    {
      out.write(buffer, 0, i);
    }
    
    out.close();
  }
}
