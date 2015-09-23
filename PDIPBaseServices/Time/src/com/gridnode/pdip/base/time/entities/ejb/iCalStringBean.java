// %1023788045215:com.gridnode.pdip.base.time.ejb%
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
package com.gridnode.pdip.base.time.entities.ejb;

import com.gridnode.pdip.base.time.entities.model.iCalString;

public class iCalStringBean
  extends iCalValueBean
{ 
  //Abstract methods of AbstractEntityBean

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4725800373210452537L;

	/**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public String getEntityName()
  {
    return iCalString.class.getName();
  }
}
