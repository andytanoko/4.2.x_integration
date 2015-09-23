// %1023788043168:com.gridnode.pdip.base.entity%
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
package com.gridnode.pdip.base.time.entities.model;

import com.gridnode.pdip.framework.db.entity.IEntity;

public interface IiCalComponent
  extends IEntity
{
  public static final Number UID = new Integer(0);  //Integer
  public static final Number KIND = new Integer(1);  // Short
  public static final Number OWNER_ID = new Integer(2);  // Integer
  public static final Number CLASSIFICATION = new Integer(3);  // String
  public static final Number CREATE_DT = new Integer(4);  // Date
  public static final Number LAST_MODIFY_DT = new Integer(5);  // Date
  public static final Number IS_DATE_TYPE = new Integer(6);  // Boolean
  public static final Number IS_DT_FLOAT = new Integer(7);  // Boolean
  public static final Number START_DT = new Integer(8);  // Date
  public static final Number PRIORITY = new Integer(9);  // Integer
  public static final Number SEQUENCE_NUM = new Integer(10);  // Integer
  public static final Number STATUS = new Integer(11);  // Integer
  public static final Number iCal_UID = new Integer(12);  // String
  public static final Number END_DT = new Integer(13);  // Date
  public static final Number DURATION = new Integer(14);  // Long
  public static final Number PROPERTIES = new Integer(15);  // List
  public static final Number PROPERTIES_STR = new Integer(16);  // List
	
  
//  public static final Number IS_PSEUDO= new Integer(16); //Boolean
  
  
  public static final Short KIND_EVENT = new Short((short)1);
  public static final Short KIND_TODO = new Short((short)2);
}