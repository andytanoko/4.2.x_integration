/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultDocumentTypeManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-21     Andrew Hill         Created
 * 2002-10-09     Andrew Hill         "partnerCat" mods
 * 2003-07-18     Andrew Hill         Support for multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.document.CreateDocumentTypeEvent;
import com.gridnode.gtas.events.document.DeleteDocumentTypeEvent;
import com.gridnode.gtas.events.document.GetDocumentTypeEvent;
import com.gridnode.gtas.events.document.GetDocumentTypeListEvent;
import com.gridnode.gtas.events.document.UpdateDocumentTypeEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultDocumentTypeManager extends DefaultAbstractManager
  implements IGTDocumentTypeManager
{
  DefaultDocumentTypeManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_DOCUMENT_TYPE, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {

      UpdateDocumentTypeEvent event = new UpdateDocumentTypeEvent(
                                        (Long)entity.getFieldValue(IGTDocumentTypeEntity.UID),
                                        entity.getFieldString(IGTDocumentTypeEntity.DESCRIPTION));
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
      CreateDocumentTypeEvent event = new CreateDocumentTypeEvent(
                                        entity.getFieldString(IGTDocumentTypeEntity.DOC_TYPE),
                                        entity.getFieldString(IGTDocumentTypeEntity.DESCRIPTION) );
      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to create", e);
    }
  }

  public IGTDocumentTypeEntity getDocumentTypeByUID(long uid)
    throws GTClientException
  {
    return (IGTDocumentTypeEntity)getByUid(uid);
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_DOCUMENT_TYPE;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_DOCUMENT_TYPE;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetDocumentTypeEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetDocumentTypeListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeleteDocumentTypeEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    return new DefaultDocumentTypeEntity();
  }

}