/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CertificateEntityHandler
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 09 2007			Alain Ah Ming				Created                                                                                     
 */
package com.gridnode.pdip.base.certificate.exceptions;

/**
 * Constants for error codes in PDIP Base-Certificate module
 * @author Alain Ah Ming
 * @since GT 4.0 VAN
 * @version GT 4.0 VAN
 */
public interface ILogErrorCodes
{
	/**
	 * Error code prefix for all PDIP Base-Certificate errors
	 */
	public static final String PREFIX = "002.003.";
	
	/**
	 * 002.003.001: Error in generating RSA key pair
	 */
	public static final String RSA_KEY_PAIR_GENERATE = PREFIX+"001";
	
	/**
	 * 002.003.002
	 */	
	public static final String CERTTIFICATE_SWAPPING_INVOKE = PREFIX+"002";
  
  /**
   * 002.003.003 Error writing PKCS8 Private Key
   */ 
  public static final String PKCS8_PRIVATE_KEY = PREFIX+"003";
  
  /**
   * 002.003.004 Error loading PKCS8 Private key
   */ 
  public static final String PKCS_PRIVATE_KEY_LOAD = PREFIX+"004";
  
  /**
   * 002.003.005 Error writing X509 cert
   */ 
  public static final String X509_CERT_WRITE = PREFIX+"005";
  
  /**
   * 002.003.006 Error performing X509 CRL related action
   */ 
  public static final String X509CRL_INVOKE = PREFIX+"006";
  
  /**
   * 002.003.007 Error loading Public key
   */ 
  public static final String PUBLIC_KEY_LOAD = PREFIX+"007";
  
  /**
   * 002.003.008 Error matching the Key Pair
   */ 
  public static final String KEY_PAIR_MATCHING = PREFIX+"008";
}
