/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultCertificateEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2008-10-17     Wong Yee Wah         Created
 *
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.document.CreateAS2DocTypeMappingEvent;
import com.gridnode.gtas.events.document.DeleteAS2DocTypeMappingEvent;
import com.gridnode.gtas.events.document.GetAS2DocTypeMappingListEvent;
import com.gridnode.gtas.events.document.GetAS2DocTypeMappingEvent;
import com.gridnode.gtas.events.document.UpdateAS2DocTypeMappingEvent;
import com.gridnode.gtas.events.partnerprocess.FilterPartnerListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

public class DefaultAS2DocTypeMappingManager
extends DefaultAbstractManager
implements IGTAS2DocTypeMappingManager
{
  DefaultAS2DocTypeMappingManager(DefaultGTSession session)
  throws GTClientException
  {
    super(IGTManager.MANAGER_AS2_DOC_TYPE_MAPPING, session);
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      CreateAS2DocTypeMappingEvent event = new CreateAS2DocTypeMappingEvent(
                                        entity.getFieldString(IGTAS2DocTypeMappingEntity.AS2_DOC_TYPE),
                                        entity.getFieldString(IGTAS2DocTypeMappingEntity.DOC_TYPE),
                                        entity.getFieldString(IGTAS2DocTypeMappingEntity.PARTNER_ID) );
      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to create", e);
    }
  }
  
  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {

      UpdateAS2DocTypeMappingEvent event = new UpdateAS2DocTypeMappingEvent(
                                        (Long)entity.getFieldValue(IGTAS2DocTypeMappingEntity.UID),
                                        entity.getFieldString(IGTAS2DocTypeMappingEntity.AS2_DOC_TYPE),
                                        entity.getFieldString(IGTAS2DocTypeMappingEntity.DOC_TYPE),
                                        entity.getFieldString(IGTAS2DocTypeMappingEntity.PARTNER_ID));
      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }
  
  public IGTAS2DocTypeMappingEntity getAS2DocTypeMappingbyUID(long uid)
  throws GTClientException
  {
    return (IGTAS2DocTypeMappingEntity)getByUid(uid);
  }
  
  protected String getEntityType()
  {
    return IGTEntity.ENTITY_AS2_DOC_TYPE_MAPPING;
  }
  
  protected int getManagerType()
  {
    return IGTManager.MANAGER_AS2_DOC_TYPE_MAPPING;
  }
  
  public IGTAS2DocTypeMappingEntity getAS2DocTypeMappingByUID(long uid)
  throws GTClientException
  {
    return (IGTAS2DocTypeMappingEntity)getByUid(uid);
  }
  
  protected AbstractGTEntity createEntityObject(String entityType)
  throws GTClientException
  {
    return new DefaultAS2DocTypeMappingEntity();
  }
  
  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
  throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetAS2DocTypeMappingEvent(uid);
  }
  
  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
  throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetAS2DocTypeMappingListEvent(filter);
  }
  
  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
  throws EventException
  { 
    return new DeleteAS2DocTypeMappingEvent(uids);
  }
  
  public Collection getApplicablePartnerList()
  throws GTClientException
  { 
    try
    {
      DefaultAbstractManager ptrMgr = (DefaultAbstractManager)
                                      _session.getManager(IGTManager.MANAGER_PARTNER);
      
        return ptrMgr.getAll();
      
    }
    catch(Throwable t)
    { 
      throw new GTClientException("Error caught delegating to Partner"+ t);
    }
  }
  public Collection getApplicableDocumentTypeList()
  throws GTClientException
  {
    try
    {
      DefaultAbstractManager ptrMgr = (DefaultAbstractManager)
                                      _session.getManager(IGTManager.MANAGER_DOCUMENT_TYPE);
      
        return ptrMgr.getAll();
     
    }
    catch(Throwable t)
    { 
      throw new GTClientException("Error caught delegating to DocumentType"+ t);
    }
  }
}
