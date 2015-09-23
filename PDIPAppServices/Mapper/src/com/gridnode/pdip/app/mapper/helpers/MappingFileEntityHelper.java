/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MappingFileEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 29 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.mapper.helpers;

import java.util.Collection;

import com.gridnode.pdip.app.mapper.model.MappingFile;
import com.gridnode.pdip.app.mapper.model.XpathMapping;
import com.gridnode.pdip.base.exportconfig.exception.ExportConfigException;
import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

/**
 * This class contains utitlies methods for the MappingFile entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class MappingFileEntityHelper implements ICheckConflict
{
  private static MappingFileEntityHelper _self = null;

  private MappingFileEntityHelper()
  {
    super();
  }

  public static MappingFileEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(MappingFileEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new MappingFileEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity entity)
    throws Exception
  {
    Logger.debug("[MappingFileEntityHelper.checkDuplicate] Start");
    MappingFile mappingFile = (MappingFile)entity;
    String mappingFileName = mappingFile.getName();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, MappingFile.NAME, filter.getEqualOperator(),
      mappingFileName, false);

    MappingFileEntityHandler handler = MappingFileEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (MappingFile)results.iterator().next();
    }

    if (mappingFile.getType().equals(MappingFile.XPATH))
    {
      XpathMapping xpathMapping = (XpathMapping)mappingFile.getMappingObject();
      String rootName = xpathMapping.getRootElement();
      try
      {
        XpathMapping conflict =
          XpathMappingEntityHandler.getInstance().findByRootName(rootName);
        if (conflict != null)
        {
          Long conflictMappingFile = conflict.getXpathUid();
          return (MappingFile)handler.getEntityByKeyForReadOnly(conflictMappingFile);
        }
      }
      catch (Throwable ex)
      {
        throw new ExportConfigException(ex);
      }
    }

    return null;
  }
}