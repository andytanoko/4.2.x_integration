/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MappingFileEntityImporter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 06 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.mapper.imports;

import com.gridnode.pdip.app.mapper.helpers.Logger;
import com.gridnode.pdip.app.mapper.helpers.XpathMappingEntityHandler;
import com.gridnode.pdip.app.mapper.model.MappingFile;
import com.gridnode.pdip.app.mapper.model.XpathMapping;
import com.gridnode.pdip.base.exportconfig.exception.ImportConfigException;
import com.gridnode.pdip.base.exportconfig.imports.AbstractEntityImporter;
import com.gridnode.pdip.base.exportconfig.imports.ImportRegistry;
import com.gridnode.pdip.base.exportconfig.model.ImportEntity;

/**
 * This class will know how to import a MappingFile entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class MappingFileEntityImporter extends AbstractEntityImporter
{

  private static MappingFileEntityImporter _self = null;

  private MappingFileEntityImporter()
  {
    super();
  }

  public static MappingFileEntityImporter getInstance()
  {
    if(_self == null)
    {
      synchronized(MappingFileEntityImporter.class)
      {
        if (_self == null)
        {
          _self = new MappingFileEntityImporter();
        }
      }
    }
    return _self;
  }

  public boolean checkFields(ImportEntity entityToImport,
    ImportRegistry registry)
    throws Exception
  {
    Logger.debug("[DefaultEntityImporter.checkFields] Start");

    try
    {
      ImportEntity persistedEntity = saveEntityToDB(entityToImport, registry);
      registry.getPersistedList().addImportEntity(persistedEntity);
      registry.getImportList().removeImportEntity(entityToImport);
      registry.getConflictList().removeImportEntity(entityToImport);

      MappingFile mappingFile = (MappingFile)persistedEntity.getEntity();
      if (mappingFile.getType().equals(MappingFile.XPATH))
      {
        Long oldUid = persistedEntity.getOldUid();
        XpathMapping oldXpathMapping =
          XpathMappingEntityHandler.getInstance().findByXpathUid(oldUid);
        if (oldXpathMapping != null)
        {
          Long oldXpathMappingUid = (Long)oldXpathMapping.getKey();
          XpathMappingEntityHandler.getInstance().remove(oldXpathMappingUid);
        }

        XpathMapping xpathMapping = (XpathMapping)mappingFile.getMappingObject();
        String rootName = xpathMapping.getRootElement();
        XpathMapping oldxpathMapping =
          XpathMappingEntityHandler.getInstance().findByRootName(rootName);
        if (oldxpathMapping != null)
        {
          Long uid = (Long)oldxpathMapping.getKey();
          XpathMappingEntityHandler.getInstance().remove(uid);
        }
        Long mappingFileUid = (Long)mappingFile.getKey();
        xpathMapping.setXpathUid(mappingFileUid);
        XpathMappingEntityHandler.getInstance().createEntity(xpathMapping);
      }
    }
    catch (Throwable ex)
    {
      throw new ImportConfigException(ex);
    }
    return true;
  }

}