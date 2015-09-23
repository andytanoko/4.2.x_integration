// %1023949932936:com.gridnode.pdip.base.gwfbase.gwfentity%
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
 * Jun 10 2002   Mahesh	      Created
 * Jun 13 2002   Mathew         Repackaged
 */



/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File:  Date           Author              Changes Jun 10
 * 2002   Mahesh          Created Jun 13 2002   Mathew       Repackaged
 */
/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File:  Date           Author              Changes Jun 11
 * 2002    Liu Xiao Hua          Created
 */
/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. Date           Author              Changes May 23 2002
 * Mahesh              Created
 */
package com.gridnode.pdip.base.gwfbase.baseentity;

public interface IGWFActivity  //extends IEntity{
{
  public static int AUTOMATIC_TYPE = 0;
  public static int MANUAL_TYPE = 1;
  public static int SUB_PROCESS_TYPE = 2;

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME!
   */
  public int getActivityType();

  /**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME!
   */
  public String getActivityName();
}