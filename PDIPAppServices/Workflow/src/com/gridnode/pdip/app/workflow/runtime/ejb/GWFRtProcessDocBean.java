/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 9 9 2002	MAHESH              Created
 */
package com.gridnode.pdip.app.workflow.runtime.ejb;

import com.gridnode.pdip.app.workflow.runtime.model.GWFRtProcessDoc;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
 
public class GWFRtProcessDocBean extends AbstractEntityBean {

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4788658749389259598L;

	protected boolean optimizeEjbStore()
  {
    return true;
  }

	//Abstract methods of AbstractEntityBean
	public String getEntityName(){
		return GWFRtProcessDoc.ENTITY_NAME;
	}
}