/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IISATConstant.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 23, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.audit.tracking.helpers;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public interface IISATConstant
{
  public static final String LOG_TYPE = "GN.AUDIT.ISAT";
  
  //The resource types
  public static final String CMM_RESOURCE_TYPE_PIP = "PIP Name";
  public static final String CMM_RESOURCE_TYPE_PARTNER_NAME = "Partner Name";
  public static final String CMM_RESOURCE_TYPE_CUSTOMER_NAME = "Customer Name";
  public static final String CMM_RESOURCE_TYPE_DOCUMENT_TYPE = "Doc Type";
  
  //Process Lock
  public static final String TYPE_LOCK = "lock";
  public static final String PROCESS_LOCK = "processlock";
}
