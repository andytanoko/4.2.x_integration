/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTWebServiceEntity.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Feb 9, 2004      Mahesh              Created
 */

package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.servicemgmt.IWebService;
 
public interface IGTWebServiceEntity extends IGTEntity
{
  //Fields
  public static final Number UID                = IWebService.UID;         // Integer(?)
  public static final Number CAN_DELETE         = IWebService.CAN_DELETE;  // Boolean
  public static final Number SERVICE_NAME       = IWebService.SERVICE_NAME;// String(30)
  public static final Number SEVICE_GROUP       = IWebService.SERVICE_GROUP;// String(30)
  public static final Number WSDL_URL           = IWebService.WSDL_URL;    // String(255)
  public static final Number END_POINT          = IWebService.END_POINT;   //  String(255)
  
  //Constants
  public static final String INTERNAL_GROUP="INTERNAL";
  public static final String EXTERNAL_GROUP="EXTERNAL";
  
}
