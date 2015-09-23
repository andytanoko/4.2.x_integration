/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CertVerifier.java
 *
 * ****************************************************************************
 * Date           Author              Changes
 * ****************************************************************************
 * 27 Sep 2001    Lim Soon Hsiung     Initial creation for GM 1.2
 * 07 Jun 2002    Jagadeesh           Changed to throw SecurityServiceException
 *                                    and Log for GridTalk-2.0.
 * 01 July 2009      Tam Wei Xiang    #560: Migrate from RSA J-SAFE/B-SAFE to BouncyCastle Lib
 */
package com.gridnode.pdip.base.security.helpers;

import java.security.PublicKey;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

import com.gridnode.pdip.base.security.exceptions.SecurityServiceException;

/**
 * This class is used to verify the validity of a X.509 certificate.
 * @since 1.2
 * @author Lim Soon Hsiung
 * @version 1.2
 *
 */
public class CertVerifier
{

  public static final int ERR_NO_ERROR          = 0;
  public static final int ERR_WRONG_ISSUER_NAME = 1;
  public static final int ERR_SIGNATURE_ERROR   = 2;

  private int errorCode = -1;

  private X509Certificate _subjectCert, _issuerCert;

  /**
   * Default constructor
   * @since 1.2
   *
   */
  public CertVerifier()
  {
  }

  /**
   * Construct a class with the subject certificate being passed in.  If no issuer
   * certificate is being set later, then this cert is treated as a self-sign cert.
   * @param subjectCertData subject cert binary data (BER X.509 Certificate)
   * @since 1.2
   *
   */
  public CertVerifier(byte[] subjectCertData) throws SecurityServiceException
  {
    this(getCert(subjectCertData), getCert(subjectCertData));
  }

  /**
   * Construct a class with the subject certificate being passed in.  If no issuer
   * certificate is being set later, then this cert is treated as a self-sign cert.
   * @param subjectCert the subject certificate
   * @since 1.2
   *
   */
  public CertVerifier(X509Certificate subjectCert)
  {
    this(subjectCert, subjectCert);
  }

  /**
   * Construct a class with the subject certificate and the issuer certificate being
   * passed in.
   * @param subjectCertData the subject cert binary data (BER X.509 Certificate)
   * @param issuerCertData the issuer cert binary data (BER X.509 Certificate)
   * @since 1.2
   *
   */
  public CertVerifier(byte[] subjectCertData, byte[] issuerCertData)
    throws SecurityServiceException
  {
    this(getCert(subjectCertData), getCert(issuerCertData));
  }

  /**
   * Construct a class with the subject certificate and the issuer certificate being
   * passed in.
   * @param subjectCert the subject certificate
   * @param issuerCert the issuer certificate
   * @since 1.2
   *
   */
  public CertVerifier(X509Certificate subjectCert, X509Certificate issuerCert)
//    throws GNException
  {
    this._subjectCert = subjectCert;
    this._issuerCert  = issuerCert;
  }

  /**
   * Verify whether the certificate is signed by teh issuer (using the issuer certificate)
   * @param issuerCert the issuer certificate
   * @return an int indicating whether the signature on the certificate is valid.
   * @since 1.2
   *
   */
  public int isIssuedBy(X509Certificate issuerCert) throws SecurityServiceException
  {
    setIssuerCert(issuerCert);
    return isIssuedBy();
  }

  /**
   * Verify whether the certificate is signed by teh issuer (using the issuer certificate)
   * @param issuerCertData the issuer certificate binary data (BER X.509 certificate)
   * @return an int indicating whether the signature on the certificate is valid.
   * @since 1.2
   *
   */
  public int isIssuedBy(byte[] issuerCertData) throws SecurityServiceException
  {
    setIssuerCert(issuerCertData);
    return isIssuedBy();
  }

  /**
   * Verify whether the certificate is signed by teh issuer (using the issuer certificate)
   * @return an int indicating whether the signature on the certificate is valid.
   * @since 1.2
   *
   */
  public int isIssuedBy() throws SecurityServiceException
  {
    if(_subjectCert == null)
      throw new SecurityServiceException("Subject certificate is not set !!!");
    if(_issuerCert == null)
      throw new SecurityException("Issuer certificate is not set !!!");

    if(!verifyName())
      return getErrorCode();

    if(!verifySignature())
      return getErrorCode();

    return ERR_NO_ERROR;
  }

  /**
   * Verify that the issuer X.500 Name is correct/valid.
   * @return a boolean indication the result, true is valid, false otherwise
   * @since 1.2
   *
   */
  private boolean verifyName()
//    throws GNException
  {
    X500Principal issuerPrincipal = _subjectCert.getIssuerX500Principal();
    X500Principal subjectPrincipal = _issuerCert.getSubjectX500Principal();
    boolean isMatched = false;
    
    if(issuerPrincipal != null && subjectPrincipal != null)
    {
      if(issuerPrincipal.getName() != null && subjectPrincipal.getName() != null)
      {
         isMatched = issuerPrincipal.equals(subjectPrincipal);
      }
    }
    
    
    if(!isMatched)
      errorCode = ERR_WRONG_ISSUER_NAME;

    return isMatched;
  }

  /**
   * Verify that the signature is correct.
   * @return a boolean indication the result.
   * @since 1.2
   *
   */
  private boolean verifySignature() throws SecurityServiceException
  {
    PublicKey verifyingKey = null;
    try
    {
      verifyingKey = _issuerCert.getPublicKey();
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to retrieve public key from certificate");
    }
    try
    {
      _subjectCert.verify(verifyingKey);
      return true;
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Unable to perform the signature algorithm", ex);
    }
  }

  /**
   * Generate the X.509 certificate from the passed in BER encoded data
   * @param certData the BER encoded certificate data
   * @return a X.509 certificate
   * @since 1.2
   *
   */
  private static X509Certificate getCert(byte[] certData) throws SecurityServiceException
  {
    X509Certificate certificate = null;
    try
    {
      certificate = GridCertUtilities.loadX509Certificate(certData);
    }
    catch (Exception ex)
    {
      throw new SecurityServiceException("Data passed in is not a valid X.509 certificate");
    }

    return certificate;
  }

  /**
   * Set the subject certificate.
   * @param subjectCertData the subject cert binary data (BER X.509 Certificate)
   * @since 1.2
   *
   */
  public void setSubjectCert(byte[] subjectCertData) throws SecurityServiceException
  {
    setSubjectCert(getCert(subjectCertData));
  }

  /**
   * Set the subject certificate.
   * @param subjectCert the subject certificate
   * @since 1.2
   *
   */
  public void setSubjectCert(X509Certificate subjectCert)
  {
    this._subjectCert = subjectCert;
  }

  /**
   * Set the issuer certificate
   * @param issuerCertData the issuer cert binary data (BER X.509 Certificate)
   * @since 1.2
   *
   */
  public void setIssuerCert(byte[] issuerCertData) throws SecurityServiceException
  {
    setIssuerCert(getCert(issuerCertData));
  }

  /**
   * Set the issuer certificate
   * @param issuerCert the issuer certificate
   * @since 1.2
   *
   */
  public void setIssuerCert(X509Certificate issuerCert)
  {
    this._issuerCert = issuerCert;
  }

  /**
   * return the error code in the previous action.
   * @return the error code. ERR_NO_ERROR, ERR_WRONG_ISSUER_NAME and ERR_SIGNATURE_ERROR
   * @since 1.2
   *
   */
  private int getErrorCode()
  {
    return errorCode;
  }


// ****************** Testing Code ********************************************
  /**
   *
   */
  public static void main(String[] args)
  {
/*    String subjectFN = null, issuerFN = null;
    X509Certificate subjectCert = null;
    X509Certificate issuerCert = null;
    CertVerifier verifier = null;

    if (args.length == 0)
    {
      System.out.println("Usage: \"self sign cert filename\" ");
      System.out.println("Usage: \"subject cert filename\" \"issuer cert filename\" ");
      return;
    }
    else if (args.length == 1)
    {
      subjectFN = args[0];
      issuerFN  = args[0];
      subjectCert = GridCertUtilities.loadX509Certificate(subjectFN);
      verifier = new CertVerifier(subjectCert);
    }
    else
    {
      subjectFN = args[0];
      issuerFN  = args[1];
      subjectCert = GridCertUtilities.loadX509Certificate(subjectFN);
      issuerCert = GridCertUtilities.loadX509Certificate(issuerFN);
      verifier = new CertVerifier(subjectCert, issuerCert);
    }

    try
    {
      System.out.println("Verify cert ...");
      int code = verifier.isIssuedBy();

      String not = (code == verifier.ERR_NO_ERROR)? "" : " NOT";
      System.out.println("Certificate " + subjectFN + " is" +  not +
       " issued by " + issuerFN);

      if(code != verifier.ERR_NO_ERROR)
      {
        switch (code)
        {
          case ERR_WRONG_ISSUER_NAME:
            System.out.println("Issuer Name is wrong ");
            break;
          case ERR_SIGNATURE_ERROR:
            System.out.println("Certificate signature is wrong ");
            break;
        }

      }
    }
    catch (Exception ex)
    {
      System.out.println("Exception occurs: ");
      ex.printStackTrace();
    }
*/
  }

}