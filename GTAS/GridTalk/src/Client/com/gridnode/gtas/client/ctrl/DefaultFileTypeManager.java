package com.gridnode.gtas.client.ctrl;
/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultFileTypeManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-06     Andrew Hill         Created
 * 2002-10-09     Andrew Hill         "partnerCat" mods
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 */

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.document.CreateFileTypeEvent;
import com.gridnode.gtas.events.document.DeleteFileTypeEvent;
import com.gridnode.gtas.events.document.GetFileTypeEvent;
import com.gridnode.gtas.events.document.GetFileTypeListEvent;
import com.gridnode.gtas.events.document.UpdateFileTypeEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultFileTypeManager extends DefaultAbstractManager
  implements IGTFileTypeManager
{
  DefaultFileTypeManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_FILE_TYPE, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTFileTypeEntity fileType = (IGTFileTypeEntity)entity;
      UpdateFileTypeEvent event = new UpdateFileTypeEvent(fileType.getUidLong(),
                                                          fileType.getFieldString(IGTFileTypeEntity.DESCRIPTION),
                                                          fileType.getFieldString(IGTFileTypeEntity.PROGRAM_NAME),
                                                          fileType.getFieldString(IGTFileTypeEntity.PROGRAM_PATH),
                                                          fileType.getFieldString(IGTFileTypeEntity.PARAMETERS),
                                                          fileType.getFieldString(IGTFileTypeEntity.WORKING_DIR) );
      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTFileTypeEntity fileType = (IGTFileTypeEntity)entity;
      CreateFileTypeEvent event = new CreateFileTypeEvent(fileType.getFieldString(IGTFileTypeEntity.FILE_TYPE),
                                                          fileType.getFieldString(IGTFileTypeEntity.DESCRIPTION),
                                                          fileType.getFieldString(IGTFileTypeEntity.PROGRAM_NAME),
                                                          fileType.getFieldString(IGTFileTypeEntity.PROGRAM_PATH),
                                                          fileType.getFieldString(IGTFileTypeEntity.PARAMETERS),
                                                          fileType.getFieldString(IGTFileTypeEntity.WORKING_DIR) );
      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to create", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_FILE_TYPE;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_FILE_TYPE;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetFileTypeEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetFileTypeListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeleteFileTypeEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_FILE_TYPE.equals(entityType))
    {
      return new DefaultFileTypeEntity();
    }
    throw new java.lang.IllegalArgumentException("Manager:" + this + " cannot create entities of type " + entityType);
  }

}