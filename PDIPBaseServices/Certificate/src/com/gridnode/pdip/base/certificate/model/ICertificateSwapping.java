/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ICertificate
 *
 ****************************************************************************
 * Date            Author              Changes
 ****************************************************************************
 * 11-Jun-2008    Yee Wah,Wong          #38   Created.
 *
 * 
 */

package com.gridnode.pdip.base.certificate.model;

public interface ICertificateSwapping
{

  /**
   * FieldId for UId. A Number.
   */
  public static final Number UID        = new Integer(0);  //integer
  
  /**
   * Name for UserAccount entity.
   */
  public static final String ENTITY_NAME    = "CertificateSwapping";
	
	  
  /**
   * FieldId for SwapDate. A DATE.
   *
  */
  public static final Number SWAP_DATE       = new Integer(1);  //DATE
  
  /**
   * FieldId for SwapDate. A String.
   *
  */
  public static final Number SWAP_TIME       = new Integer(2);  //String
  
  /**
   * FieldId for SwapDate. A Long.
   *
  */
  public static final Number ALARM_UID       = new Integer(3);  //Long
  
 
  
  
}
