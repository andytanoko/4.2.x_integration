// %1023949930483:com.gridnode.pdip.base.gwfbase.bpss.ejb%
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

package com.gridnode.pdip.base.gwfbase.bpss.ejb;

import com.gridnode.pdip.base.gwfbase.bpss.model.*;
import com.gridnode.pdip.framework.db.ejb.*;

public class BpssJoinBean
  extends AbstractEntityBean
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -7107245389091131447L;

	protected boolean optimizeEjbStore()
  {
    return true;
  }

  //Abstract methods of AbstractEntityBean
  public String getEntityName()
  {
    return BpssJoin.ENTITY_NAME;
  }
}