/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAlertList.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Nov 26 2002    Srinath	             Created
 * Feb 06 2003    Neo Sok Lay             Change USERID to USER_UID.
 *                                        Change FROMID to FROM_UID.
 * Mar 03 2003    Neo Sok Lay             Add Version and CanDelete fields.
 */

package com.gridnode.pdip.app.alert.model;

/**
 * This Interface defines the properties and FieldIds for accessing fields
 * in AlertList entity.
 *
 * @author Srinath
 *
 */

public interface IAlertList
{
  /**
   * Name for AlertList entity.
   */
  public static final String ENTITY_NAME = "AlertList";

  /**
   * FieldId for uid.
   */
  public static final Number UID = new Integer(0); // long

  /**
   * FieldId for User Id.
   */
  public static final Number USER_UID = new Integer(1); // long

  /**
   * FieldId for From Id.
   */
  public static final Number FROM_UID = new Integer(2); // long

  /**
   * FieldId for Title.
   */
  public static final Number TITLE = new Integer(3); // string 35

  /**
   * FieldId for Message.
   */
  public static final Number MESSAGE = new Integer(4); // long text

  /**
   * FieldId for Read Status.
   */
  public static final Number READSTATUS = new Integer(5); // tiny int 1

  /**
   * FieldId for Date.
   */
  public static final Number DATE = new Integer(6); // Date

  /**
   * FieldId for Version. A Double.
   */
  public static final Number VERSION = new Integer(4); //double

  /**
   * FieldId for CanDelete. A Boolean.
   */
  public static final Number CAN_DELETE = new Integer(5); //boolean


}