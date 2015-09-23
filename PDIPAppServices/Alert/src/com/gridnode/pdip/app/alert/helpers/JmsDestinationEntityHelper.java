/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsDestinationEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 03 2006    Neo Sok Lay        Created
 */
package com.gridnode.pdip.app.alert.helpers;

import com.gridnode.pdip.app.alert.model.JmsDestination;
import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;
import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * @author i00107
 * For checking conflict during import config
 */
public class JmsDestinationEntityHelper implements ICheckConflict
{
	private static JmsDestinationEntityHelper _self = null;
	private static final Object _lock = new Object();
	/**
	 * 
	 */
	private JmsDestinationEntityHelper()
	{
		super();
	}

	public static JmsDestinationEntityHelper getInstance()
	{
		if (_self == null)
		{
			synchronized (_lock)
			{
				if (_self == null)
				{
					_self = new JmsDestinationEntityHelper();
				}
			}
		}
		return _self;
	}
	
	/**
	 * @see com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict#checkDuplicate(com.gridnode.pdip.framework.db.entity.IEntity)
	 */
	public IEntity checkDuplicate(IEntity entity) throws Exception
	{
    AlertLogger.debugLog("JmsDestinationEntityHelper", "checkDuplicate", "Start");
    String jmsDestName = entity.getFieldValue(JmsDestination.NAME).toString();
    JmsDestinationEntityHandler handler = JmsDestinationEntityHandler.getInstance();
    return handler.findByJmsDestName(jmsDestName); 
	}

}
