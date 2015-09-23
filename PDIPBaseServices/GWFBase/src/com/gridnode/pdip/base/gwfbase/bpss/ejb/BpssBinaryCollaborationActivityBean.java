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

import com.gridnode.pdip.base.gwfbase.bpss.model.BpssBinaryCollaborationActivity;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

public class BpssBinaryCollaborationActivityBean
  extends AbstractEntityBean
{
  //Abstract methods of AbstractEntityBean

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3926096700953006963L;

	public String getEntityName()
  {
    return BpssBinaryCollaborationActivity.ENTITY_NAME;
  }
  
  protected boolean optimizeEjbStore()
  {
    return true;
  }
  
}