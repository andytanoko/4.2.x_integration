/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RSASelfSignedCert.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 13 2002    Neo Sok Lay         Created
 * June 29 2009   Tam Wei Xiang       #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib
 */
package com.gridnode.pdip.base.certificate.rsa;

import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.X509Extension;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.x509.X509V3CertificateGenerator;

/**
 * This class creates and provides self-signed X.509 Certifcate.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class RSASelfSignedCert
{
  private RSAKeyPair _keyPair;
  private RSACertRequest _certRequest;
  private RDNAttributes  _rdnAttributes;
  private Date _notBefore;
  private Date _notAfter;

  private X509V3CertificateGenerator _v3CertGen;
  private X509Certificate _certificate;
  
  /**
   * Construct a RSASelfSignedCert object.
   * This will set the necessary parameters in the X509Certificate object and
   * sign it.
   *
   * @param rdnAttributes The RDNAttributes object providing the Subject Name
   * for the certificate.
   * @param keyPair The RSAKeyPair object providing the generated Public and
   * Private key pair.
   * @param certRequest The RSACertRequest object providing the generated
   * PKCS 10 certificate request.
   * @param notBefore The validity time of the certificate. This indicates that
   * the certificate is not valid before this time.
   * @param notAfter The validity time of the certificate. This indicates that
   * the certificate is not valid after this time.
   *
   * @throws NoSuchAlgorithmException Error signing the certificate.
   * @throws CertificateException Error generating the certificate request.
   */
  public RSASelfSignedCert(
    RDNAttributes rdnAttributes, RSAKeyPair keyPair,
    RSACertRequest certRequest, Date notBefore, Date notAfter)
    throws NoSuchAlgorithmException,NoSuchProviderException,
           InvalidKeyException, SignatureException, CertificateEncodingException
  {
    _keyPair = keyPair;
    _certRequest = certRequest;
    _rdnAttributes = rdnAttributes;
    _notBefore = notBefore;
    _notAfter = notAfter;
    _v3CertGen = new X509V3CertificateGenerator();
    
    populate();
    
    //BC sign it when generate the cert...
    //sign();
  }

  /**
   * Populate the certificate with necessary parameters like: version,
   * serial number, validity period, extensions, issuer name, subject name,
   * and public key.
   *
   * @throws NoSuchAlgorithmException Error creating random number generator.
   * @throws CertificateException Error setting parameters into the certificate.
   */
  protected void populate()
    throws NoSuchAlgorithmException,NoSuchProviderException,
           InvalidKeyException, SignatureException, CertificateEncodingException
  {
    // generate a random serial number.
    // a real CA might increment an existing value, but we will generate a
    // random serial number. This value should be different each time because
    // we are seeding with a different value each time.
    SecureRandom random = _keyPair.getRandomNumGen();
    byte[] serialNo = new byte[16];
    random.nextBytes(serialNo);
    BigInteger serialNoInt = (new BigInteger(serialNo)).abs();
    _v3CertGen.setSerialNumber(serialNoInt);

    // set the validity times
    _v3CertGen.setNotBefore(_notBefore);
    _v3CertGen.setNotAfter(_notAfter);

    // set the issuer name of the certificate
    // we use the SubjectName as the IssuerName
    _v3CertGen.setIssuerDN(_rdnAttributes.getSubjectNameJava());

    // set the subject name of the certificate
    _v3CertGen.setSubjectDN(_rdnAttributes.getSubjectNameJava());

    // set the public key
    _v3CertGen.setPublicKey(_keyPair.getPublicKey());
    
    //set sign algo
    _v3CertGen.setSignatureAlgorithm("SHA1WithRSA");

    // convert any attributes in the certificate request to extensions.
    // Only V3ExtensionAttribute type attributes will be converted.
    // handling of other attributes is not automatic, and needs to be handled
    // by the application.
    Hashtable<DERObjectIdentifier, X509Extension> extensionsMap = convertAttributesToExtensions(_certRequest.getCertRequest().getCertificationRequestInfo().getAttributes());

    if (extensionsMap != null && extensionsMap.size() > 0)
    {
      Enumeration<DERObjectIdentifier> oidEnum = extensionsMap.keys();
      while(oidEnum.hasMoreElements())
      {
        DERObjectIdentifier oid = oidEnum.nextElement();
        X509Extension extension = extensionsMap.get(oid);
        _v3CertGen.addExtension(oid, extension.isCritical(), extension.getValue().getOctets());
      }
    }
      
    _certificate = _v3CertGen.generate(_keyPair.getPrivateKey(), GridCertUtilities.getSecurityProvider());

  }

  /**
   * Convert any extensions masquerading as attributes in the certificate
   * request to extensions for the certificate. Dealing with other attributes
   * is defined by the policy of the Certification Authority. Dealing with such
   * attributes is not covered here.
   *
   * @param attributes the extension request attributes.
   *
   * @return the DERObjectIdentifier and its corresponding X509Extension in the hashtable
   */
  private Hashtable<DERObjectIdentifier, X509Extension> convertAttributesToExtensions(ASN1Set attributes)
  {
    Hashtable<DERObjectIdentifier, X509Extension> extensionMap = new Hashtable<DERObjectIdentifier, X509Extension>();
    
    if(attributes != null && attributes.size() > 0)
    {
      for(int i = 0; i < attributes.size(); i++)
      {
        Attribute attr = Attribute.getInstance(attributes.getObjectAt(i));
        
        //process extension request
        if(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest.equals(attr.getAttrType()))
        {
          X509Extensions extensions = X509Extensions.getInstance(attr.getAttrValues().getObjectAt(0));
          Enumeration e = extensions.oids();
          while(e.hasMoreElements())
          {
            DERObjectIdentifier oid = (DERObjectIdentifier)e.nextElement();
            X509Extension ext = extensions.getExtension(oid);
            extensionMap.put(oid, ext);
          }
        }
      }
    }
    
    return extensionMap;
  }  
  
  /**
   * Sign the certificate using the SHA-1 algorithm.
   * At this point, the signature of the certificate
   * is generated. If the date in the certificate is changed after this point,
   * the signature will have to be re-computed.
   *
   * @throws NoSuchAlgorithmException Unknown algorithm to use.
   * @throws CertificateException Error creating the digital signature.
   */
  /*
  protected void sign()
    throws NoSuchAlgorithmException, CertificateException
  {
    _certificate.signCertificate(
      "SHA1/RSA/PKCS1Block01Pad", "Java", _keyPair.getPrivateKey(),
      _keyPair.getRandomNumGen());
  }*/

  public X509Certificate getCertificate()
  {
    return _certificate;
  }
}