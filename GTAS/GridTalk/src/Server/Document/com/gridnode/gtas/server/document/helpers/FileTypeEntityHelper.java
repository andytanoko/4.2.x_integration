/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FileTypeEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.document.helpers;

import com.gridnode.gtas.server.document.model.FileType;

import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;

/**
 * This class contains utitlies methods for the FileType entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class FileTypeEntityHelper implements ICheckConflict
{

  private static FileTypeEntityHelper _self = null;

  private FileTypeEntityHelper()
  {
    super();
  }

  public static FileTypeEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(FileTypeEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new FileTypeEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity fileType) throws Exception
  {
    Logger.debug("[FileTypeEntityHelper.checkDuplicate] Start");
    String type = fileType.getFieldValue(FileType.FILE_TYPE).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, FileType.FILE_TYPE, filter.getEqualOperator(),
      type, false);

    FileTypeEntityHandler handler = FileTypeEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (FileType)results.iterator().next();
    }
    return null;
  }
}