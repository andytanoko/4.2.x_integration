/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityExportLogic.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 08 2003    Koh Han Sing        Created
 * Oct 17 2005    Neo Sok Lay         For JDK1.5 compliance: reflection invocation
 *                                    must explicit cast for null args.
 * Feb 09 2007		Alain Ah Ming				Add error code in error log or use warning log                                   
 */
package com.gridnode.pdip.base.exportconfig.helpers;

import com.gridnode.pdip.base.exportconfig.exports.AbstractEntityExporter;
import com.gridnode.pdip.base.exportconfig.exports.DefaultEntityExporter;
import com.gridnode.pdip.base.exportconfig.exports.ExportRegistry;
import com.gridnode.pdip.base.exportconfig.model.ImportEntity;
import com.gridnode.pdip.base.exportconfig.model.ImportEntityList;

import com.gridnode.pdip.framework.db.AbstractEntityHandler;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;
import com.gridnode.pdip.framework.db.meta.MetaInfoFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

/**
 * This class contains the logic for exporting the entities.
 *
 * @author Koh Han Sing
 *
 * @version GT 4.0 VAN
 * @since 2.1 I1
 */
public class EntityExportLogic
{
  /**
   * This method will retrieve from database a list of all exportable entities
   * and store them in a ConfigEntitiesContainer to be passed to the user to
   * select.
   */
  public static ImportEntityList getExportableEntities()
    throws Exception
  {
    Logger.debug("[ExportConfigLogic.getExportableEntities] Start");
    ImportEntityList importEntityList = new ImportEntityList();
    Collection entities =
      ExportConfigLoader.getInstance().getSelectableEntitiesName();
    for (Iterator i = entities.iterator(); i.hasNext(); )
    {
      String entityName = i.next().toString();
      Collection importEntities = getImportEntitiesForExport(entityName);
      if (!importEntities.isEmpty())
      {
        importEntityList.addEntities(importEntities, entityName);
      }
    }

    return importEntityList;
  }

  /**
   * This method will serialize the entities in the ConfigEntitiesContainer
   * into a zip file.
   */
  public static File exportConfig(ImportEntityList exportEntityList)
    throws Exception
  {
    ExportRegistry registry = new ExportRegistry(exportEntityList);
    registry = getForeignEntities(exportEntityList, registry);
    registry.updateExportListWithForeignEntities();
    exportEntityList = registry.getExportList();

    EntitySerializeVisitor visitor = new EntitySerializeVisitor();
    Hashtable entityTable = exportEntityList.getEntityTable();
    Enumeration enu = entityTable.keys();
    while(enu.hasMoreElements())
    {
      String entityName = enu.nextElement().toString();
      Collection exportEntities = (Collection)entityTable.get(entityName);
      // Export a type of entity one by one
      Logger.debug("[ExportConfigLogic.exportConfig] Export entityType : "+entityName);
      exportEntity(exportEntities, visitor);
    }

    File zipFile = visitor.zip();
    return zipFile;
  }

  /**
   * This method returns a collection of foreign entities found in the entity
   * passed it.
   *
   * @param entity The entity whose foreign entities are to be retrieved.
   * @returns an Collection of IEntity. Empty is no foreign entities are found.
   */
  public static ExportRegistry getAllForeignEntities(IEntity entity,
    ExportRegistry registry)
    throws Exception
  {
    String entityName = entity.getEntityName();
    Logger.debug("[ExportConfigHelper.getAllForeignEntities] Find all foreign entities for : "+entity.getEntityDescr());

    AbstractEntityExporter exporter = null;
    try
    {
      String entityClassName = entity.getClass().getName();
      String modulePackage =
        entityClassName.substring(0,
          entityClassName.indexOf(IMetaInfoConstants.PACKAGE_MODEL_DOT+entityName));
      String exporterClassName =
        modulePackage+IMetaInfoConstants.PACKAGE_EXPORTS_DOT+entityName+IMetaInfoConstants.CLASS_ENTITY_EXPORTER;
      Logger.debug("[ExportConfigHelper.getAllForeignEntities] Loading exporter : "+exporterClassName);
      Class exportClass = Class.forName(exporterClassName);
      exporter = (AbstractEntityExporter)
        exportClass.getDeclaredMethod(IMetaInfoConstants.MTD_GET_INSTANCE, (Class[])null).invoke(null, (Object[])null);
    }
    catch (ClassNotFoundException ex)
    {
      Logger.debug("[ExportConfigHelper.getAllForeignEntities] Exporter not found using DefaultEntityExporter");
      exporter = DefaultEntityExporter.getInstance();
    }
    registry = exporter.getAllForeignEntities(entity, registry);

    return registry;
  }

  /**
   * This methods searches all the entities for any foreign entities they have
   * but are not selected to export by the user. If there are any, they will be
   * added to the list to be exported.
   *
   * @param exportList the ConfigEntitiesContainer containing the entities
   *                   selected by the user
   * @return the ConfigEntitiesContainer after adding the foreign entities if
   *         any
   */
  private static ExportRegistry getForeignEntities(ImportEntityList exportList,
    ExportRegistry registry)
    throws Exception
  {
    Logger.debug("[ExportConfigHelper.getForeignEntities] Start");

    Collection lists = exportList.getEntities();
    for (Iterator i = lists.iterator(); i.hasNext(); )
    {
      ImportEntity exportEntity = (ImportEntity)i.next();
      IEntity entityToExport = (IEntity)exportEntity.getEntity();
      registry = getAllForeignEntities(entityToExport, registry);
    }

    Logger.debug("[ExportConfigHelper.getForeignEntities] End");
    return registry;
  }



  public IEntity getEntity(String entityName, Long uid)
    throws Exception
  {
    AbstractEntityHandler handler = EntityHandlerHelper.getHandler(entityName);
    IEntity entity = handler.getEntityByKeyForReadOnly(uid);
    return entity;
  }

  private static Collection getImportEntitiesForExport(String entityName)
    throws Exception
  {
    ArrayList importEntities = new ArrayList();

    try
    {
      AbstractEntityHandler handler = EntityHandlerHelper.getHandler(entityName);

      EntityMetaInfo metaInfo =
        MetaInfoFactory.getInstance().getMetaInfoFor(entityName);
      String objectClassName = metaInfo.getObjectName();
      String packageName =
        objectClassName.substring(0, objectClassName.indexOf(entityName)-1);
      Class interfaceClass = Class.forName(packageName+IMetaInfoConstants.DOT_I+entityName);

      IDataFilter filter = new DataFilterImpl();

      try
      {
        filter.addSingleFilter(
          null,
          interfaceClass.getField(IMetaInfoConstants.FIELD_CAN_DELETE).get(null),
          filter.getEqualOperator(),
          Boolean.TRUE,
          false);
      }
      catch (NoSuchFieldException ex)
      {
        filter.addSingleFilter(
          null,
          interfaceClass.getField(IMetaInfoConstants.FIELD_UID).get(null),
          filter.getEqualOperator(),
          null,
          true);
      }

      Collection entities = handler.getEntityByFilterForReadOnly(filter);

      for (Iterator i = entities.iterator(); i.hasNext(); )
      {
        IEntity entity = (IEntity)i.next();
        ImportEntity importEntity =
          new ImportEntity(entity, (Long)entity.getKey(), null, false, false);
        importEntities.add(importEntity);
      }
    }
    catch (Exception ex)
    {
      Logger.warn("[ExportConfigLogic.getConfigEntityList] Exception", ex);
      throw ex;
    }
    return importEntities;
  }

  private static void exportEntity(Collection exportEntities,
                                   EntitySerializeVisitor visitor)
                                   throws Exception
  {
    ArrayList entitiesToExport = new ArrayList();
    for (Iterator i = exportEntities.iterator(); i.hasNext(); )
    {
      ImportEntity exportEntity = (ImportEntity)i.next();
      IEntity entity = exportEntity.getEntity();
      entitiesToExport.add(entity);
    }
    EntitySerializeDecorator decorator =
      new EntitySerializeDecorator(entitiesToExport);
    decorator.accepts(visitor);
  }

}