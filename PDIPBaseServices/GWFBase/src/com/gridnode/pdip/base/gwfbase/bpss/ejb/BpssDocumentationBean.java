/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * June 27 2002	 MAHESH              Created
 */

package com.gridnode.pdip.base.gwfbase.bpss.ejb;

import com.gridnode.pdip.base.gwfbase.bpss.model.BpssDocumentation;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;
 
public class BpssDocumentationBean extends AbstractEntityBean {

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -4193236154542483949L;

	protected boolean optimizeEjbStore()
  {
    return true;
  }

	//Abstract methods of AbstractEntityBean
	public String getEntityName(){
		return BpssDocumentation.ENTITY_NAME;
	}
}