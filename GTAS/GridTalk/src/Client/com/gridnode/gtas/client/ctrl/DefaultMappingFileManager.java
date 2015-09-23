/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultMappingFileManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-12     Andrew Hill         Created
 * 2002-07-15     Andrew Hill         Add getAllOfType() method
 * 2002-10-09     Andrew Hill         "partnerCat" mods
 * 2003-07-18     Andrew Hill         Support for new multiple deletion events
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

//import sun.rmi.rmic.iiop.StaticStringsHash;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.utils.StaticUtils;
import com.gridnode.gtas.events.mapper.CreateMappingFileEvent;
import com.gridnode.gtas.events.mapper.DeleteMappingFileEvent;
import com.gridnode.gtas.events.mapper.GetJavaBinaryClassesEvent;
import com.gridnode.gtas.events.mapper.GetMappingFileEvent;
import com.gridnode.gtas.events.mapper.GetMappingFileListEvent;
import com.gridnode.gtas.events.mapper.UpdateMappingFileEvent;
import com.gridnode.gtas.events.userprocedure.GetClassesFromJarEvent;
import com.gridnode.gtas.model.mapper.IMappingFile;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.rpf.event.EventException;

class DefaultMappingFileManager extends DefaultAbstractManager
  implements IGTMappingFileManager
{

  DefaultMappingFileManager(DefaultGTSession session)
    throws GTClientException
  {
    super(IGTManager.MANAGER_MAPPING_FILE, session);
  }

  public IGTMappingFileEntity getMappingFileByUID(long uid)
    throws GTClientException
  {
    return (IGTMappingFileEntity)getByUid(uid);
  }

  protected void doUpdate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    update(entity,false);
  }

  public void update(IGTEntity entity, boolean updateFile)
    throws com.gridnode.gtas.client.GTClientException
  {
    IGTMappingFileEntity gtmf = (IGTMappingFileEntity)entity;
    try
    {
      Long   uid = (Long)gtmf.getFieldValue(IGTMappingFileEntity.UID);
      String description = gtmf.getFieldString(IGTMappingFileEntity.DESCRIPTION);
      String filename = gtmf.getFieldString(IGTMappingFileEntity.FILENAME);
      String subPath = fixSubPath(gtmf.getFieldString(IGTMappingFileEntity.SUB_PATH));
      Short  type = (Short)gtmf.getFieldValue(IGTMappingFileEntity.TYPE);
      UpdateMappingFileEvent event = new UpdateMappingFileEvent(
                                          uid,
                                          description,
                                          filename,
                                          subPath,
                                          type);
      handleUpdateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to update", e);
    }
  }

  protected void doCreate(IGTEntity entity) throws com.gridnode.gtas.client.GTClientException
  {
    IGTMappingFileEntity gtmf = (IGTMappingFileEntity)entity;
    try
    {
      String name = gtmf.getFieldString(IGTMappingFileEntity.NAME);
      String description = gtmf.getFieldString(IGTMappingFileEntity.DESCRIPTION);
      String filename = gtmf.getFieldString(IGTMappingFileEntity.FILENAME);
      String subPath = fixSubPath(gtmf.getFieldString(IGTMappingFileEntity.SUB_PATH));
//System.out.println("[DEBUG] DefaultMappingFileManager.create(): subPath=" + subPath);      
      Short  type = (Short)gtmf.getFieldValue(IGTMappingFileEntity.TYPE);
      Short mappingClass = (Short)gtmf.getFieldValue(IGTMappingFileEntity.MAPPNG_CLASS);
      CreateMappingFileEvent event = new CreateMappingFileEvent(
                                          name,
                                          description,
                                          filename,
                                          subPath,
                                          type);
      handleCreateEvent(event, (AbstractGTEntity)entity);
    }
    catch(Exception e)
    {
      throw new GTClientException("GTAS Error attempting to create", e);
    }
  }

  public Collection getAllOfType(Short type) throws GTClientException
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null,IMappingFile.TYPE,filter.getEqualOperator(),type,false);

    GetMappingFileListEvent event = new GetMappingFileListEvent(filter);
    return handleGetListEvent(event);
  }

  protected int getManagerType()
  {
    return IGTManager.MANAGER_MAPPING_FILE;
  }

  protected String getEntityType()
  {
    return IGTEntity.ENTITY_MAPPING_FILE;
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetEvent(Long uid)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetMappingFileEvent(uid);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getGetListEvent(com.gridnode.pdip.framework.db.filter.IDataFilter filter)
    throws com.gridnode.pdip.framework.rpf.event.EventException
  {
    return new GetMappingFileListEvent(filter);
  }

  protected com.gridnode.pdip.framework.rpf.event.IEvent getDeleteEvent(Collection uids)
    throws EventException
  { //20030718AH
    return new DeleteMappingFileEvent(uids);
  }

  protected AbstractGTEntity createEntityObject(String entityType)
    throws GTClientException
  {
    return new DefaultMappingFileEntity();
  }
  
  //added by ming qian
  public List listClassesInJar(Long longUid) throws GTClientException
  { //20030716AH
    if (longUid == null)
      throw new NullPointerException("longUid is null");
    try
    {
      GetJavaBinaryClassesEvent event = new GetJavaBinaryClassesEvent(longUid);
      List classes = (List)handleEvent(event);
      return classes == null ? Collections.EMPTY_LIST : classes; 
    }
    catch(Throwable t)
    {
      throw new GTClientException("Unable to get list of classes in jar file for procedureDefFile with uid="
                                  + longUid,t);
    }
  }
  //end of added by ming qian

  protected String fixSubPath(String subPath)
  {
    if(StaticUtils.stringEmpty(subPath))
    {
      return "";
    }
    
    // Change all path separators to '/'
    subPath = FileUtil.convertPath(subPath);

    // remove any leading path separator
    if(subPath.charAt(0) == '/')
    {
      subPath = subPath.substring(1);
    }

    // ensure trailing path separator
    if(subPath.charAt(subPath.length() - 1) != '/')
    {
      subPath += '/';
    }
    
    return subPath;
  }
}