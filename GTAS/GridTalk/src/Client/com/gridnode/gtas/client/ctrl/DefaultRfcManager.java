/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultRfcManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-19     Andrew Hill         Created
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.backend.CreateRfcEvent;
import com.gridnode.gtas.events.backend.DeleteRfcEvent;
import com.gridnode.gtas.events.backend.GetRfcEvent;
import com.gridnode.gtas.events.backend.GetRfcListEvent;
import com.gridnode.gtas.events.backend.UpdateRfcEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

class DefaultRfcManager extends DefaultAbstractManager
  implements IGTRfcManager
{
  DefaultRfcManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_RFC, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTRfcEntity rfc = (IGTRfcEntity)entity;

      Long uid = rfc.getUidLong();
      String description = rfc.getFieldString(IGTRfcEntity.DESCRIPTION);
      String host = rfc.getFieldString(IGTRfcEntity.HOST);
      String connectionType = IGTRfcEntity.CONNECTION_TYPE_TCPIP; //for now. Later to change when more types
      Integer portNumber = (Integer)rfc.getFieldValue(IGTRfcEntity.PORT_NUMBER);
      Boolean useCommandFile = (Boolean)rfc.getFieldValue(IGTRfcEntity.USE_COMMAND_FILE);
      boolean ucf = useCommandFile.booleanValue();
      String commandFileDir =  ucf ? rfc.getFieldString(IGTRfcEntity.COMMAND_FILE_DIR) : null;
      String commandFile = ucf ? rfc.getFieldString(IGTRfcEntity.COMMAND_FILE) : null;
      String commandLine = ucf ? rfc.getFieldString(IGTRfcEntity.COMMAND_LINE) : null;

      IEvent event = new UpdateRfcEvent(uid,
                                        description,
                                        host,
                                        connectionType,
                                        portNumber,
                                        useCommandFile,
                                        commandFileDir,
                                        commandFile,
                                        commandLine);

      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update rfc", e);
    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTRfcEntity rfc = (IGTRfcEntity)entity;

      String name = rfc.getFieldString(IGTRfcEntity.NAME);
      String description = rfc.getFieldString(IGTRfcEntity.DESCRIPTION);
      String host = rfc.getFieldString(IGTRfcEntity.HOST);
      String connectionType = IGTRfcEntity.CONNECTION_TYPE_TCPIP; //for now. Later to change when more types
      Integer portNumber = (Integer)rfc.getFieldValue(IGTRfcEntity.PORT_NUMBER);
      Boolean useCommandFile = (Boolean)rfc.getFieldValue(IGTRfcEntity.USE_COMMAND_FILE);
      boolean ucf = useCommandFile.booleanValue();
      String commandFileDir =  ucf ? rfc.getFieldString(IGTRfcEntity.COMMAND_FILE_DIR) : null;
      String commandFile = ucf ? rfc.getFieldString(IGTRfcEntity.COMMAND_FILE) : null;
      String commandLine = ucf ? rfc.getFieldString(IGTRfcEntity.COMMAND_LINE) : null;

      IEvent event = new CreateRfcEvent(name,
                                        description,
                                        host,
                                        connectionType,
                                        portNumber,
                                        useCommandFile,
                                        commandFileDir,
                                        commandFile,
                                        commandLine);

      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update rfc", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_RFC;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_RFC;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetRfcEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetRfcListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeleteRfcEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_RFC.equals(entityType))
    {
      return new DefaultRfcEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }
}