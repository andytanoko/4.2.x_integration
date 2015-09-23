// %1023788043465:com.gridnode.pdip.base.entity%
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

public interface IiCalProperty
  extends IEntity
{
  public static final Number UID = new Integer(0);  //Integer
  public static final Number KIND = new Integer(1);  // Short
  public static final Number COMP_KIND = new Integer(2);  // Short
  public static final Number iCal_COMP_ID = new Integer(3);  // Long
}