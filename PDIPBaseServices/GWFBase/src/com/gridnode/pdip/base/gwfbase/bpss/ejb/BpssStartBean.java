// %1023949930639:com.gridnode.pdip.base.gwfbase.bpss.ejb%
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
 * Rights Reserved. Date           Author              Changes May 24 2002
 * Mahesh              Created
 */
package com.gridnode.pdip.base.gwfbase.bpss.ejb;

import com.gridnode.pdip.base.gwfbase.bpss.model.*;
import com.gridnode.pdip.framework.db.ejb.*;

public class BpssStartBean
  extends AbstractEntityBean
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5886659741672937954L;

	protected boolean optimizeEjbStore()
  {
    return true;
  }

  //Abstract methods of AbstractEntityBean
  public String getEntityName()
  {
    return BpssStart.ENTITY_NAME;
  }
}