/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: WorkflowActivityBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 03 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.partnerfunction.entities.ejb;

import com.gridnode.gtas.server.partnerfunction.model.WorkflowActivity;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/**
 * A WorkflowActivityBean provides persistency services for WorkflowActivity.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public class WorkflowActivityBean extends AbstractEntityBean
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -9027820051188416998L;

	public String getEntityName()
  {
    return WorkflowActivity.ENTITY_NAME;
  }
}