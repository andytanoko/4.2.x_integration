/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2004 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IiCalAlarm.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 09 2004    Koh Han Sing        Created
 */
package com.gridnode.gtas.model.scheduler;

public interface IiCalAlarm
{
  public static final String ENTITY_NAME = "iCalAlarm";

  public static final String USER_PROCEDURE = "UserProcedure";
  public static final String CHECK_LICENSE = "CheckLicense";
  public static final String HOUSEKEEPING_INFO = "HousekeepingInfo";

  public static final Integer UID = new Integer(0);  //Integer
  public static final Integer DELAY_PERIOD = new Integer(4);  // Long
  public static final Integer CATEGORY = new Integer(6);  // String
  public static final Integer DISABLED = new Integer(9);  // Boolean
  public static final Integer COUNT = new Integer(11);  // Integer

  public static final Integer TASK_ID= new Integer(18); //String

}
