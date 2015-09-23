/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2006 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultSchemaMappingFileManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 14, 2006    Tam Wei Xiang       Created
 */
package com.gridnode.gtas.client.ctrl;



import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

/**
 * @author Tam Wei Xiang
 * 
 * @since GT 4.0
 */
public class DefaultSchemaMappingFileManager 
	extends DefaultAbstractManager	
	implements IGTSchemaMappingFileManager
{
	DefaultSchemaMappingFileManager(DefaultGTSession session)
		throws GTClientException
	{
		super(IGTManager.MANAGER_SCHEMA_MAPPING_FILE, session);
	}

	@Override
	protected IEvent getGetEvent(Long uid) throws EventException
	{
		throw new UnsupportedOperationException("[DefaultSchemaMappingFileManager] getGetEvent is not supported.");
	}

	@Override
	protected IEvent getGetListEvent(IDataFilter filter) throws EventException
	{
		throw new UnsupportedOperationException("[DefaultSchemaMappingFileManager] getGetListEvent is not supported.");
	}

	@Override
	protected IEvent getDeleteEvent(Collection uids) throws EventException
	{
		throw new UnsupportedOperationException("[DefaultSchemaMappingFileManager] getDeleteEvent is not supported.");
	}

	@Override
	protected AbstractGTEntity createEntityObject(String entityType) throws GTClientException
	{
		return new DefaultSchemaMappingFile();
	}

	@Override
	protected void doUpdate(IGTEntity entity) throws GTClientException
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected void doCreate(IGTEntity entity) throws GTClientException
	{
		// TODO Auto-generated method stub

	}

	@Override
	protected int getManagerType()
	{
		return IGTManager.MANAGER_SCHEMA_MAPPING_FILE;
	}

	@Override
	protected String getEntityType()
	{
		return IGTEntity.ENTITY_SCHEMA_MAPPING_FILE;
	}

}
