// %1023788043043:com.gridnode.pdip.base.entity%
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
 * Feb 09 2004    Koh Han Sing        Add TaskId. 
 */



/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms. Copyright 2001-2002 (C) GridNode Pte Ltd. All
 * Rights Reserved. File: PartnerEntityHandler.java Date           Author
 * Changes Jun 20 2002    Mathew Yap          Created
 */
package com.gridnode.pdip.base.time.entities.model;

import com.gridnode.pdip.framework.db.entity.IEntity;

public interface IiCalAlarm
  extends IEntity
{
  public static final String  ENTITY_NAME = "iCalAlarm";
  	
  public static final Number UID = new Integer(0);  //Integer
  public static final Number START_DURATION = new Integer(1);  // Long
  public static final Number START_DT = new Integer(2);  // Date
  public static final Number RELATED = new Integer(3);  // Integer
  public static final Number DELAY_PERIOD = new Integer(4);  // Long
  public static final Number REPEAT = new Integer(5);  // Integer
  public static final Number CATEGORY = new Integer(6);  // String
  public static final Number SENDER_KEY = new Integer(7);  // String
  public static final Number RECEIVER_KEY = new Integer(8);  // String
  public static final Number DISABLED = new Integer(9);  // Boolean
  public static final Number NEXT_DUE_TIME = new Integer(10);  // Date
  public static final Number COUNT = new Integer(11);  // Integer
  
  //newly added to integrate with iCalComponent  
  public static final Number PARENT_UID=new Integer(12); //Long
  public static final Number PARENT_KIND=new Integer(13); //Short
  public static final Number RECUR_LIST_STR = new Integer(14); //String
  public static final Number IS_RECUR_COMPLETE = new Integer(15); //Boolean
  public static final Number CUR_RECUR=new Integer(16); //STRING
  public static final Number IS_PSEUDO_PARENT= new Integer(17); //Boolean
  
  public static final Number TASK_ID= new Integer(18); //String
  public static final Number INCLUDE_PARENT_START_TIME = new Integer(19); //Boolean
}