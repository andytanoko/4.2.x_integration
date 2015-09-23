// %1023788044418:com.gridnode.pdip.base.time.ejb%
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

import com.gridnode.pdip.base.time.entities.model.iCalDate;

public class iCalDateBean
  extends iCalValueBean
{ 
  //Abstract methods of AbstractEntityBean

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 8696863562060520153L;

	/**
   * DOCUMENT ME!
   * 
   * @return DOCUMENT ME! 
   */
  public String getEntityName()
  {
    return iCalDate.class.getName();
  }
}
