/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractEntityExporter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 02 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.base.exportconfig.exports;

import com.gridnode.pdip.base.exportconfig.helpers.EntityRelationshipHelper;
import com.gridnode.pdip.base.exportconfig.helpers.IMetaInfoConstants;
import com.gridnode.pdip.base.exportconfig.helpers.Logger;
import com.gridnode.pdip.base.exportconfig.model.ImportEntity;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;

/**
 * This class will serialize a collection of entities into a XML file.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.3.3
 * @since 2.1 I1
 */

public abstract class AbstractEntityExporter
{

  /**
   * This method returns a collection of foreign entities found in the entity
   * passed it.
   *
   * @param entity The entity whose foreign entities are to be retrieved.
   * @returns an Collection of IEntity. Empty is no foreign entities are found.
   */
  public abstract ExportRegistry getAllForeignEntities(IEntity entity, ExportRegistry registry)
    throws Exception;


  protected Collection getForeignTypeEntities(IEntity entity,
    FieldMetaInfo fMetaInfo)
    throws Exception
  {
    Logger.debug("[AbstractEntityExporter.getForeignTypeEntities] For Entity ="+entity.getEntityDescr()+
      " From field ="+fMetaInfo.getFieldName());
    ArrayList foreignEntities = new ArrayList();
    Properties properties = fMetaInfo.getConstraints();
    //Object fieldValue = entity.getFieldValue(fMetaInfo.getFieldId());
 
    String cached = properties.getProperty(IMetaInfoConstants.FOREIGN_CACHED, IMetaInfoConstants.FALSE);
    if (!cached.equals(IMetaInfoConstants.TRUE))
    {
      Logger.debug("[AbstractEntityExporter.getForeignTypeEntities] Field is non-cached");
      Collection foreigns = getNonCachedForeignEntities(entity, fMetaInfo);
      foreignEntities.addAll(foreigns);
    }
    else
    {
      Logger.debug("[AbstractEntityExporter.getForeignTypeEntities] Field is cached");
      Collection foreigns = getCachedForeignEntities(entity, fMetaInfo);
      foreignEntities.addAll(foreigns);
    }

    return foreignEntities;
  }

  protected Collection getEmbeddedEntities(IEntity entity,
    FieldMetaInfo fMetaInfo)
    throws Exception
  {
    Collection embeddedEntities = new ArrayList();

    Object embeddedObj = entity.getFieldValue(fMetaInfo.getFieldId());
    if (embeddedObj != null)
    {
      if (embeddedObj instanceof Collection)
      {
        embeddedEntities.addAll((Collection)embeddedObj);
      }
      else
      {
        embeddedEntities.add((IEntity)embeddedObj);
      }
    }
    return embeddedEntities;
  }

  /**
   * This method checks whether the entity type requires special handling to
   * retrieve its foreign entities.
   */
  protected ExportRegistry addEntityToForeignList(IEntity entity, ExportRegistry registry)
    throws Exception
  {
    if (registry.getExportList().getEntity(entity) == null)
    {
      if (registry.getForeignList().getEntity(entity) == null)
      {
        ImportEntity foreignEntity =
          new ImportEntity(entity, (Long)entity.getKey(), null, false, false);
        registry.getForeignList().addImportEntity(foreignEntity);
      }
    }
    return registry;
  }

  protected Collection getCachedForeignEntities(IEntity entity, FieldMetaInfo fMetaInfo)
    throws Exception
  {
    Logger.debug("[AbstractEntityExporter.getCachedForeignEntities] Starts");
    Collection foreignEntities = new ArrayList();

    Object foreignEntityObj = entity.getFieldValue(fMetaInfo.getFieldId());
    if (foreignEntityObj != null)
    {
      if (foreignEntityObj instanceof Collection)
      {
        Collection foreigns = (Collection)foreignEntityObj;
        for (Iterator fe = foreigns.iterator(); fe.hasNext(); )
        {
          IEntity foreignEntity = (IEntity)fe.next();
          foreignEntities.add(foreignEntity);
        }
      }
      else
      {
        IEntity foreignEntity = (IEntity)foreignEntityObj;
        foreignEntities.add(foreignEntity);
      }
    }
    Logger.debug("[AbstractEntityExporter.getCachedForeignEntities] End");
    return foreignEntities;
  }

  protected Collection getNonCachedForeignEntities(IEntity entity,
    FieldMetaInfo fMetaInfo)
    throws Exception
  {
    Logger.debug("[AbstractEntityExporter.getNonCachedForeignEntities] Starts");
    ArrayList foreignEntities = new ArrayList();
    Object result =
      EntityRelationshipHelper.getNonCachedForeignEntitiesFromField(
        entity, fMetaInfo.getFieldId());
    if (result != null)
    {
      if (result instanceof Collection)
      {
        foreignEntities.addAll((Collection)result);
      }
      else
      {
        foreignEntities.add(result);
      }
    }
    return foreignEntities;
  }

}