/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsMessageRecordBean.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 6, 2006    Tam Wei Xiang       Created
 * Feb 28 2006    Neo Sok Lay         Add Serial Version UID
 */
package com.gridnode.pdip.app.alert.entities.ejb;

import com.gridnode.pdip.app.alert.model.JmsMessageRecord;
import com.gridnode.pdip.framework.db.ejb.AbstractEntityBean;

/**
 *
 *
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public class JmsMessageRecordBean
	extends AbstractEntityBean
{
	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 5255619548126822030L;

	public String getEntityName()
	{
		return JmsMessageRecord.ENTITY_NAME;
	}
}
