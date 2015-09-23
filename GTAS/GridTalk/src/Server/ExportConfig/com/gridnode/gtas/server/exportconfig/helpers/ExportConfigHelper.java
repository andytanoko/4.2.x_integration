/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExportConfigHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 08 2003    Koh Han Sing        Created
 * Nov 10 2005    Neo Sok Lay         Use Localcontext to lookup ImportExportService
 */
package com.gridnode.gtas.server.exportconfig.helpers;

import java.io.File;
import java.util.*;

import com.gridnode.gtas.model.exportconfig.ConfigEntitiesContainer;
import com.gridnode.gtas.model.exportconfig.ConfigEntityDescriptor;
import com.gridnode.gtas.model.exportconfig.ConfigEntityList;
import com.gridnode.pdip.base.exportconfig.facade.ejb.IImportExportServiceLocalHome;
import com.gridnode.pdip.base.exportconfig.facade.ejb.IImportExportServiceLocalObj;
import com.gridnode.pdip.base.exportconfig.imports.ImportRegistry;
import com.gridnode.pdip.base.exportconfig.model.ImportEntity;
import com.gridnode.pdip.base.exportconfig.model.ImportEntityList;
import com.gridnode.pdip.framework.db.AbstractEntityHandler;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This class contains the logic for exporting the entities.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */
public class ExportConfigHelper
{
  /**
   * This method will retrieve from database a list of all exportable entities
   * and store them in a ConfigEntitiesContainer to be passed to the user to
   * select.
   */
  public static ConfigEntitiesContainer getExportableEntities()
    throws Exception
  {
    Logger.debug("[ExportConfigHelper.getExportableEntities] Start");

    ImportEntityList list = getImportExportBean().getExportableEntities();

    ConfigEntitiesContainer container = convert(list);

    Logger.debug("[ExportConfigHelper.getExportableEntities] End");
    return container;
  }

  /**
   * This method will serialize the entities in the ConfigEntitiesContainer
   * into a zip file.
   */
  public static File exportConfig(ConfigEntitiesContainer container)
    throws Exception
  {
    Logger.debug("[ExportConfigHelper.exportConfig] Start");

    ImportEntityList list = convert(container);

    File zipFile = getImportExportBean().exportConfig(list);

    Logger.debug("[ExportConfigHelper.exportConfig] End");
    return zipFile;
  }

  /**
   * This method will deserialize the entities from the config file. It will
   * then checks for conflicting entities. If there are conflicts, it will
   * return a ConfigEntitiesContainer containing the list of conflicts and
   * store the deserialized entities in memory, waiting for the user to
   * response to the conflicts. On the other hand, if there are no conflicts it
   * will persist the import entities and return an empty
   * ConfigEntitiesContainer.
   */
  public static List importConfig(File zipFile, boolean isOverwrite)
    throws Exception
  {
    Logger.debug("[ExportConfigHelper.importConfig] Start");

    ImportRegistry registry =
      getImportExportBean().prepareImport(zipFile, isOverwrite);
    ImportEntityList conflictList =  registry.getConflictList();
    ConfigEntitiesContainer conflictContainer = new ConfigEntitiesContainer();

    if (conflictList.getEntities().isEmpty() || isOverwrite)
    {
      Logger.debug("[ExportConfigHelper.importConfig] conflictContainer.isEmpty() = "
                    +conflictContainer.isEmpty()
                    +"   isOverwrite = "+isOverwrite);
      getImportExportBean().importConfig(registry);
    }
    else
    {
      conflictContainer = convert(conflictList);
    }
    ArrayList resultList = new ArrayList();
    resultList.add(conflictContainer);
    resultList.add(registry);

    Logger.debug("[ExportConfigHelper.importConfig] End");
    return resultList;
  }

  /**
   * The ConfigEntitiesContainer will contain the entities that the user wants
   * to be overwritten, a conflict list will be created from it. The
   * deserialized entites will be retrieved using the sessionId and then
   * persisted into database.
   */
  public static void resolveAndImport(ImportRegistry registry,
    ConfigEntitiesContainer overwriteList)
    throws Exception
  {
    registry = updateConflictList(registry, overwriteList);

    getImportExportBean().importConfig(registry);
  }


  /**
   * This methods searches all the entities for any foreign entities they have
   * but are not selected to export by the user. If there are any, they will be
   * added to the list to be exported.
   *
   * @param container the ConfigEntitiesContainer containing the entities
   *                  selected by the user
   * @return the ConfigEntitiesContainer after adding the foreign entities if
   *         any
   */
  private static ImportEntityList convert(ConfigEntitiesContainer container)
    throws Exception
  {
    ImportEntityList newlist = new ImportEntityList();
    Collection oldlist = container.getConfigEntityLists();
    for (Iterator i = oldlist.iterator(); i.hasNext(); )
    {
      ConfigEntityList configEntityList = (ConfigEntityList)i.next();
      String entityName = configEntityList.getEntityName();

      AbstractEntityHandler handler = getImportExportBean().getHandler(entityName);

      Collection descs = configEntityList.getConfigEntityDescriptors();
      for (Iterator j = descs.iterator(); j.hasNext(); )
      {
        ConfigEntityDescriptor desc = (ConfigEntityDescriptor)j.next();
        IEntity entity = handler.getEntityByKeyForReadOnly(desc.getUid());
        Logger.debug("[ExportConfigHelper.checkForeignEntity] entity uid "+desc.getUid());
        ImportEntity aEntity =
          new ImportEntity(entity, desc.getUid(), null, false, false);
        newlist.addImportEntity(aEntity);
      }
    }
    return newlist;

  }

  /**
   * This methods searches all the entities for any foreign entities they have
   * but are not selected to export by the user. If there are any, they will be
   * added to the list to be exported.
   *
   * @param container the ConfigEntitiesContainer containing the entities
   *                  selected by the user
   * @return the ConfigEntitiesContainer after adding the foreign entities if
   *         any
   */
  private static ConfigEntitiesContainer convert(ImportEntityList list)
    throws Exception
  {
    ConfigEntitiesContainer container = new ConfigEntitiesContainer();
    Hashtable table = list.getEntityTable();
    Enumeration enu = table.keys();
    while (enu.hasMoreElements())
    {
      String entityName = enu.nextElement().toString();
      Object importEntitiesObj = table.get(entityName);
      if (importEntitiesObj != null)
      {
        Collection importEntities = (Collection)importEntitiesObj;
        ConfigEntityList configList = new ConfigEntityList();
        configList.setEntityName(entityName);
        for (Iterator i = importEntities.iterator(); i.hasNext(); )
        {
          ImportEntity importEntity = (ImportEntity)i.next();
          IEntity entity = importEntity.getEntity();
          ConfigEntityDescriptor desc = getConfigEntityDescriptor(entity);
          configList.addConfigEntityDescriptor(desc);
        }
        container.addConfigEntityList(configList);
      }
    }
    return container;

  }

  private static ConfigEntityDescriptor getConfigEntityDescriptor(IEntity entity)
  {
    ConfigEntityDescriptor desc = new ConfigEntityDescriptor();
    desc.setUid(new Long(entity.getKey().toString()));
    desc.setDescription(entity.getEntityDescr());

    return desc;
  }

  /**
   * Update the conflict list with the entities that the user has choosen to
   * overwrite.
   *
   * @param overwriteList the ConfigEntitiesContainer containing entities
   *                      selected by the user to be overwritten.
   */
  private static ImportRegistry updateConflictList(ImportRegistry registry,
    ConfigEntitiesContainer overwriteList)
  {
    Collection configEntityLists = overwriteList.getConfigEntityLists();
    for (Iterator i = configEntityLists.iterator(); i.hasNext(); )
    {
      ConfigEntityList configEntityList = (ConfigEntityList)i.next();
      String entityName = configEntityList.getEntityName();
      Collection descs = configEntityList.getConfigEntityDescriptors();
      for (Iterator j = descs.iterator(); j.hasNext(); )
      {
        ConfigEntityDescriptor desc = (ConfigEntityDescriptor)j.next();
        Long oldUid = desc.getUid();
        ImportEntity impEntity =
          registry.getConflictList().getEntity(entityName, oldUid);
        if (impEntity != null)
        {
          impEntity.setIsOverwrite(true);
        }
      }
    }
    return registry;
  }


  /**
   * Obtain the EJBObject for the ImportExportServiceBean.
   *
   * @return The EJBObject to the ImportExportServiceBean.
   * @exception ServiceLookupException Error in look up.
   *
   * @since 2.0
   */
  private static IImportExportServiceLocalObj getImportExportBean()
    throws ServiceLookupException
  {
    return (IImportExportServiceLocalObj)ServiceLocator.instance(
      ServiceLocator.LOCAL_CONTEXT).getObj(
      IImportExportServiceLocalHome.class.getName(),
      IImportExportServiceLocalHome.class,
      new Object[0]);
  }
}