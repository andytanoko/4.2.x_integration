/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IWebService.java
 *
 ****************************************************************************
 * Date						Author              Changes
 ****************************************************************************
 * Feb 6, 2004		Mahesh             	Created
 */
package com.gridnode.pdip.app.servicemgmt.model;

public interface IWebService
{
  /**
   * Name of the WebService entity.
   */
  public static final String ENTITY_NAME = "WebService";

  /**
   * FieldId for UID of a WebService entity instance. A Number.
   */
  public static final Number UID                  = new Integer(0);  //integer

  /**
   * FieldId for WsdlUrl of a WebService entity instance. A String.
   */
  public static final Number WSDL_URL             = new Integer(1); //string

  /**
   * FieldId for EndPoint of a WebService entity instance. A String.
   */
  public static final Number END_POINT            = new Integer(2); //string
  
  /**
   * FieldId for ServiceName of a WebService entity instance. A String.
   */
  public static final Number SERVICE_NAME         = new Integer(3); //string

  /**
   * FieldId for ServiceGroup of a WebService entity instance. A String.
   */
  public static final Number SERVICE_GROUP         = new Integer(4); //string

  /**
   * FieldId for CanDelete flag of a WebService instance. A Boolean.
   */
  public static final Number CAN_DELETE           = new Integer(5);  //Boolean

  /**
   * FieldId for Version of a WebService instance, A Double.
   */
  public static final Number VERSION              = new Integer(6);  //Double

  //Constants
  public static final String INTERNAL_GROUP="INTERNAL";
  public static final String EXTERNAL_GROUP="EXTERNAL";
}
