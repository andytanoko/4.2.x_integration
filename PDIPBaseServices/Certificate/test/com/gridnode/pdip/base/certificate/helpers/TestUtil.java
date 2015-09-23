/**
 * This software is the proprietary information of CrimsonLogic eTrade Services Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2006-2009 (C) CrimsonLogic eTrade Services Pte Ltd. All Rights Reserved.
 *
 * File: TestUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 9, 2009   Tam Wei Xiang       Created
 */
package com.gridnode.pdip.base.certificate.helpers;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.X509CRL;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Random;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.CRLNumber;
import org.bouncycastle.asn1.x509.CRLReason;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.x509.X509V1CertificateGenerator;
import org.bouncycastle.x509.X509V2CRLGenerator;
import org.bouncycastle.x509.X509V3CertificateGenerator;
import org.bouncycastle.x509.extension.AuthorityKeyIdentifierStructure;
import org.bouncycastle.x509.extension.SubjectKeyIdentifierStructure;

/**
 * Reference sample from Begin Cryptography with Java
 * @author Tam Wei Xiang
 * @version GT4.2.1
 * @since GT4.2.1
 */
public class TestUtil
{
  private static class FixedRand extends SecureRandom
  {
      MessageDigest sha;
      byte[]      state;
      
      FixedRand()
      {
          try
          {
              this.sha = MessageDigest.getInstance("SHA-1");
              this.state = sha.digest();
          }
          catch (NoSuchAlgorithmException e)
          {
              throw new RuntimeException("can't find SHA-1!");
          }
      }

    public void nextBytes(
       byte[] bytes)
    {
        int off = 0;
        
        sha.update(state);
        
        while (off < bytes.length)
        {             
            state = sha.digest();
            
            if (bytes.length - off > state.length)
            {
                System.arraycopy(state, 0, bytes, off, state.length);
            }
            else
            {
                System.arraycopy(state, 0, bytes, off, bytes.length - off);
            }
            
            off += state.length;
            
            sha.update(state);
        }
    }
  }
  
  /**
   * Return a SecureRandom which produces the same value.
   * <b>This is for testing only!</b>
   * @return a fixed random
   */
  public static SecureRandom createFixedRandom()
  {
      return new FixedRand();
  }
  
  public static KeyPair generateRSAKeyPair()
    throws Exception
  {
    KeyPairGenerator  kpGen = KeyPairGenerator.getInstance("RSA", "BC");

    kpGen.initialize(1024, new SecureRandom());

    return kpGen.generateKeyPair();
  }

  private static final int VALIDITY_PERIOD = 365 * 24 * 60 * 60 * 1000; // one week
  
  /**
   * Generate a sample V1 certificate to use as a CA root certificate
   */
  public static X509Certificate generateRootCert(KeyPair pair)
      throws Exception
{
    X509V1CertificateGenerator  certGen = new X509V1CertificateGenerator();

    certGen.setSerialNumber(BigInteger.valueOf( Math.abs((new Random()).nextInt()))); //modify to generate random number
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
  public static X509Certificate generateIntermediateCert(PublicKey intKey, PrivateKey caKey, X509Certificate caCert)
      throws Exception
  {
      X509V3CertificateGenerator  certGen = new X509V3CertificateGenerator();

      certGen.setSerialNumber(BigInteger.valueOf( Math.abs((new Random()).nextInt()))); //modify to generate random number
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
  public static X509Certificate generateEndEntityCert(PublicKey entityKey, PrivateKey caKey, X509Certificate caCert)
    throws Exception
  {
      X509V3CertificateGenerator  certGen = new X509V3CertificateGenerator();

      certGen.setSerialNumber(BigInteger.valueOf( Math.abs((new Random()).nextInt())));
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
 
  public static X509CRL createCRL(
                                  X509Certificate caCert,
                                  PrivateKey      caKey,
                                  BigInteger      revokedSerialNumber)
                                  throws Exception
                               {
                                  X509V2CRLGenerator  crlGen = new X509V2CRLGenerator();
                                  Date                now = new Date();

                                  crlGen.setIssuerDN(caCert.getSubjectX500Principal());

                                  crlGen.setThisUpdate(now);
                                  crlGen.setNextUpdate(new Date(now.getTime() + 100000));
                                  crlGen.setSignatureAlgorithm("SHA256WithRSAEncryption");

                                  crlGen.addCRLEntry(revokedSerialNumber, now, CRLReason.privilegeWithdrawn);

                                  crlGen.addExtension(X509Extensions.AuthorityKeyIdentifier,
                                                          false, new AuthorityKeyIdentifierStructure(caCert));
                                  crlGen.addExtension(X509Extensions.CRLNumber,
                                                              false, new CRLNumber(BigInteger.valueOf(1)));

                                  return crlGen.generateX509CRL(caKey, "BC");
                               }
  
  public static void main(String[] args) throws Exception
  {
    KeyPair rootKey = TestUtil.generateRSAKeyPair();
    X509Certificate rootCert = TestUtil.generateRootCert(rootKey);
    
    KeyPair intermediatePair = TestUtil.generateRSAKeyPair();
    X509Certificate intermediateCert = TestUtil.generateIntermediateCert(intermediatePair.getPublic(), intermediatePair.getPrivate(), rootCert);
    
    KeyPair endPair = TestUtil.generateRSAKeyPair();
    X509Certificate endCert = TestUtil.generateEndEntityCert(endPair.getPublic(), endPair.getPrivate(), intermediateCert);
    
    GridCertUtilities.writeX509Certificate("rootCert.cer", rootCert);
    GridCertUtilities.writeX509Certificate("intermediate.cer", intermediateCert);
    GridCertUtilities.writeX509Certificate("endCert.cer", endCert);
  }

  
}
