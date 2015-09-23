/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: JmsMessageRecordHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 6, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.pdip.app.alert.helpers;

import java.util.Collection;

import javax.ejb.CreateException;

import com.gridnode.pdip.app.alert.entities.ejb.IJmsMessageRecordLocalHome;
import com.gridnode.pdip.app.alert.entities.ejb.IJmsMessageRecordLocalObj;
import com.gridnode.pdip.app.alert.model.JmsMessageRecord;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.CreateEntityException;
import com.gridnode.pdip.framework.exceptions.DeleteEntityException;
import com.gridnode.pdip.framework.exceptions.FindEntityException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.UpdateEntityException;

/**
 * It handles the access to the method in local obj interface and home interface.
 *
 * @author Tam Wei Xiang
 * @since GT 4.0
 */
public class JmsMessageRecordEntityHandler
	extends LocalEntityHandler
{
	private JmsMessageRecordEntityHandler()
	{
		super(JmsMessageRecord.ENTITY_NAME);
	}
	
	public static synchronized JmsMessageRecordEntityHandler getInstance()
	{
		JmsMessageRecordEntityHandler handler = null;
		if(EntityHandlerFactory.hasEntityHandlerFor(JmsMessageRecord.ENTITY_NAME, true))
		{
			handler = (JmsMessageRecordEntityHandler)EntityHandlerFactory.getHandlerFor(JmsMessageRecord.ENTITY_NAME, true);
		}
		else
		{
			handler = new JmsMessageRecordEntityHandler();
			EntityHandlerFactory.putEntityHandler(JmsMessageRecord.ENTITY_NAME, true,
			                                      handler);
		}
		
		return handler;
	}
	
	public Long createJmsMessageRecord(JmsMessageRecord jmsMsgRecord)
		throws CreateEntityException, SystemException
	{
		Long pk = null;
		try
		{
			pk =  (Long)createEntity(jmsMsgRecord).getKey();
		}
		catch(CreateException ex)
		{
			throw new CreateEntityException("Unable to create jmsMessageRecord. ", ex);
		}
		catch(Throwable th)
		{
			throw new SystemException("Unexpected error has occured.", th);
		}
		return pk;
	}
	
	public void updateJmsMessageRecord(JmsMessageRecord jmsMsgRecord)
		throws UpdateEntityException
	{
		try
		{
			super.update(jmsMsgRecord);
		}
		catch(Throwable th)
		{
			throw new UpdateEntityException("Unable to update jmsMessageRecord with UID "+jmsMsgRecord.getKey(), th);
		}
	}
	
	public void deleteJmsMessageRecord(Long UID)
		throws DeleteEntityException
	{
		try
		{
			super.remove(UID);
		}
		catch(Throwable th)
		{
			throw new DeleteEntityException("Unable to delete jmsMessageRecord with UID "+UID, th);
		}
	}
	
	public JmsMessageRecord findByUID(Long UID)
		throws FindEntityException
	{
		JmsMessageRecord jmsMsgRecord = null;
		try
		{
			jmsMsgRecord = (JmsMessageRecord)super.findByPrimaryKey(UID);
		}
		catch(Throwable th)
		{
			throw new FindEntityException("Unable to find jmsMessageRecord with UID "+UID, th);
		}
		return jmsMsgRecord;
	}
	
	public Collection<JmsMessageRecord> findJmsMessageRecordByFilter(IDataFilter filter)
		throws FindEntityException
	{
		Collection<JmsMessageRecord> result = null;
		try
		{
			result = super.getEntityByFilter(filter);
		}
		catch(Throwable th)
		{
			throw new FindEntityException("Unable to find a collection of jmsMesageRecord given the filter "+ filter.getFilterExpr(), th);
		}
		return result;
	}
	
	
	/**
	 * Return the proxy interface class (LocalObj) of JmsMessageRecordBean
	 */
	protected Class getProxyInterfaceClass()
	{
		return IJmsMessageRecordLocalObj.class;
	}
	
	/**
	 * Return the home interface of JmsMessageRecordBean
	 */
	protected Class getHomeInterfaceClass()
	{
		return IJmsMessageRecordLocalHome.class;
	}
}
