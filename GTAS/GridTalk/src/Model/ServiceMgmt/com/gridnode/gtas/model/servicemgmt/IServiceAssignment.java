/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IServiceAssignment.java
 *
 ****************************************************************************
 * Date						Author              Changes
 ****************************************************************************
 * Feb 6, 2004		Mahesh             	Created
 */
package com.gridnode.gtas.model.servicemgmt;

public interface IServiceAssignment
{
  /**
   * Name of the ServiceAssignment entity.
   */
  public static final String ENTITY_NAME = "ServiceAssignment";

  /**
   * FieldId for UID of a ServiceAssignment entity instance. A Number.
   */
  public static final Number UID                  = new Integer(0); //Integer
  
  /**
   * FieldId for UserName of a ServiceAssignment entity instance. A String.
   */
  public static final Number USER_NAME            = new Integer(1); //String 

  /**
   * FieldId for Password of a ServiceAssignment entity instance. A String.
   */
  public static final Number PASSWORD             = new Integer(2); //String

  /**
   * FieldId for UserType of a ServiceAssignment entity instance. A String.
   */
  public static final Number USER_TYPE            = new Integer(3); //String 

  /**
   * FieldId for WebServiceUIds of a ServiceAssignment entity instance. A HashSet.
   */
  public static final Number WEBSERVICE_UIDS      = new Integer(4); //HashSet of WebService UID's  

  /**
   * FieldId for CanDelete flag of a ServiceAssignment instance. A Boolean.
   */
  public static final Number CAN_DELETE         = new Integer(5);  //Boolean

  /**
   * FieldId for Version of a ServiceAssignment instance, A Double.
   */
  public static final Number VERSION            = new Integer(6);  //Double


  // Constants
  public static final String PARTNER_TYPE="PARTNER";
}
