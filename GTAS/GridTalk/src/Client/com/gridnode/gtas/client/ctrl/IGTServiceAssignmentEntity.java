/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTServiceAssignmentEntity.java
 *
 ****************************************************************************
 * Date             Author              Changes
 ****************************************************************************
 * Feb 9, 2004      Mahesh              Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.servicemgmt.IServiceAssignment;

public interface IGTServiceAssignmentEntity extends IGTEntity
{
  //Fields
  public static final Number UID                = IServiceAssignment.UID;            // Integer(?)
  public static final Number CAN_DELETE         = IServiceAssignment.CAN_DELETE;     // Boolean
  public static final Number USER_NAME          = IServiceAssignment.USER_NAME;      // String(30)
  public static final Number PASSWORD           = IServiceAssignment.PASSWORD;       // String(30)
  public static final Number USER_TYPE          = IServiceAssignment.USER_TYPE;      // String(30)
  public static final Number WEBSERVICE_UIDS    = IServiceAssignment.WEBSERVICE_UIDS;// Set
  
  //constants
  public static final String PARTNER_TYPE= "PARTNER";
}
