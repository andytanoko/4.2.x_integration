/**
 * This software is the proprietary information of GridNode Pte Ltd. Use is
 * subjected to license terms.
 * 
 * Copyright 2001-2008 (C) GridNode Pte Ltd. All Rights Reserved.
 * 
 * File: DefaultArchiveManager.java
 * 
 **************************************************************************** 
 * Date          Author             Changes
 **************************************************************************** 
 * 2008-12-17    Ong Eu Soon        Created
 * 2009-03-30    Tam Wei Xiang      #122 - Change ArchiveEvent to CreateArchiveEvent.
 *                                  Added isArchiveOnce, archiveOlderThan, clientTZ.
 * 2009-04-11    Tam Wei Xiang      #122 - Change to use UpdateArchiveEvent instead of
 *                                         using CreateArchiveEvent in doUpdate(..)
 */

package com.gridnode.gtas.client.ctrl;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.dbarchive.CreateArchiveEvent;
import com.gridnode.gtas.events.dbarchive.DeleteArchiveEvent;
import com.gridnode.gtas.events.dbarchive.GetArchiveEvent;
import com.gridnode.gtas.events.dbarchive.GetArchiveListEvent;
import com.gridnode.gtas.events.dbarchive.UpdateArchiveEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.util.TimeUtil;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.client.web.archive.helpers.Logger;


class DefaultArchiveManager extends DefaultAbstractManager implements
                                                          IGTArchiveManager
{

  DefaultArchiveManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_ARCHIVE, session);

    loadFmi(IGTEntity.ENTITY_ARCHIVE);

  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTArchiveEntity archive = (IGTArchiveEntity) entity;
      UpdateArchiveEvent event = new UpdateArchiveEvent();
      
      event.setUID(StaticUtils.longValue(entity.getFieldString(IGTArchiveEntity.UID)));
      event.setArchiveID(entity.getFieldString(IGTArchiveEntity.ARCHIVE_ID));
      event.setArchiveDescription(entity.getFieldString(IGTArchiveEntity.ARCHIVE_DESCRIPTION));

      String fromDateStr = entity.getFieldString(IGTArchiveEntity.FROM_START_DATE);
      String toDateStr = entity.getFieldString(IGTArchiveEntity.TO_START_DATE);

      String fromTimeStr = entity.getFieldString(IGTArchiveEntity.FROM_START_TIME);
      String toTimeStr = entity.getFieldString(IGTArchiveEntity.TO_START_TIME);

      event.setFromStartDate(fromDateStr);
      event.setToStartDate(toDateStr);

      event.setFromStartTime(fromTimeStr);
      event.setToStartTime(toTimeStr);
      
      String archiveType = entity.getFieldString(IGTArchiveEntity.ARCHIVE_TYPE);
      event.setArchiveType(archiveType);

      Boolean isEnabledSearchArchived = StaticUtils.booleanValue( entity
          .getFieldString(IGTArchiveEntity.ENABLE_SEARCH_ARCHIVED));
      Boolean isEnabledRestoreArchived = StaticUtils.booleanValue( entity
          .getFieldString(IGTArchiveEntity.ENABLE_RESTORE_ARCHIVED));
      event.setEnableSearchArchived(isEnabledSearchArchived);
      event.setEnableRestoreArchived(isEnabledRestoreArchived);


      List partnerList = (List) entity.getFieldValue(IGTArchiveEntity.PARTNER_ID_FOR_ARCHIVE);
      event.setPartnerIDForArchive(partnerList);
      
      //#122 30032009
      String archiveOlderThan = (String)(entity.getFieldValue(IGTArchiveEntity.ARCHIVE_OLDER_THAN));
      if(archiveOlderThan != null && archiveOlderThan.trim().length() > 0)
      {
        event.setArchiveOlderThan( Integer.parseInt(archiveOlderThan));
      }
      
      event.setArchiveFrequencyOnce( Boolean.parseBoolean( (String)(entity.getFieldValue(IGTArchiveEntity.IS_ARCHIVE_FREQUENCY_ONCE))));
      event.setClientTZ((String) entity.getFieldValue(IGTArchiveEntity.CLIENT_TZ));
      
      if (IGTArchiveEntity.ARCHIVE_TYPE_PROCESS_INSTANCE.equals(archiveType))
      {
        List processDef = (List) entity.getFieldValue(IGTArchiveEntity.PROCESS_DEF_NAME_LIST);
        Boolean includeIncomplete =  StaticUtils.booleanValue( entity.getFieldString(IGTArchiveEntity.INCLUDE_INCOMPLETE_PROCESSES));

        event.setProcessDefNameList(processDef);
        event.setIncludeIncompleteProcesses(includeIncomplete);

      }
      else if (IGTArchiveEntity.ARCHIVE_TYPE_DOCUMENT.equals(archiveType))
      {
        List folder = (List) entity.getFieldValue(IGTArchiveEntity.FOLDER_LIST);
        List docType = (List) entity.getFieldValue(IGTArchiveEntity.DOCUMENT_TYPE_LIST);

        event.setFolderList(folder);
        event.setDocumentTypeList(docType);
      }
      
      handleUpdateEvent(event, (AbstractGTEntity) entity);
    }
    catch (Exception e)
    {
      throw new GTClientException(
                                  "GTAS Error attempting to update Archive",
                                  e);
    }
  }

  private IEvent constructEvent(IGTEntity entity, boolean b) throws GTClientException
  {
    try
    {
      
      CreateArchiveEvent event = new CreateArchiveEvent();
      String archiveID = entity.getFieldString(IGTArchiveEntity.ARCHIVE_ID);
      event.setArchiveID(archiveID);
      event.setArchiveDescription(entity
          .getFieldString(IGTArchiveEntity.ARCHIVE_DESCRIPTION));

      String fromDateStr = entity.getFieldString(IGTArchiveEntity.FROM_START_DATE);
      String toDateStr = entity.getFieldString(IGTArchiveEntity.TO_START_DATE);

      String fromTimeStr = entity.getFieldString(IGTArchiveEntity.FROM_START_TIME);
      String toTimeStr = entity.getFieldString(IGTArchiveEntity.TO_START_TIME);

      event.setFromStartDate(fromDateStr);
      event.setToStartDate(toDateStr);

      event.setFromStartTime(fromTimeStr);
      event.setToStartTime(toTimeStr);

      String archiveType = entity.getFieldString(IGTArchiveEntity.ARCHIVE_TYPE);
      Logger.debug("DefaultArchiveManager: Archive Type - " + archiveType);
      event.setArchiveType(archiveType);

      Boolean isEnabledSearchArchived = StaticUtils.booleanValue( entity
          .getFieldString(IGTArchiveEntity.ENABLE_SEARCH_ARCHIVED));
      Boolean isEnabledRestoreArchived = StaticUtils.booleanValue( entity
          .getFieldString(IGTArchiveEntity.ENABLE_RESTORE_ARCHIVED));
      event.setEnableSearchArchived(isEnabledSearchArchived);
      event.setEnableRestoreArchived(isEnabledRestoreArchived);
      
      Logger.debug("[DefaultArchiveManager ] isEnabledSearchArchived "+isEnabledSearchArchived);
      Logger.debug("[DefaultArchiveManager ] isEnabledRestoreArchived "+isEnabledRestoreArchived);
      

      List partnerList = (List) entity
          .getFieldValue(IGTArchiveEntity.PARTNER_ID_FOR_ARCHIVE);
      for (int i = 0; partnerList != null && partnerList.size() > 0
                      && i < partnerList.size(); i++)
      {
        Logger.log("[DefaultArchiveManager] partner selected is "
                   + partnerList.get(i));
      }
      event.setPartnerIDForArchive(partnerList);
      
      //#122 30032009
      String archiveOlderThan = (String)(entity.getFieldValue(IGTArchiveEntity.ARCHIVE_OLDER_THAN));
      if(archiveOlderThan != null && archiveOlderThan.trim().length() > 0)
      {
        event.setArchiveOlderThan( Integer.parseInt(archiveOlderThan));
      }
      
      event.setArchiveFrequencyOnce( Boolean.parseBoolean( (String)(entity.getFieldValue(IGTArchiveEntity.IS_ARCHIVE_FREQUENCY_ONCE))));
      event.setClientTZ((String) entity.getFieldValue(IGTArchiveEntity.CLIENT_TZ));
      
      if (IGTArchiveEntity.ARCHIVE_TYPE_PROCESS_INSTANCE.equals(archiveType))
      {
        List processDef = (List) entity
            .getFieldValue(IGTArchiveEntity.PROCESS_DEF_NAME_LIST);
        Boolean includeIncomplete =  StaticUtils.booleanValue( entity
           .getFieldString(IGTArchiveEntity.INCLUDE_INCOMPLETE_PROCESSES));

        event.setProcessDefNameList(processDef);
        event.setIncludeIncompleteProcesses(includeIncomplete);

      }
      else if (IGTArchiveEntity.ARCHIVE_TYPE_DOCUMENT.equals(archiveType))
      {
        List folder = (List) entity
            .getFieldValue(IGTArchiveEntity.FOLDER_LIST);
        List docType = (List) entity
            .getFieldValue(IGTArchiveEntity.DOCUMENT_TYPE_LIST);

        event.setFolderList(folder);
        event.setDocumentTypeList(docType);
      }
      else
      {
        throw new GTClientException("Invalid archiveType:" + archiveType);
      }
      return event;
    }
    catch (EventException e)
    {
      throw new GTClientException(e);
    }
  }

  @SuppressWarnings("unchecked")
  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    if (IGTEntity.ENTITY_ARCHIVE.equals(entity.getType()))
    {
      try
      {
        DefaultArchiveEntity da = (DefaultArchiveEntity) entity;
        CreateArchiveEvent event = (CreateArchiveEvent) constructEvent(entity, true);
        Map results = (Map) ((DefaultGTSession) _session).fireEvent(event);

        if (results == null)
        { // Internal sanity check
          throw new NullPointerException(
                                         "ArchiveEvent event return null result map");
        }
        da.setNewEntity(false);
        da.setEntityDirty(false);
      }
      catch (Throwable t)
      {
        throw new GTClientException("Error creating archive", t);
      }
    }
    else
    {
      throw new UnsupportedOperationException("Bad entity:" + entity);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_ARCHIVE;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_ARCHIVE;
  }

  protected IEvent getGetEvent(Long uid) throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetArchiveEvent(uid);
  }

  protected IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter) throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetArchiveListEvent(filter);

  }

  protected IEvent getDeleteEvent(Collection uids) throws EventException
  {
    return new DeleteArchiveEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType) throws GTClientException
  {
    if (IGTEntity.ENTITY_ARCHIVE.equals(entityType))
    {
      return new DefaultArchiveEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException(
                                                        "Manager:"
                                                            + this
                                                            + " cannot create entity object of type "
                                                            + entityType);
    }
  }


  public IGTEntity newEntity() throws GTClientException
  {
    AbstractGTEntity entity = initEntityObjects(IGTEntity.ENTITY_ARCHIVE);
    entity.setNewEntity(true);
    return entity;
  }



  
  private Timestamp convertToUTC(String date, String time, TimeZone defTZ) throws ParseException
  {
    if (defTZ == null)
    {
      defTZ = TimeZone.getDefault();
    }
    Calendar c = Calendar.getInstance(defTZ);
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    formatter.setTimeZone(defTZ);
    Date givenDate = formatter.parse(date);
    c.setTimeInMillis(givenDate.getTime());
    int year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int day = c.get(Calendar.DAY_OF_MONTH);

    c = Calendar.getInstance(defTZ);
    formatter = new SimpleDateFormat("HH:mm");
    formatter.setTimeZone(defTZ);
    Date givenTime = formatter.parse(time);
    c.setTimeInMillis(givenTime.getTime());
    int hour = c.get(Calendar.HOUR_OF_DAY); // 24 hour
    int minute = c.get(Calendar.MINUTE);

    c = Calendar.getInstance(defTZ);
    c.set(Calendar.YEAR, year);
    c.set(Calendar.MONTH, month);
    c.set(Calendar.DAY_OF_MONTH, day);
    c.set(Calendar.HOUR_OF_DAY, hour);
    c.set(Calendar.MINUTE, minute);
    c.set(Calendar.SECOND, 0);
    c.set(Calendar.MILLISECOND, 0);

    return new Timestamp(TimeUtil.localToUtc(c.getTimeInMillis()));
  }
}