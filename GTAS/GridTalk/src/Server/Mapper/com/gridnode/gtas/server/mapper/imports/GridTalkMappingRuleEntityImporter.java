/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridTalkMappingRuleEntityImporter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 13 2003    Koh Han Sing        Created
 */
package com.gridnode.gtas.server.mapper.imports;

import java.util.Properties;

import com.gridnode.gtas.server.mapper.helpers.GridTalkMappingRuleEntityHandler;
import com.gridnode.gtas.server.mapper.helpers.Logger;
import com.gridnode.gtas.server.mapper.helpers.ServiceLookupHelper;
import com.gridnode.gtas.server.mapper.model.GridTalkMappingRule;
import com.gridnode.pdip.app.mapper.model.MappingRule;
import com.gridnode.pdip.base.exportconfig.exception.ImportConfigException;
import com.gridnode.pdip.base.exportconfig.imports.AbstractEntityImporter;
import com.gridnode.pdip.base.exportconfig.imports.ImportRegistry;
import com.gridnode.pdip.base.exportconfig.model.ImportEntity;
import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;
import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;
import com.gridnode.pdip.framework.db.meta.MetaInfoFactory;

/**
 * This class will know how to import a Alert entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class GridTalkMappingRuleEntityImporter extends AbstractEntityImporter
{

  private static GridTalkMappingRuleEntityImporter _self = null;

  private GridTalkMappingRuleEntityImporter()
  {
    super();
  }

  public static GridTalkMappingRuleEntityImporter getInstance()
  {
    if(_self == null)
    {
      synchronized(GridTalkMappingRuleEntityImporter.class)
      {
        if (_self == null)
        {
          _self = new GridTalkMappingRuleEntityImporter();
        }
      }
    }
    return _self;
  }

  public boolean checkFields(ImportEntity entityToImport,
    ImportRegistry registry)
    throws Exception
  {
    Logger.debug("[GridTalkMappingRuleEntityImporter.checkFields] Start");
    boolean noUnlinkForeignEntities = true;

    GridTalkMappingRule gtMr = (GridTalkMappingRule)entityToImport.getEntity();
    String entityName = gtMr.getEntityName();
    EntityMetaInfo metaInfo =
      MetaInfoFactory.getInstance().getMetaInfoFor(entityName);
    FieldMetaInfo[] fMetaInfos = metaInfo.getFieldMetaInfo();
    for (int j = 0; j < fMetaInfos.length; j++)
    {
      boolean isForeignFieldLinked = true;
      FieldMetaInfo fMetaInfo = fMetaInfos[j];
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
      Logger.debug("[GridTalkMappingRuleEntityImporter.checkFields] noUnlinkForeignEntities");
      entityToImport.updateEntity();
      gtMr = (GridTalkMappingRule)entityToImport.getEntity();
      try
      {
        if (entityToImport.getNewUid() != null)
        {
          Long uidToOverwrite = entityToImport.getNewUid();
          Logger.debug("[GridTalkMappingRuleEntityImporter.checkFields] uidToOverwrite ="+uidToOverwrite);
          GridTalkMappingRule oldGtMr = (GridTalkMappingRule)
            GridTalkMappingRuleEntityHandler.getInstance().getEntityByKeyForReadOnly(uidToOverwrite);
          Long oldMrUid = (Long)oldGtMr.getMappingRule().getKey();
          double oldMrVersion = oldGtMr.getMappingRule().getVersion();
          MappingRule mr = gtMr.getMappingRule();
          mr.setVersion(oldMrVersion);
          mr.setFieldValue(MappingRule.UID, oldMrUid);
          ServiceLookupHelper.getManager().updateMappingRule(mr);

          gtMr.setMappingRule(mr);
          double oldGtMrVersion = oldGtMr.getVersion();
          gtMr.setVersion(oldGtMrVersion);
          gtMr.setFieldValue(GridTalkMappingRule.UID, uidToOverwrite);

          GridTalkMappingRuleEntityHandler.getInstance().update(gtMr);
          gtMr = (GridTalkMappingRule)GridTalkMappingRuleEntityHandler.getInstance().getEntityByKey(uidToOverwrite);
        }
        else
        {
          MappingRule mr = gtMr.getMappingRule();
          mr = ServiceLookupHelper.getManager().createMappingRule(mr);
          gtMr.setMappingRule(mr);
          gtMr = (GridTalkMappingRule)GridTalkMappingRuleEntityHandler.getInstance().createEntity(gtMr);
        }
      }
      catch (Throwable ex)
      {
        throw new ImportConfigException("Error saving Alert", ex);
      }

      ImportEntity persistedEntity = new ImportEntity(
                                           gtMr,
                                           (Long)entityToImport.getOldUid(),
                                           (Long)gtMr.getKey(),
                                           false,
                                           false);
      registry.getPersistedList().addImportEntity(persistedEntity);
      registry.getImportList().removeImportEntity(entityToImport);
      registry.getConflictList().removeImportEntity(entityToImport);
      return true;
    }
    else
    {
      Logger.debug("[GridTalkMappingRuleEntityImporter.checkFields] got UnlinkForeignEntities");
    }
    return false;
  }

}