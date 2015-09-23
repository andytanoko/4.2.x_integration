/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultHouseKeepingManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2004-01-14     Daniel D'Cotta      Created
 * 2006-06-26     Tam Wei Xiang       Modified method getHouseKeeping(),
 *                                    saveHouseKeeping(...) to add in new field
 *                                    'WF_RECORDS_DAYS_TO_KEEP'
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.housekeeping.GetHousekeepingInfoEvent;
import com.gridnode.gtas.events.housekeeping.SaveHousekeepingInfoEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

class DefaultHouseKeepingManager extends DefaultAbstractManager
  implements IGTHouseKeepingManager
{
  //private static final String RESTORE_DOCUMENT_PATH_KEY = IPathConfig.PATH_TEMP; //@todo - set correctly  

  DefaultHouseKeepingManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_HOUSE_KEEPING, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new UnsupportedOperationException("Update HouseKeeping not supported");
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    throw new UnsupportedOperationException("Create HouseKeeping not supported");
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_HOUSE_KEEPING;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_HOUSE_KEEPING;
  }

  protected IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    throw new UnsupportedOperationException("No get event for this type");
  }

  protected IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    throw new UnsupportedOperationException("No get list event for this type");
  }

  protected IEvent getDeleteEvent(Collection uids)
    throws EventException
  { 
    throw new UnsupportedOperationException("No delete event for this type");
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_HOUSE_KEEPING.equals(entityType))
    {
      return new DefaultHouseKeepingEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }

  public Collection getAll() throws GTClientException
  {
    throw new UnsupportedOperationException("Manager does not support multiple entities");
  }
  
  /**
   * To simplify development we are actually making a call to the getHousekeeping() method here.
   * The UI has been written such that all visits to the page for editing the housekeeping are actually
   * treated as though they were 'creates' and hence will go through this version of newInstance to
   * obtain the IGTEntity instance. Note that housekeeping can be considered as a singleton entity
   * in that there is only one housekeeping object. Multiple calls to newInstance will however return
   * different value object instances of course.
   * @return The housekeeping entity
   */
  public IGTEntity newEntity() throws GTClientException
  {
    return getHouseKeeping();
  }
  
  public IGTHouseKeepingEntity getHouseKeeping() throws GTClientException
  {
    try
    {
      GetHousekeepingInfoEvent event = new GetHousekeepingInfoEvent();
      
      Map results = (Map)((DefaultGTSession)_session).fireEvent(event);     
      
      if(results == null)
      { // Internal sanity check
        throw new NullPointerException("GetHousekeepingInfoEvent event returned null result map");
      }

      IGTHouseKeepingEntity houseKeeping = (IGTHouseKeepingEntity)initEntityObjects(IGTEntity.ENTITY_HOUSE_KEEPING);
      houseKeeping.setFieldValue(IGTHouseKeepingEntity.TEMP_FILES_DAYS_TO_KEEP,       results.get(IGTHouseKeepingEntity.TEMP_FILES_DAYS_TO_KEEP));
      houseKeeping.setFieldValue(IGTHouseKeepingEntity.LOG_FILES_DAYS_TO_KEEP,        results.get(IGTHouseKeepingEntity.LOG_FILES_DAYS_TO_KEEP));
      houseKeeping.setFieldValue(IGTHouseKeepingEntity.WF_RECORDS_DAYS_TO_KEEP, results.get(IGTHouseKeepingEntity.WF_RECORDS_DAYS_TO_KEEP)); //TWX 26062006
      return houseKeeping;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error getting HouseKeeping entity", t);
    }
  }

  public void saveHouseKeeping(IGTHouseKeepingEntity houseKeeping)
    throws GTClientException
  {
    try
    {
      Map houseKeepingMap = new HashMap();
      houseKeepingMap.put(IGTHouseKeepingEntity.TEMP_FILES_DAYS_TO_KEEP,        houseKeeping.getFieldValue(IGTHouseKeepingEntity.TEMP_FILES_DAYS_TO_KEEP));
      houseKeepingMap.put(IGTHouseKeepingEntity.LOG_FILES_DAYS_TO_KEEP,         houseKeeping.getFieldValue(IGTHouseKeepingEntity.LOG_FILES_DAYS_TO_KEEP));
      houseKeepingMap.put(IGTHouseKeepingEntity.WF_RECORDS_DAYS_TO_KEEP, houseKeeping.getFieldValue(IGTHouseKeepingEntity.WF_RECORDS_DAYS_TO_KEEP)); //TWX 26062006
      
      SaveHousekeepingInfoEvent event = new SaveHousekeepingInfoEvent(houseKeepingMap);
      
      //Map results = (Map)((DefaultGTSession)_session).fireEvent(event);     
      ((DefaultGTSession)_session).fireEvent(event);     
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error saving HouseKeeping entity", t);
    }
  }
}