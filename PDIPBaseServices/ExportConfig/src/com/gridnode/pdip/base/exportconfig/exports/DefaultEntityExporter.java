/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultEntityExporter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 02 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.base.exportconfig.exports;

import com.gridnode.pdip.base.exportconfig.helpers.EntityExportLogic;
import com.gridnode.pdip.base.exportconfig.helpers.IMetaInfoConstants;
import com.gridnode.pdip.base.exportconfig.helpers.Logger;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;
import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;
import com.gridnode.pdip.framework.db.meta.MetaInfoFactory;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

/**
 * This class is the default exporter used to retrieve other foreign entities
 * use by a entity.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.3.3
 * @since 2.1 I1
 */

public class DefaultEntityExporter extends AbstractEntityExporter
{
  private static DefaultEntityExporter _self = null;

  private DefaultEntityExporter()
  {
    super();
  }

  public static DefaultEntityExporter getInstance()
  {
    if(_self == null)
    {
      synchronized(DefaultEntityExporter.class)
      {
        if (_self == null)
        {
          _self = new DefaultEntityExporter();
        }
      }
    }
    return _self;
  }

  /**
   * This method finds all foreign entities in the specified entity and updated
   * the registry.
   *
   * @param entity The entity whose foreign entities are to be retrieved.
   * @returns an ExportRegistry containing the foreign entities used by the
   *          specified entity.
   */
  public ExportRegistry getAllForeignEntities(IEntity entity, ExportRegistry registry)
    throws Exception
  {
    Logger.debug("[DefaultEntityExporter.getAllForeignEntities] Find all foreign entities for : "+entity.getEntityDescr());

    EntityMetaInfo metaInfo =
      MetaInfoFactory.getInstance().getMetaInfoFor(entity.getEntityName());
    FieldMetaInfo[] fMetaInfos = metaInfo.getFieldMetaInfo();
    for (int j = 0; j < fMetaInfos.length; j++)
    {
      FieldMetaInfo fMetaInfo = fMetaInfos[j];
      Properties properties = fMetaInfo.getConstraints();
      String type = properties.getProperty(IMetaInfoConstants.CONSTRAINT_TYPE, IMetaInfoConstants.ESTR);
      if (type.equals(IMetaInfoConstants.TYPE_FOREIGN))
      {
        Collection foreignTypeEntites = getForeignTypeEntities(entity, fMetaInfo);
        for (Iterator i = foreignTypeEntites.iterator(); i.hasNext();)
        {
          IEntity foreignTypeEntity = (IEntity)i.next();
          Logger.debug("[DefaultEntityExporter.getAllForeignEntities] found type foreign foreignTypeEntity "+foreignTypeEntity.getEntityDescr());
          registry = EntityExportLogic.getAllForeignEntities(foreignTypeEntity, registry);
          registry = addEntityToForeignList(foreignTypeEntity, registry);
        }
      }
      else if (type.equals(IMetaInfoConstants.TYPE_DYNAMIC) || type.equals(IMetaInfoConstants.TYPE_EMBEDDED))
      {
        Collection dembeddedEntites = getEmbeddedEntities(entity, fMetaInfo);
        for (Iterator i = dembeddedEntites.iterator(); i.hasNext();)
        {
          IEntity embeddedEntity = (IEntity)i.next();
          Logger.debug("[DefaultEntityExporter.getAllForeignEntities] found type dynamic or embedded embeddedEntity "+embeddedEntity.getEntityDescr());
          registry = EntityExportLogic.getAllForeignEntities(embeddedEntity, registry);
        }
      }
    }
    return registry;
  }

}