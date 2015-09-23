/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2002    Mahesh              Created
 *
 */
package com.gridnode.pdip.app.workflow.runtime.ejb;

import com.gridnode.pdip.app.workflow.runtime.model.*;
import com.gridnode.pdip.framework.db.ejb.*;


public class GWFRtProcessBean extends AbstractEntityBean {
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2632195151099051438L;

	protected boolean optimizeEjbStore()
  {
    return true;
  }

    //Abstract methods of AbstractEntityBean
    public String getEntityName(){
        return GWFRtProcess.ENTITY_NAME;
    }
}
