/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2004 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IFrequency.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 09 2004    Koh Han Sing        Created
 */
package com.gridnode.gtas.model.scheduler;


public interface IScheduleFrequency
{
  public static final int ONCE = -1;
  public static final int MINUTELY = 2;
  public static final int HOURLY = 3;
  public static final int DAILY = 4;
  public static final int WEEKLY = 5;
  public static final int MONTHLY = 6;
}
