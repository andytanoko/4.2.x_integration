/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ILogErrorCodes.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 12 2007		Chong SoonFui				Created
 */
package com.gridnode.pdip.base.security.exceptions;

/**
 * Error codes for PDIP-Base-Security Module 
 * @author Chong SoonFui
 * @since GT 4.0 VAN
 * @version GT 4.0 VAN
 */
public interface ILogErrorCodes
{
	public static final String PREFIX = "002.014.";
	
	/**
	 * 002.014.001: Error in Security Service Bean while staring CertDb service
	 */
	public static final String SECURITY_SVC_BEAN_CERTDB_STARTUP = PREFIX+"001";
	
	/**
	 * 002.014.002: Error in SMIME Security Facade while staring CertDb service
	 */
	public static final String SMIME_SECURITY_FACADE_CERTDB_STARTUP = PREFIX+"002";
	
  /**
   * 002.014.003: Error in verification CMS
   */
  public static final String CMS_VERIFICATION_FAILED = PREFIX+"003";
  
  /**
   * 002.014.004: Error in loading public key
   */
  public static final String PUBLIC_KEY_LOAD = PREFIX+"004";
  
  /**
   * 002.014.005: Error in matching the public key and private key
   */
  public static final String KEY_PAIR_MATCHING = PREFIX+"005";
  
  
  
}
