/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ServiceAssignmentBean.java
 *
 ****************************************************************************
 * Date      			Author              Changes
 ****************************************************************************
 * Feb 6, 2004   	Mahesh             	Created
 */
package com.gridnode.pdip.app.servicemgmt.entities.ejb;

import com.gridnode.pdip.app.servicemgmt.model.ServiceAssignment;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

public class ServiceAssignmentBean extends AbstractEntityBean
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 2388998878194431241L;

	public String getEntityName()
  {
    return ServiceAssignment.ENTITY_NAME;
  }
}
