/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * June  27 2002	MAHESH              Created
 */
package com.gridnode.pdip.base.contextdata.entities.ejb;

import com.gridnode.pdip.base.contextdata.entities.model.ContextData;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
 
public class ContextDataBean extends AbstractEntityBean {

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 375978349481511149L;

	protected boolean optimizeEjbStore()
  {
    return true;
  }
  
	//Abstract methods of AbstractEntityBean
	public String getEntityName(){
		return ContextData.ENTITY_NAME;
	}
}