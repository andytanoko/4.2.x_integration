/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ProcessDefEntityImporter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 30 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.rnif.imports;

import java.util.Properties;

import com.gridnode.pdip.app.rnif.helpers.BpssGenerator;
import com.gridnode.pdip.app.rnif.helpers.Logger;
import com.gridnode.pdip.app.rnif.model.ProcessDef;
import com.gridnode.pdip.base.exportconfig.imports.AbstractEntityImporter;
import com.gridnode.pdip.base.exportconfig.imports.ImportRegistry;
import com.gridnode.pdip.base.exportconfig.model.ImportEntity;
import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;
import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;
import com.gridnode.pdip.framework.db.meta.MetaInfoFactory;

/**
 * This class will know how to import a ProcessDef entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class ProcessDefEntityImporter extends AbstractEntityImporter
{

  private static ProcessDefEntityImporter _self = null;

  private ProcessDefEntityImporter()
  {
    super();
  }

  public static ProcessDefEntityImporter getInstance()
  {
    if(_self == null)
    {
      synchronized(ProcessDefEntityImporter.class)
      {
        if (_self == null)
        {
          _self = new ProcessDefEntityImporter();
        }
      }
    }
    return _self;
  }

  public boolean checkFields(ImportEntity entityToImport,
    ImportRegistry registry)
    throws Exception
  {
    Logger.debug("[ProcessDefEntityImporter.checkFields] Start");

    boolean noUnlinkForeignEntities = true;
    String entityName = entityToImport.getEntity().getEntityName();
    Logger.debug("[DefaultEntityImporter.checkFields] entityName = "+entityName);
    EntityMetaInfo metaInfo =
      MetaInfoFactory.getInstance().getMetaInfoFor(entityName);
    FieldMetaInfo[] fMetaInfos = metaInfo.getFieldMetaInfo();
    for (int j = 0; j < fMetaInfos.length; j++)
    {
      boolean isForeignFieldLinked = true;
      FieldMetaInfo fMetaInfo = fMetaInfos[j];
      Logger.debug("[DefaultEntityImporter.checkFields] entityName = "+
                    entityName+" fieldName = "+fMetaInfo.getFieldName());
      Properties properties = fMetaInfo.getConstraints();
      String type = properties.getProperty("type");
      if (type != null)
      {
        if (type.equals("foreign"))
        {
          isForeignFieldLinked = checkForeignEntity(entityToImport,
                                                    fMetaInfo,
                                                    registry,
                                                    properties);
        }
        else if (type.equals("dynamic") || type.equals("embedded"))
        {
          isForeignFieldLinked = checkEmbeddedEntity(entityToImport,
                                                     fMetaInfo,
                                                     registry);
        }

        if (!isForeignFieldLinked)
        {
          noUnlinkForeignEntities = false;
        }
      }
    }

    if (noUnlinkForeignEntities)
    {
      Logger.debug("[DefaultEntityImporter.checkFields] noUnlinkForeignEntities");
      if (!entityToImport.isEmbedded())
      {
        Logger.debug("[DefaultEntityImporter.checkFields] is Not Embedded ");
        ImportEntity persistedEntity = saveEntityToDB(entityToImport, registry);
        registry.getPersistedList().addImportEntity(persistedEntity);
        registry.getImportList().removeImportEntity(entityToImport);
        registry.getConflictList().removeImportEntity(entityToImport);

        ProcessDef def = (ProcessDef)persistedEntity.getEntity();
        new BpssGenerator(def).createBPSS();
      }
      else
      {
        Logger.debug("[DefaultEntityImporter.checkFields] is Embedded ");
      }
      return true;
    }
    else
    {
      Logger.debug("[DefaultEntityImporter.checkFields] got UnlinkForeignEntities");
    }
    return false;
  }

}