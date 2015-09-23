/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultEntityImporter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 03 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.base.exportconfig.imports;

import com.gridnode.pdip.base.exportconfig.helpers.IMetaInfoConstants;
import com.gridnode.pdip.base.exportconfig.helpers.Logger;
import com.gridnode.pdip.base.exportconfig.model.ImportEntity;

import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;
import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;
import com.gridnode.pdip.framework.db.meta.MetaInfoFactory;

import java.util.Properties;

/**
 * This class will know how to import a entity.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.3.3
 * @since 2.1 I1
 */

public class DefaultEntityImporter extends AbstractEntityImporter
{

  private static DefaultEntityImporter _self = null;

  protected DefaultEntityImporter()
  {
    super();
  }

  public static DefaultEntityImporter getInstance()
  {
    if(_self == null)
    {
      synchronized(DefaultEntityImporter.class)
      {
        if (_self == null)
        {
          _self = new DefaultEntityImporter();
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
      String type = properties.getProperty(IMetaInfoConstants.CONSTRAINT_TYPE, IMetaInfoConstants.ESTR);
      if (type.equals(IMetaInfoConstants.TYPE_FOREIGN))
      {
        isForeignFieldLinked = checkForeignEntity(entityToImport,
                                                  fMetaInfo,
                                                  registry,
                                                  properties);
      }
      else if (type.equals(IMetaInfoConstants.TYPE_DYNAMIC) || type.equals(IMetaInfoConstants.TYPE_EMBEDDED))
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