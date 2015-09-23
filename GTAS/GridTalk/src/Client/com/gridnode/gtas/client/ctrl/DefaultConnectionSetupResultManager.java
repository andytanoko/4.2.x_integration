/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultConnectionSetupResultManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-11-01    Andrew Hill          Created
 * 2002-11-14    Andrew Hill          Add support for SECURITY_PASSWORD vfield
 * 2003-04-04    Neo Sok Lay          handleEvent() instead of handleUpdateEvent()
 *                                    for EndConnectionSetupEvent.
 * 2003-07-18     Andrew Hill         Support for multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.NotApplicableException;
import com.gridnode.gtas.events.connection.EndConnectionSetupEvent;
import com.gridnode.gtas.events.connection.GetConnectionSetupResultEvent;
import com.gridnode.gtas.events.connection.SetupConnectionEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

class DefaultConnectionSetupResultManager extends DefaultAbstractManager
  implements IGTConnectionSetupResultManager
{

  DefaultConnectionSetupResultManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_CONNECTION_SETUP_RESULT, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTConnectionSetupResultEntity result = (IGTConnectionSetupResultEntity)entity;
      IGTConnectionSetupParamEntity param = (IGTConnectionSetupParamEntity)
                                            entity.getFieldValue(IGTConnectionSetupResultEntity.SETUP_PARAMETERS);
      Short status = (Short)result.getFieldValue(IGTConnectionSetupResultEntity.STATUS);
      IEvent event = null;
      if(status == null)
      {
        throw new java.lang.NullPointerException("null STATUS value");
      }
      else if( IGTConnectionSetupResultEntity.STATUS_NOT_DONE.equals(status) || IGTConnectionSetupResultEntity.STATUS_FAILURE.equals(status) )
      {
        // If not done then we are submitting the first screen (location & jms IP)
        String currentLocation = param.getFieldString(IGTConnectionSetupParamEntity.CURRENT_LOCATION);
        String servicingRouter = param.getFieldString(IGTConnectionSetupParamEntity.SERVICING_ROUTER);
        String securityPassword = param.getFieldString(IGTConnectionSetupParamEntity.SECURITY_PASSWORD);
        event = new SetupConnectionEvent(currentLocation, servicingRouter, securityPassword);
        handleUpdateEvent(event, (AbstractGTEntity)entity);
      }
      else if( IGTConnectionSetupResultEntity.STATUS_SUCCESS.equals(status) )
      {
        // If status is successful we are submitting details about the gridmaster etc.. order
        Collection availableGridMasters = (Collection)result.getFieldValue(IGTConnectionSetupResultEntity.AVAILABLE_GRIDMASTERS);
        Collection availableRouters = (Collection)result.getFieldValue(IGTConnectionSetupResultEntity.AVAILABLE_ROUTERS);
        event = new EndConnectionSetupEvent(availableGridMasters, availableRouters);
        handleEvent(event);
      }
      else
      {
        throw new java.lang.IllegalStateException("Illegal STATUS value:" + status);
      }
      //handleUpdateEvent(event, (AbstractGTEntity)entity);
      //hackedHandleUpdateEvent(event, (AbstractGTEntity)entity);
      ((AbstractGTEntity)param).setNewFieldValue(IGTConnectionSetupParamEntity.SECURITY_PASSWORD, null);
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error updating connection setup",t);
    }
  }


  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new NotApplicableException("Create is not applicable to ConnectionSetupResult");
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_CONNECTION_SETUP_RESULT;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_CONNECTION_SETUP_RESULT;
  }

  public IGTConnectionSetupResultEntity getConnectionSetupResult() throws GTClientException
  {
    try
    {
      return (IGTConnectionSetupResultEntity)handleGetEvent(getGetEvent(null));
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error retrieving ConnectionSetupResult",t);
    }
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    if(uid != null)
    {
      throw new java.lang.IllegalArgumentException("ConnectionSetupResult may not be referenced by uid");
    }
    return new GetConnectionSetupResultEvent();
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    throw new NotApplicableException("ConnectionSetupResult does not have associated list events");
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    throw new java.lang.UnsupportedOperationException("ConnectionSetupResult entity may not be deleted");
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_CONNECTION_SETUP_RESULT.equals(entityType))
    {
      return new DefaultConnectionSetupResultEntity();
    }
    else if(IGTEntity.ENTITY_CONNECTION_SETUP_PARAM.equals(entityType))
    {
      return new DefaultConnectionSetupParamEntity();
    }
    else
    {
      throw new GTClientException("Manager " + this
        + " cannot create entity objects for entity type " + entityType);
    }
  }

  protected IGTFieldMetaInfo[] defineVirtualFields(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_CONNECTION_SETUP_PARAM.equals(entityType))
    {
      VirtualSharedFMI[] sharedVfmi = new VirtualSharedFMI[1];
      sharedVfmi[0] = new VirtualSharedFMI( "connectionSetupParam.securityPassword",
                                            IGTConnectionSetupParamEntity.SECURITY_PASSWORD);
      sharedVfmi[0].setMandatoryCreate(true);
      sharedVfmi[0].setMandatoryUpdate(true);
      return sharedVfmi;
    }
    return new IGTFieldMetaInfo[0];
  }

  /*protected final void hackedHandleUpdateEvent(IEvent event, AbstractGTEntity entity)
    throws GTClientException
  {
    try
    {
      IGTConnectionSetupResultEntity result = (IGTConnectionSetupResultEntity)entity;
      Short status = (Short)result.getFieldValue(result.STATUS);
      if( result.STATUS_SUCCESS.equals(status) )
      {
        //throw new GTClientException("test");
      }

      event = getGetEvent(null);

      Map fieldMap = (Map)handleEvent(event);
      if (fieldMap == null)
      { // Sanity check on what we were returned
        throw new java.lang.NullPointerException("Returned fieldMap is null for update event " + event);
      }

      Map gm1 = new HashMap();
      gm1.put(IGridNode.UID, new Long(1) );
      gm1.put(IGridNode.ID, "123");
      gm1.put(IGridNode.NAME, "TestGM1");
      gm1.put(IGridNode.STATE, new Short(IGridNode.STATE_GM) );

      Map gm2 = new HashMap();
      gm2.put(IGridNode.UID, new Long(2) );
      gm2.put(IGridNode.ID, "223");
      gm2.put(IGridNode.NAME, "TestGM2");
      gm2.put(IGridNode.STATE, new Short(IGridNode.STATE_GM) );

      Map gm3 = new HashMap();
      gm3.put(IGridNode.UID, new Long(3) );
      gm3.put(IGridNode.ID, "323");
      gm3.put(IGridNode.NAME, "TestGM3");
      gm3.put(IGridNode.STATE, new Short(IGridNode.STATE_GM) );

      Map gm4 = new HashMap();
      gm4.put(IGridNode.UID, new Long(4) );
      gm4.put(IGridNode.ID, "423");
      gm4.put(IGridNode.NAME, "TestGM4");
      gm4.put(IGridNode.STATE, new Short(IGridNode.STATE_GM) );

      Collection fakeGms = new Vector(4);
      fakeGms.add(gm1);
      fakeGms.add(gm2);
      fakeGms.add(gm3);
      fakeGms.add(gm4);

      fieldMap.put(IConnectionSetupResult.AVAILABLE_GRIDMASTERS,fakeGms);

      Map router1 = new HashMap();
      router1.put(IJmsRouter.UID, new Long(1));
      router1.put(IJmsRouter.NAME, "TestRouter1");
      router1.put(IJmsRouter.IP_ADDRESS, "0.0.0.1");

      Map router2 = new HashMap();
      router2.put(IJmsRouter.UID, new Long(2));
      router2.put(IJmsRouter.NAME, "TestRouter2");
      router2.put(IJmsRouter.IP_ADDRESS, "0.0.0.2");

      Map router3 = new HashMap();
      router3.put(IJmsRouter.UID, new Long(3));
      router3.put(IJmsRouter.NAME, "TestRouter3");
      router3.put(IJmsRouter.IP_ADDRESS, "0.0.0.3");

      Map router4 = new HashMap();
      router4.put(IJmsRouter.UID, new Long(4));
      router4.put(IJmsRouter.NAME, "TestRouter4");
      router4.put(IJmsRouter.IP_ADDRESS, "0.0.0.4");

      Vector fakeRouters = new Vector(4);
      fakeRouters.add(router1);
      fakeRouters.add(router2);
      fakeRouters.add(router3);
      fakeRouters.add(router4);

      fieldMap.put(IConnectionSetupResult.AVAILABLE_ROUTERS,fakeRouters);

      fieldMap.put(IConnectionSetupResult.STATUS, new Short(IConnectionSetupResult.STATUS_SUCCESS));

      // Reload entities fields from the returned fieldMap
      initEntityFields(getEntityType(), entity, fieldMap);
    }
    catch(Throwable ex)
    {
      throw new GTClientException("Error handling update event", ex);
    }
  }*/
}