/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MappingFileEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 27 2002    Koh Han Sing        Created
 * Aug 20 2003    Koh Han Sing        GNDB00014938 XSL cache not cleared after
 *                                    create/update of XSL mapping file.
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 * Mar 07 2006    Neo Sok Lay         Use generics.
 * Jun 03 2010    Tam Wei Xiang       #1100 - Mark the mapping jar as deleted if the system
 *                                    still caching it when user perform deletion.                                   
 */
package com.gridnode.pdip.app.mapper.helpers;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import com.gridnode.pdip.app.mapper.entities.ejb.IMappingFileLocalHome;
import com.gridnode.pdip.app.mapper.entities.ejb.IMappingFileLocalObj;
import com.gridnode.pdip.app.mapper.model.MappingFile;
import com.gridnode.pdip.app.mapper.model.XpathMapping;
import com.gridnode.pdip.base.xml.helpers.XMLServiceHandler;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the MappingFileBean.
 *
 * @author Koh Han Sing
 *
 * @version GT 4.0
 * @since 2.0
 */
public final class MappingFileEntityHandler
  extends          LocalEntityHandler
{
  private MappingFileEntityHandler()
  {
    super(MappingFile.ENTITY_NAME);
  }

  /**
   * Get an instance of a MappingFileEntityHandler.
   */
  public static MappingFileEntityHandler getInstance()
  {
    MappingFileEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(MappingFile.ENTITY_NAME, true))
    {
      handler = (MappingFileEntityHandler)EntityHandlerFactory.getHandlerFor(
                  MappingFile.ENTITY_NAME, true);
    }
    else
    {
      handler = new MappingFileEntityHandler();
      EntityHandlerFactory.putEntityHandler(MappingFile.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }

  /**
   * Find the MappingFile whose name is the specified.
   *
   * @param mappingFileName The name of the MappingFile.
   * @return the MappingFile having the specified name.
   */
  public MappingFile findByMappingFileName(String mappingFileName) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, MappingFile.NAME, filter.getEqualOperator(),
      mappingFileName, false);

    Collection result = getEntityByFilterForReadOnly(filter);
    if(result == null || result.isEmpty())
      return null;
    return (MappingFile)result.iterator().next();
  }

  /**
   * Find the MappingFile whose type is the specified.
   *
   * @param mappingFileType The type of the MappingFile to retrieve.
   * @return the MappingFiles having the specified type.
   */
  public Collection<MappingFile> findByMappingFileType(Short mappingFileType) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, MappingFile.TYPE, filter.getEqualOperator(),
      mappingFileType, false);

    Collection result = getEntityByFilterForReadOnly(filter);
    return result;
  }

  public Object create(MappingFile mappingFile) throws Throwable
  {
    Logger.debug("[MappingFileEntityHandler.create]");
    if (mappingFile.getType().equals(MappingFile.XSL))
    {
      XMLServiceHandler.getInstance().clearCache();
    }
    return super.create(mappingFile);
  }

  public IEntity createEntity(MappingFile mappingFile) throws Throwable
  {
    Logger.debug("[MappingFileEntityHandler.createEntity]");
    if (mappingFile.getType().equals(MappingFile.XSL))
    {
      XMLServiceHandler.getInstance().clearCache();
    }
    return super.createEntity(mappingFile);
  }

  public void update(MappingFile mappingFile) throws Throwable
  {
    Logger.debug("[MappingFileEntityHandler.update]");
    if (mappingFile.getType().equals(MappingFile.XSL))
    {
      XMLServiceHandler.getInstance().clearCache();
    }
    super.update(mappingFile);
  }

  public void remove(Long mappingFileUid) throws Throwable
  {
    MappingFile mappingFile =
      (MappingFile)getEntityByKeyForReadOnly(mappingFileUid);
    if (mappingFile.getType().equals(MappingFile.XPATH))
    {
      XpathMapping delMapping =
        XpathMappingEntityHandler.getInstance().findByXpathUid(mappingFileUid);
      if (delMapping != null)
      {
        XpathMappingEntityHandler.getInstance().remove(new Long(delMapping.getUId()));
      }
    }

    String pathkey = (String)mappingFile.getFieldValue(MappingFile.PATH);
    String filename = (String)mappingFile.getFieldValue(MappingFile.FILENAME);
    String subPath = mappingFile.getSubPath();
    
    //TWX 20100602 #1100 handling for mapping file with type JAVA_BINARY
    //The following is temp work around for deleting the jar file which is 
    //still referenced by the un-collectable JarFileFactory instance. See Sun bug id = 4167874
    if(MappingFile.JAVA_BINARY.equals(mappingFile.getType()))
    {
      try
      {
        FileUtil.delete(pathkey, subPath==null?"":subPath, filename);
      }
      catch(FileAccessException e)
      {
        
          File mapFile = FileUtil.getFile(pathkey, subPath==null?"":subPath, filename);
          if(mapFile != null && mapFile.exists())
          {
            Logger.debug("[MappingFileEntityHandler.remove] MappingFile="+mapFile.getAbsolutePath()+" is still referenced by jvm, will be deleted when system is shutdown");
            //mapFile.deleteOnExit();
            
            MappingFileHelper.markMappingFileAsDeleted(pathkey, filename);
            Logger.warn("Can not remove Mapping file="+mappingFile.getFilename()+ " under subpath="+subPath, e);
          }
          
      }
    }
    else
    {
      FileUtil.delete(pathkey, subPath==null?"":subPath, filename);
    }
    
    
    super.remove(mappingFileUid);
  }

  public void removeByFilter(IDataFilter filter) throws Throwable
  {
    Collection deleteList = findByFilter(filter);
    for (Iterator i=deleteList.iterator(); i.hasNext(); )
    {
      MappingFile mappingFile = (MappingFile)i.next();
      if (mappingFile.getType().equals(MappingFile.XPATH))
      {
        XpathMapping delMapping =
          XpathMappingEntityHandler.getInstance().findByXpathUid(new Long(mappingFile.getUId()));
        if (delMapping != null)
        {
          XpathMappingEntityHandler.getInstance().remove(new Long(delMapping.getUId()));
        }
      }

      String pathkey = (String)mappingFile.getFieldValue(MappingFile.PATH);
      String filename = (String)mappingFile.getFieldValue(MappingFile.FILENAME);
      String subPath = mappingFile.getSubPath();
      FileUtil.delete(pathkey, subPath==null?"":subPath, filename);
    }

    super.removeByFilter(filter);
  }


  /**
   * Looks up the Home interface in the local context.
   *
   * @return The Local Home interface object.
   *//*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IMappingFileLocalHome.class.getName(),
      IMappingFileLocalHome.class);
  }*/

  protected Class getHomeInterfaceClass() throws Exception
	{
		return IMappingFileLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IMappingFileLocalObj.class;
  }
}