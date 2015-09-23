/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsDestinationBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 28, 2005    Tam Wei Xiang       Created
 * Feb 28 2006    Neo Sok Lay         Add Serial version UID
 */
package com.gridnode.pdip.app.alert.entities.ejb;

import com.gridnode.pdip.app.alert.model.JmsDestination;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/**
 * A JmsDestinationBean provide persistency service for JmsDestination
 *
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public class JmsDestinationBean
	extends AbstractEntityBean
{
	
	/**
	 * Serial VersionUID
	 */
	private static final long serialVersionUID = 5472882408698367855L;

	public String getEntityName()
	{
		return JmsDestination.ENTITY_NAME;
	}
}
