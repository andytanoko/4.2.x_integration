/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGWFArchiveProcess.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 21, 2007    Tam Wei Xiang       Created
 */
package com.gridnode.pdip.app.workflow.runtime.model;

import java.util.Date;

import com.gridnode.pdip.app.workflow.runtime.model.GWFArchiveProcess;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT VAN (GT 4.0.3)
 */
public interface IGWFArchiveProcess
{
  /**
   * Name for DocummentMEtaInfo entity.
   */
  public static final String ENTITY_NAME = GWFArchiveProcess.class.getSimpleName();
  
  /**
   * The following is the fieldID for the respective field. 
   * Their type is stated after the declaration of the Number object.
   */
  public static final Number UID = new Integer(0);  //Long
  
  public static final Number ENGINE_TYPE = new Integer(1); //String
  
  public static final Number PROCESS_TYPE = new Integer(2); //String
  
  public static final Number PROCESS_START_TIME = new Integer(3); //Date
  
  public static final Number PROCESS_END_TIME = new Integer(4); //Date
  
  public static final Number PROCESS_UID = new Integer(5); //Long
  
  public static final Number RT_PROCESS_DOC_UID = new Integer(6); //Long
  
  public static final Number CUSTOMER_BE_ID = new Integer(7); //String
  
  public static final Number PARTNER_KEY = new Integer(8); //String
  
  public static final Number PROCESS_STATUS = new Integer(9); //Integer
}
