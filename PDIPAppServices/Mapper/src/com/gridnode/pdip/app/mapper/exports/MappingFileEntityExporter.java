/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MappingFileEntityExporter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 07 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.mapper.exports;

import com.gridnode.pdip.app.mapper.helpers.Logger;
import com.gridnode.pdip.app.mapper.helpers.XpathMappingEntityHandler;
import com.gridnode.pdip.app.mapper.model.MappingFile;
import com.gridnode.pdip.app.mapper.model.XpathMapping;
import com.gridnode.pdip.base.exportconfig.exception.ExportConfigException;
import com.gridnode.pdip.base.exportconfig.exports.AbstractEntityExporter;
import com.gridnode.pdip.base.exportconfig.exports.ExportRegistry;
import com.gridnode.pdip.base.exportconfig.model.ImportEntity;
import com.gridnode.pdip.framework.db.entity.IEntity;

/**
 * This class will know how to export a MappingFile entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class MappingFileEntityExporter extends AbstractEntityExporter
{
  private static MappingFileEntityExporter _self = null;

  private MappingFileEntityExporter()
  {
    super();
  }

  public static MappingFileEntityExporter getInstance()
  {
    if(_self == null)
    {
      synchronized(MappingFileEntityExporter.class)
      {
        if (_self == null)
        {
          _self = new MappingFileEntityExporter();
        }
      }
    }
    return _self;
  }

  /**
   * This method returns a collection of foreign entities found in the entity
   * passed it.
   *
   * @param entity The entity whose foreign entities are to be retrieved.
   * @returns an Collection of IEntity. Empty is no foreign entities are found.
   */
  public ExportRegistry getAllForeignEntities(IEntity entity,
    ExportRegistry registry)
    throws Exception
  {
    try
    {
      Logger.debug("[MappingFileEntityExporter.getAllForeignEntities] Start");

      MappingFile mappingFile = (MappingFile)entity;
      if (mappingFile.getType().equals(MappingFile.XPATH))
      {
        ImportEntity exportMappingFile =
          registry.getExportList().getEntity(mappingFile);
        registry.getExportList().removeImportEntity(exportMappingFile);

        Long uid = (Long)mappingFile.getKey();
        XpathMapping xpathMapping =
          XpathMappingEntityHandler.getInstance().findByXpathUid(uid);
        mappingFile.setMappingObject(xpathMapping);
        ImportEntity updatedMappingFile = new ImportEntity(mappingFile, uid, null, false, false);
        registry.getExportList().addImportEntity(updatedMappingFile);
      }
    }
    catch (Throwable ex)
    {
      throw new ExportConfigException("Error exporting alert "+entity.getEntityDescr());
    }
    return registry;
  }

}