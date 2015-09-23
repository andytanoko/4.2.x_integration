/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileTypeEntityHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 18 2002    Koh Han Sing        Created
 * Oct 31 2005    Neo Sok Lay         1. Remove implementation for getHome()
 *                                    2. Implement getHomeInterfaceClass()
 */
package com.gridnode.gtas.server.document.helpers;

import java.util.Collection;

import com.gridnode.gtas.server.document.entities.ejb.IFileTypeLocalHome;
import com.gridnode.gtas.server.document.entities.ejb.IFileTypeLocalObj;
import com.gridnode.gtas.server.document.model.FileType;
import com.gridnode.pdip.framework.db.EntityHandlerFactory;
import com.gridnode.pdip.framework.db.LocalEntityHandler;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This helper class helps to handle the calls to the Home, Local interfaces
 * of the FileTypeBean.
 *
 * @author Koh Han Sing
 *
 * @version 2.0
 * @since 2.0
 */
public final class FileTypeEntityHandler
  extends          LocalEntityHandler
{
  private FileTypeEntityHandler()
  {
    super(FileType.ENTITY_NAME);
  }

  /**
   * Get an instance of a FileTypeEntityHandler.
   */
  public static FileTypeEntityHandler getInstance()
  {
    FileTypeEntityHandler handler = null;

    if (EntityHandlerFactory.hasEntityHandlerFor(FileType.ENTITY_NAME, true))
    {
      handler = (FileTypeEntityHandler)EntityHandlerFactory.getHandlerFor(
                  FileType.ENTITY_NAME, true);
    }
    else
    {
      handler = new FileTypeEntityHandler();
      EntityHandlerFactory.putEntityHandler(FileType.ENTITY_NAME,
         true, handler);
    }

    return handler;
  }

  /**
   * Find the FileType whose name is the specified.
   *
   * @param fileType The name of the file type.
   * @return the FileType having the specified name.
   */
  public FileType findByFileType(String fileType) throws Throwable
  {
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, FileType.FILE_TYPE, filter.getEqualOperator(),
      fileType, false);

    Collection result = getEntityByFilterForReadOnly(filter);
    if(result == null || result.isEmpty())
      return null;
    return (FileType)result.iterator().next();
  }

  /**
   * Looks up the Home interface in the local context.
   *
   * @return The Local Home interface object.
   *//*
  protected Object getHome() throws java.lang.Exception
  {
    return ServiceLocator.instance(ServiceLocator.LOCAL_CONTEXT).getHome(
      IFileTypeLocalHome.class.getName(),
      IFileTypeLocalHome.class);
  }*/

  
  protected Class getHomeInterfaceClass() throws Exception
	{
		return IFileTypeLocalHome.class;
	}

	/**
   * This method should return the Proxy (Remote/Local) interface class for the
   * EntityBean it handles.
   *
   * @return The Proxy interface class for the EntityBean.
   */
  protected Class getProxyInterfaceClass() throws Exception
  {
    return IFileTypeLocalObj.class;
  }
}