// %1023788048059:com.gridnode.pdip.base.time.value%
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: 
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 11 2002    Liu Xiao Hua	      Created
 */



/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File: PartnerEntityHandler.java Date           Author
 * Changes Jun 20 2002    Mathew Yap          Created
 */
package com.gridnode.pdip.base.time.entities.value;

public interface IByRule
{
  public static final int BY_SECOND = 0;
  public static final int BY_MINUTE = 1;
  public static final int BY_HOUR = 2;
  public static final int BY_DAY = 3;
  public static final int BY_MONTH_DAY = 4;
  public static final int BY_YEAR_DAY = 5;
  public static final int BY_WEEK_NO = 6;
  public static final int BY_MONTH = 7;
  public static final int BY_SET_POS = 8;
}