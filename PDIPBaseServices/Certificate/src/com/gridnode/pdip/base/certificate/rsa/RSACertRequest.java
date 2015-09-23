/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RSACertRequest.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 13 2002    Neo Sok Lay         Created
 * June 29 2009   Tam Wei Xiang       #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib
 */
package com.gridnode.pdip.base.certificate.rsa;

import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.pkcs.Attribute;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.X509Extensions;
import org.bouncycastle.asn1.x509.X509ExtensionsGenerator;
import org.bouncycastle.jce.PKCS10CertificationRequest;
import org.bouncycastle.jce.X509KeyUsage;

/**
 * This class generates and provides a wrapper for a PKCS 10 Certification
 * Request object.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I5
 * @since 2.0 I5
 */
public class RSACertRequest
{
  private RDNAttributes _rdnAttributes;
  private RSAKeyPair    _keyPair;
  private PKCS10CertificationRequest _certRequest;

  /**
   * Construct a RSACertRequest object. This would set the necessary attributes
   * in the PKCS10CertRequest object and sign it.
   *
   * @param rdnAttributes RDNAttributes object providing the Subject Name for
   * the certificate request.
   * @param keyPair RSAKeyPair object providing the Public and Private Key pair.
   * @throws CertificateException Error in generating the certificate request.
   * @throws NoSuchAlgorithmException Error signing the certificate request.
   */
  public RSACertRequest(
    RDNAttributes rdnAttributes, RSAKeyPair keyPair)
    throws InvalidKeyException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException
  {
    _rdnAttributes = rdnAttributes;
    _keyPair       = keyPair;
    populate();
    //sign();
  }

  /**
   * Populate the necessary parameters for the certificate request.
   *
   * @throws InvalidKeyException Error setting parameters to the certificate
   * request.
   */
  protected void populate()
    throws InvalidKeyException, NoSuchProviderException, NoSuchAlgorithmException, SignatureException
  {
    // add an X509 V3 extension. PKCS 10 does not allow extensions, so the
    // extension has to be "wrapped" in an attribute. Any number of extensions
    // or attributes can be added. In this case, only a single extension/attribute
    // is being added. Request attributes or extensions are NOT required for the
    // creation of a PKCS10 certification request.
    X509ExtensionsGenerator x509ExGen = new X509ExtensionsGenerator();
    x509ExGen.addExtension(X509Extensions.KeyUsage, true, createKeyUsageExtension());
    X509Extensions extensions = x509ExGen.generate();
    Attribute attribute = new Attribute(PKCSObjectIdentifiers.pkcs_9_at_extensionRequest,
                                        new DERSet(extensions));

    /* Generate the digital signature on the certificate request using the
    * SHA-1 algorithm for generating the message digest of the request contents.
    */
    _certRequest = new PKCS10CertificationRequest("SHA1withRSA", _rdnAttributes.getSubjectNameJava(),
                                                    _keyPair.getPublicKey(), new DERSet(attribute),
                                                    _keyPair.getPrivateKey(), GridCertUtilities.getSecurityProvider());
  }

  /**
   * Create a KeyUsage extension object.
   *
   * @return A KeyUsage object that indicates that the key can be used for
   * encrypting and signing.
   */
  private X509KeyUsage createKeyUsageExtension()
  {
    // the KeyUsage extension determines what can be done with a given key.
    // In this case, the key can be used for encrypting and signing.
    return new X509KeyUsage(X509KeyUsage.dataEncipherment | 
                               X509KeyUsage.digitalSignature);
  }
  
  /**
   * Get the generated Certificate Request object.
   *
   * @return The signed PKCS10CertRequest object.
   */
  public PKCS10CertificationRequest getCertRequest()
  {
    return _certRequest;
  }
}