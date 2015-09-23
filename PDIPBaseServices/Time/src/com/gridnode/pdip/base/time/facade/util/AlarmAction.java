// %1023788047652:com.gridnode.pdip.base.time.util%
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
package com.gridnode.pdip.base.time.facade.util;

public interface AlarmAction
{
  static final Integer ADD = new Integer(1);
  static final Integer CANCEL = new Integer(2);
  static final Integer UPDATE = new Integer(3);
  static final Integer INVOKE = new Integer(4);
  static final Integer MISSED = new Integer(5);
}