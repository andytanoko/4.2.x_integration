/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultPortManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-19     Andrew Hill         Created
 * 2002-05-22     Daniel D'Cotta      Added running sequence number support
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 * 2005-08-22	    Tam Wei Xiang	      For doUpdate(), doCreate() : Variable attachmentDir 
 *                                    has been removed.
 *                                    (GDOC n UDOC will be placed in the same folder.)
 * 2006-03-03     Tam Wei Xiang       Added FileGrouping in IGTPortEntity                                  
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.events.backend.CreatePortEvent;
import com.gridnode.gtas.events.backend.DeletePortEvent;
import com.gridnode.gtas.events.backend.GetPortEvent;
import com.gridnode.gtas.events.backend.GetPortListEvent;
import com.gridnode.gtas.events.backend.UpdatePortEvent;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;

class DefaultPortManager extends DefaultAbstractManager
  implements IGTPortManager
{
  DefaultPortManager(DefaultGTSession session) throws GTClientException
  {
    super(IGTManager.MANAGER_PORT, session);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    try
    {
      IGTPortEntity port = (IGTPortEntity)entity;

      Long uid = port.getUidLong();
      String description = port.getFieldString(IGTPortEntity.DESCRIPTION);
      Boolean isRfc = (Boolean)port.getFieldValue(IGTPortEntity.IS_RFC);
      Long rfc =  isRfc.booleanValue() ? (Long)port.getFieldValue(IGTPortEntity.RFC) : null;
      String hostDir = port.getFieldString(IGTPortEntity.HOST_DIR);
      Boolean isOverwrite = (Boolean)port.getFieldValue(IGTPortEntity.IS_OVERWRITE);
      Boolean isDiffFilename = (Boolean)port.getFieldValue(IGTPortEntity.IS_DIFF_FILE_NAME);
      boolean idfn = isDiffFilename.booleanValue();
      String filename = idfn ? port.getFieldString(IGTPortEntity.FILE_NAME) : null;
      Boolean isAddFileExt = (Boolean)port.getFieldValue(IGTPortEntity.IS_ADD_FILE_EXT);
      boolean iafe = isAddFileExt.booleanValue();
      Integer fileExtType = idfn && iafe ? (Integer)port.getFieldValue(IGTPortEntity.FILE_EXT_TYPE) : null;
      String fileExtValue = idfn && iafe ? port.getFieldString(IGTPortEntity.FILE_EXT_VALUE) : null;
      //String attachmentDir = port.getFieldString(IGTPortEntity.ATTACHMENT_DIR);
      Boolean isExportGdoc = (Boolean)port.getFieldValue(IGTPortEntity.IS_EXPORT_GDOC);

      // running sequence number (always remember setting even if extention type is different)
      Integer startNum = (Integer)port.getFieldValue(IGTPortEntity.START_NUM);
      Integer rolloverNum = (Integer)port.getFieldValue(IGTPortEntity.ROLLOVER_NUM);
      Integer nextNum = (Integer)port.getFieldValue(IGTPortEntity.NEXT_NUM);
      Boolean isPadded = (Boolean)port.getFieldValue(IGTPortEntity.IS_PADDED);
      Integer fixNumLength = isPadded.booleanValue() ? (Integer)port.getFieldValue(IGTPortEntity.FIX_NUM_LENGTH) : null;
      
      Integer fileGrouping = (Integer)port.getFieldValue(IGTPortEntity.FILE_GROUPING);
      
      IEvent event = new UpdatePortEvent( uid,
                                          description,
                                          isRfc,
                                          rfc,
                                          hostDir,
                                          isDiffFilename,
                                          isOverwrite,
                                          filename,
                                          isAddFileExt,
                                          fileExtType,
                                          fileExtValue,
                                          isExportGdoc,
                                          startNum,
                                          rolloverNum,
                                          nextNum,
                                          isPadded,
                                          fixNumLength,
                                          fileGrouping);

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
      IGTPortEntity port = (IGTPortEntity)entity;

      String name = port.getFieldString(IGTPortEntity.NAME);
      String description = port.getFieldString(IGTPortEntity.DESCRIPTION);
      Boolean isRfc = (Boolean)port.getFieldValue(IGTPortEntity.IS_RFC);
      Long rfc =  isRfc.booleanValue() ? (Long)port.getFieldValue(IGTPortEntity.RFC) : null;
      String hostDir = port.getFieldString(IGTPortEntity.HOST_DIR);
      Boolean isOverwrite = (Boolean)port.getFieldValue(IGTPortEntity.IS_OVERWRITE);
      Boolean isDiffFilename = (Boolean)port.getFieldValue(IGTPortEntity.IS_DIFF_FILE_NAME);
      boolean idfn = isDiffFilename.booleanValue();
      String filename = idfn ? port.getFieldString(IGTPortEntity.FILE_NAME) : null;
      Boolean isAddFileExt = (Boolean)port.getFieldValue(IGTPortEntity.IS_ADD_FILE_EXT);
      boolean iafe = isAddFileExt.booleanValue();
      Integer fileExtType = idfn && iafe ? (Integer)port.getFieldValue(IGTPortEntity.FILE_EXT_TYPE) : null;
      String fileExtValue = idfn && iafe ? port.getFieldString(IGTPortEntity.FILE_EXT_VALUE) : null;
      //String attachmentDir = port.getFieldString(IGTPortEntity.ATTACHMENT_DIR);
      Boolean isExportGdoc = (Boolean)port.getFieldValue(IGTPortEntity.IS_EXPORT_GDOC);

      // running sequence number (always remember setting even if extention type is different)
      Integer startNum = (Integer)port.getFieldValue(IGTPortEntity.START_NUM);
      Integer rolloverNum = (Integer)port.getFieldValue(IGTPortEntity.ROLLOVER_NUM);
      Integer nextNum = (Integer)port.getFieldValue(IGTPortEntity.NEXT_NUM);
      Boolean isPadded = (Boolean)port.getFieldValue(IGTPortEntity.IS_PADDED);
      Integer fixNumLength = isPadded.booleanValue() ? (Integer)port.getFieldValue(IGTPortEntity.FIX_NUM_LENGTH) : null;
      
      Integer fileGrouping = (Integer)port.getFieldValue(IGTPortEntity.FILE_GROUPING);
      
      IEvent event = new CreatePortEvent( name,
                                          description,
                                          isRfc,
                                          rfc,
                                          hostDir,
                                          isDiffFilename,
                                          isOverwrite,
                                          filename,
                                          isAddFileExt,
                                          fileExtType,
                                          fileExtValue,
                                          isExportGdoc,
                                          startNum,
                                          rolloverNum,
                                          nextNum,
                                          isPadded,
                                          fixNumLength,
                                          fileGrouping);
      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_PORT;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_PORT;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetPortEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetPortListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeletePortEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    if(IGTEntity.ENTITY_PORT.equals(entityType))
    {
      return new DefaultPortEntity();
    }
    else
    {
      throw new java.lang.UnsupportedOperationException("Manager:" + this
                  + " cannot create entity object of type " + entityType);
    }
  }
}