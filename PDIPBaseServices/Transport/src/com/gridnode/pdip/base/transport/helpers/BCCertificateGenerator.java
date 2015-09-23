/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: BCCertificateGenerator.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 31 March 2003  Qingsong            Initial creation for GTAS 2.0
 * 21 Sep 2005    Lim Soon Hsiung     Upgrade bouncy castle to 1.5 130,
 *   replace DERInputStream with ASN1InputStream and org.bouncycastle.jce.X509V1/3CertificateGenerator
 *   with org.bouncycastle.x509.X509V1/3CertificateGenerator
 */

package com.gridnode.pdip.base.transport.helpers;

import org.bouncycastle.asn1.ASN1InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.cert.X509Certificate;
import java.util.Date;

import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509Name;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.x509.X509V1CertificateGenerator;
import org.bouncycastle.x509.X509V3CertificateGenerator;

public class BCCertificateGenerator implements CertificateGenerator
{
  X509V1CertificateGenerator  v1CertGen = new X509V1CertificateGenerator();
  X509V3CertificateGenerator  v3CertGen = new X509V3CertificateGenerator();
  long sn = 1;
  private String csrsubject = "CN=GridNode, C=SG, O=GridNode Pte Ltd, OU=GridNode Pte Ltd Primary Certificate";
  private String subject = "CN=GridNode, C=SG, O=GridNode Pte Ltd, OU=GridNode Pte Ltd Primary Certificate";
  private String issuer = "CN=GridNode, C=SG, O=GridNode Pte Ltd, OU=GridNode Pte Ltd Primary Certificate";
  private java.util.Date from = new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 3 * 365);
  private java.util.Date to = new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 20 * 365));

  public void reset()
  {
    v1CertGen.reset();
    v3CertGen.reset();
    setSerialNo(1);
  }

  public void setSerialNo(long sn)
  {
    this.sn = sn;
  }

  public static SubjectKeyIdentifier createSubjectKeyId(PublicKey   pubKey)
  {
      try
      {
          ByteArrayInputStream    bIn = new ByteArrayInputStream(pubKey.getEncoded());
          SubjectPublicKeyInfo info = new SubjectPublicKeyInfo((ASN1Sequence)new ASN1InputStream(bIn).readObject());
          return new SubjectKeyIdentifier(info);
      }
      catch (Exception e)
      {
          throw new RuntimeException("error creating key");
      }
  }

  public X509Name createIssuerName()
  {
      return new X509Name(issuer);
  }

  public X509Name createSubjectName()
  {
      return new X509Name(subject);
  }

  public X509Name createCSRSubjectName()
  {
      return new X509Name(csrsubject);
  }

  public static AuthorityKeyIdentifier createAuthorityKeyId(PublicKey pubKey,X509Name name,int sNumber)
  {
      try
      {
          ByteArrayInputStream    bIn = new ByteArrayInputStream(pubKey.getEncoded());
          SubjectPublicKeyInfo info = new SubjectPublicKeyInfo((ASN1Sequence)new ASN1InputStream(bIn).readObject());
          GeneralName             genName = new GeneralName(name);
          ASN1EncodableVector     v = new ASN1EncodableVector();
          v.add(genName);
          return new AuthorityKeyIdentifier(info, new org.bouncycastle.asn1.x509.GeneralNames(new DERSequence(v)), BigInteger.valueOf(sNumber));
      }
      catch (Exception e)
      {
          throw new RuntimeException("error creating AuthorityKeyId");
      }
  }

    public static AuthorityKeyIdentifier createAuthorityKeyId(PublicKey pubKey)
    {
        try
        {
            ByteArrayInputStream    bIn = new ByteArrayInputStream(pubKey.getEncoded());
            SubjectPublicKeyInfo info = new SubjectPublicKeyInfo((ASN1Sequence)new ASN1InputStream(bIn).readObject());
            return new AuthorityKeyIdentifier(info);
        }
        catch (Exception e)
        {
            throw new RuntimeException("error creating AuthorityKeyId");
        }
    }

  public BCCertificateGenerator()
  {
    reset();
  }

  public BCCertificateGenerator(long sn, Date from, Date to)
  {
    this();
    setSerialNo(sn);
    setValidateDate(from, to);
  }

  public void setValidateDate(Date from, Date to)
  {
    setFrom(from);
    setTo(to);
  }

  public X509Certificate generateCertificate(KeyPair signerkeypair, byte[] csr) throws InvalidKeyException, SignatureException, IOException, NoSuchProviderException, NoSuchAlgorithmException
  {
      ByteArrayInputStream    bIn = new ByteArrayInputStream(csr);
      ASN1InputStream          dIn = new ASN1InputStream(bIn);
      PKCS10CertificationRequest req = new PKCS10CertificationRequest((ASN1Sequence)dIn.readObject());
      PublicKey pubkey = req.getPublicKey();
      setSubjectName(req.getCertificationRequestInfo().getSubject().toString());
      return generateCertificate(signerkeypair,pubkey);
  }

  public X509Certificate generateCertificate(KeyPair signerkeypair, PublicKey pubkey) throws InvalidKeyException, SignatureException
  {
      v3CertGen.setSerialNumber(BigInteger.valueOf(sn));

      v3CertGen.setIssuerDN(createIssuerName());
      v3CertGen.setNotBefore(from);
      v3CertGen.setNotAfter(to);
      v3CertGen.setSubjectDN(createSubjectName());
      v3CertGen.setPublicKey(pubkey);
      v3CertGen.setSignatureAlgorithm("MD5WithRSAEncryption");
      v3CertGen.addExtension(
			X509Extensions.SubjectKeyIdentifier,
			false,
			createSubjectKeyId(pubkey));

		v3CertGen.addExtension(
			X509Extensions.AuthorityKeyIdentifier,
			false,
			createAuthorityKeyId(signerkeypair.getPublic()));
      return v3CertGen.generateX509Certificate(signerkeypair.getPrivate());
  }

  public X509Certificate generateCertificate(KeyPair keypair) throws InvalidKeyException, SignatureException
  {
      v1CertGen.setNotBefore(from);
      v1CertGen.setNotAfter(to);
      v1CertGen.setIssuerDN(createIssuerName());
      v1CertGen.setSubjectDN(createIssuerName());
      v1CertGen.setSerialNumber(BigInteger.valueOf(sn));
      v1CertGen.setPublicKey(keypair.getPublic());
      v1CertGen.setSignatureAlgorithm("MD5WithRSAEncryption");
      return v1CertGen.generateX509Certificate(keypair.getPrivate());
  }

  public byte[] generateCSR(KeyPair keypair) throws InvalidKeyException, SignatureException, NoSuchProviderException, NoSuchAlgorithmException, IOException
  {
      PKCS10CertificationRequest req1 = new PKCS10CertificationRequest(
                                                  "SHA1withRSA",
                                                  createCSRSubjectName(),
                                                  keypair.getPublic(),
                                                  null,
                                                  keypair.getPrivate());
      ByteArrayOutputStream bOut = new ByteArrayOutputStream();
      DEROutputStream dOut = new DEROutputStream(bOut);
      dOut.writeObject(req1);
      dOut.close();
      return bOut.toByteArray();
  }
  /*
  public static void main(String[] args)
  {
    BCCertificateGenerator BCCertificateGenerator1 = new BCCertificateGenerator();
  }*/
  public void setIssuerName(String issuer)
  {
    this.issuer = issuer;
  }
  public void setSubjectName(String subject)
  {
    this.subject = subject;
  }
  public void setCSRSubjectName(String csrsubject)
  {
    this.csrsubject = csrsubject;
  }
  public void setFrom(java.util.Date from)
  {
    this.from = from;
  }
  public java.util.Date getFrom()
  {
    return from;
  }
  public void setTo(java.util.Date to)
  {
    this.to = to;
  }
  public java.util.Date getTo()
  {
    return to;
  }
}