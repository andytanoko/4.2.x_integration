/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultAttachmentManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-09     Andrew Hill         Created
 * 2003-07-18     Andrew Hill         Support for multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.document.GetAttachmentEvent;
import com.gridnode.gtas.events.document.GetAttachmentListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultAttachmentManager extends DefaultAbstractManager
  implements IGTAttachmentManager
{
  private static final Log _log = LogFactory.getLog(DefaultAttachmentManager.class); // 20031209 DDJ
  
  DefaultAttachmentManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_ATTACHMENT, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new java.lang.UnsupportedOperationException("Updating of attachments is not supported");
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new java.lang.UnsupportedOperationException("Direct creation of attachments is not supported");
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_ATTACHMENT;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_ATTACHMENT;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    _log.info("[getGetEvent] uid = " + uid);
    return new GetAttachmentEvent(uid);
//    throw new java.lang.UnsupportedOperationException("Retrieval of attachments by UID is not supported");
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    _log.info("[getGetListEvent] filter = " + filter);
    return new GetAttachmentListEvent(filter);
//    throw new java.lang.UnsupportedOperationException("Retrieval of attachment lists is not supported");
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    throw new UnsupportedOperationException("Deletion of attachments is not supported");
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_ATTACHMENT.equals(entityType))
    {
      return new DefaultAttachmentEntity();
    }
    else
    {
      throw new GTClientException("Manager " + this
        + " cannot create entity objects for entity type " + entityType);
    }
  }
}