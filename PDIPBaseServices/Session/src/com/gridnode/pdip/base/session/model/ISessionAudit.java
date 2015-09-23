/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISessionAudit.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 06 2002    OHL                 Created
 */
package com.gridnode.pdip.base.session.model;


/**
 * This interface defines the properties and FieldIds for accessing fields
 * in SessionAudit entity.
 *
 * @author Ooi Hui Linn
 *
 * @version 2.0
 * @since 2.0
 */

public interface ISessionAudit
{
  /**
   * Name for UserAccount entity.
   */
  public static final String ENTITY_NAME = "SessionAudit";

  /**
   * FieldId for UId. A Number.
   */
  public static final Number UID        = new Integer(0);  //integer

  /**
   * FieldId for Session Id. A String.
   */
  public static final Number SESSION_ID  = new Integer(1);  //string(30)

  /**
   * FieldId for User Name which this session belongs to. A String.
   */
  public static final Number SESSION_NAME    = new Integer(2);  //string(30)

  /**
   * FieldId for State of the Session. A short.
   */
  public static final Number STATE   = new Integer(3);  //smallint(6)

  /**
   * FieldId for Data stored in the session. This is serialised binary data. A byte[].
   */
  public static final Number SESSION_DATA      = new Integer(4);  //longblob

  /**
   * FieldId for Time when session is opened. A Date.
   */
  public static final Number OPEN_TIME      = new Integer(5);  //datetime

  /**
   * FieldId for Time the last activity was taken (This refers only to activity involving the session,
   * such as gettting or updating session. This time must be updated continously to prevent the session
   * from expiring. A Date.
   */
  public static final Number LAST_ACTIVE_TIME   = new Integer(6); //datetime

  /**
   * FieldId for Time this session is closed, expired. A Date.
   */
  public static final Number DESTROY_TIME = new Integer(7); //datetime

  /**
   * Definition of Session's State
   */
  public static final short STATE_OPENED        = 1;
//  public static final short STATE_AUTHENTICATED = 2;
  public static final short STATE_SUSPENDED     = 2;
  public static final short STATE_CLOSED        = 3;

}
