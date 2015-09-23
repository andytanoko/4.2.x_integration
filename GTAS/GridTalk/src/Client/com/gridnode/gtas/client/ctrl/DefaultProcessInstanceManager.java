/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultProcessInstanceManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-13     Daniel D'Cotta      Created
 * 2003-04-10     Andrew Hill         Eliminate dependecy on StaticUtils, fixed error message text
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.rnif.CancelProcessInstanceEvent;
import com.gridnode.gtas.events.rnif.DeleteProcessInstanceEvent;
import com.gridnode.gtas.events.rnif.GetProcessInstanceEvent;
import com.gridnode.gtas.events.rnif.GetProcessInstanceListEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultProcessInstanceManager extends DefaultAbstractManager
  implements IGTProcessInstanceManager
{
  DefaultProcessInstanceManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_PROCESS_INSTANCE, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new java.lang.UnsupportedOperationException("ProcessInstance entity does not support editing");
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new java.lang.UnsupportedOperationException("ProcessInstance entity does not support direct creation via manager"); //20030410AH
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_PROCESS_INSTANCE;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_PROCESS_INSTANCE;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    // 20030116 DDJ: Second param not needed more according to Xiao Hua
    return new GetProcessInstanceEvent(uid, "");
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetProcessInstanceListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    throw new java.lang.UnsupportedOperationException("ProcessInstances may not be deleted using the standard event");
  }

  public void delete(Collection instUids, Boolean deleteGridDoc)
    throws GTClientException
  {
    if(instUids == null) throw new NullPointerException("instUids is null"); //20030410AH
    if(deleteGridDoc == null) throw new NullPointerException("deleteGridDoc is null"); //20030410AH
    try
    {
      DeleteProcessInstanceEvent event = new DeleteProcessInstanceEvent(instUids,
                                                                        deleteGridDoc);
      handleEvent(event);
    }
    catch(Throwable t)
    {
      //20030410AH - importing?????? - throw new GTClientException("Error importing documents",t);
      throw new GTClientException("Error deleting processInstances",t); //20030410AH
    }
  }

  public void abort(Collection instUids, String reason)
    throws GTClientException
  {
    if(instUids == null) throw new NullPointerException("instUids is null"); //20030410AH
    if(reason == null) throw new NullPointerException("reason is null"); //20030410AH
    try
    {
      CancelProcessInstanceEvent event = new CancelProcessInstanceEvent(instUids,
                                                                        reason);
      handleEvent(event);
    }
    catch(Throwable t)
    {
      //20030410AH - cutnpaste junkie! bad! - throw new GTClientException("Error importing documents",t);
      throw new GTClientException("Error aborting processInstances",t); //20030410AH
    }
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_PROCESS_INSTANCE.equals(entityType))
    {
      return new DefaultProcessInstanceEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }
}