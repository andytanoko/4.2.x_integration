/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EntityImportLogic.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 28 2003    Koh Han Sing        Created
 * Jan 12 2004    Mahesh              Added method to modify schema uri
 * Oct 27 2005    Neo Sok Lay         Load mapping using System.workingDir
 * Feb 08 2007    Neo Sok Lay         Modify patchSchema(): do not use file
 *                                    renameTo, as it's behaviour is platform-dependent. 
 */
package com.gridnode.pdip.base.exportconfig.helpers;

import java.io.*;
import java.util.Collection;
import java.util.Iterator;

import org.exolab.castor.mapping.Mapping;

import com.gridnode.pdip.base.exportconfig.exception.ImportConfigException;
import com.gridnode.pdip.base.exportconfig.imports.AbstractEntityImporter;
import com.gridnode.pdip.base.exportconfig.imports.DefaultEntityImporter;
import com.gridnode.pdip.base.exportconfig.imports.ImportRegistry;
import com.gridnode.pdip.base.exportconfig.model.ExportEntityList;
import com.gridnode.pdip.base.exportconfig.model.ImportEntity;
import com.gridnode.pdip.framework.db.AbstractEntityHandler;
import com.gridnode.pdip.framework.db.XmlObjectDeserializer;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;
import com.gridnode.pdip.framework.util.SystemUtil;

/**
 * This class persist the imported entities into the database.
 *
 * @author Koh Han Sing
 *
 * @version GT 2.3.3
 * @since 2.1 I1
 */

public class EntityImportLogic
{

  /**
   * This method will deserialize the entities from the config file. It will
   * then checks for conflicting entities. If there are conflicts, it will
   * return a ConfigEntitiesContainer containing the list of conflicts and
   * store the deserialized entities in memory, waiting for the user to
   * response to the conflicts. On the other hand, if there are no conflicts it
   * will persist the import entities and return an empty
   * ConfigEntitiesContainer.
   */
  public static ImportRegistry prepareImport(File zipFile, boolean isOverwrite)
    throws Exception
  {
    Logger.debug("[EntityImportLogic.prepareImport] Start");
    File unzipDir = unzipConfigFile(zipFile);

    //ArrayList resultList = new ArrayList();
    ImportRegistry registry = new ImportRegistry();
    registry.setUnzipDir(unzipDir);
    File[] entityFiles = unzipDir.listFiles();
    for (int j = 0; j < entityFiles.length; j++)
    {
      File entityXMLFile = entityFiles[j];
      if (entityXMLFile.isDirectory())
      {
        continue;
      }
      patchSchema(entityXMLFile);
      Collection entities = deserializeEntities(entityXMLFile);
      for (Iterator i = entities.iterator(); i.hasNext(); )
      {
        IEntity entity = (IEntity)i.next();
        IEntity duplicate = checkConflict(entity);
        if (duplicate != null)
        {
          ImportEntity conflictEntity = new ImportEntity(entity,
            (Long)entity.getKey(), (Long)duplicate.getKey(), isOverwrite, false);
          if (EntityRelationshipHelper.canDelete(duplicate))
          {
            registry.getConflictList().addImportEntity(conflictEntity);
            registry.getImportList().addImportEntity(conflictEntity);
          }
          else
          {
            Logger.debug("[EntityImportLogic.prepareImport] Entity "+
              conflictEntity.getEntity().getEntityName()+
              " cannot overwrite an existing entity");
            registry.getPersistedList().addImportEntity(conflictEntity);
          }
          continue;
        }

        ImportEntity importEntity =
          new ImportEntity(entity, (Long)entity.getKey(), null, false, false);
        registry.getImportList().addImportEntity(importEntity);
      }
    }

    Logger.debug("[EntityImportLogic.prepareImport] End");
    return registry;
  }

  /**
   * Persist the entities in the import list into the database
   */
  public static void importConfig(ImportRegistry registry) throws Exception
  {
    Logger.debug("[EntityImportLogic.importConfig] Start");
    Collection importEntites = registry.getImportList().getEntities();
    Logger.debug("[EntityImportLogic.importConfig] importEntites.size() :"+importEntites.size());
    int maxIteration = importEntites.size();
    int counter = 0;
    while (!importEntites.isEmpty())
    {
      counter++;
      if (counter > maxIteration)
      {
        throw new ImportConfigException("Unable to complete import process, please check log files for details");
      }

      Logger.debug("[EntityImportLogic.importConfig] !importEntites.isEmpty()");

      for (Iterator i = importEntites.iterator(); i.hasNext(); )
      {
        ImportEntity entityToImport = (ImportEntity)i.next();
        if (registry.getImportList().getEntity(entityToImport.getEntity()) == null)
        {
          // if the entity is no longer in the importlist, dun process, cause the
          // iterator contains entities that may have already been processed when
          // their parent entities are processed.
          continue;
        }
        String entityName = entityToImport.getEntity().getEntityName();
        Logger.debug("[EntityImportLogic.importConfig] entityName = "+entityName);
        Logger.debug("[EntityImportLogic.importConfig] ImportList = "+registry.getImportList().toString());
        Logger.debug("[EntityImportLogic.importConfig] ConflictList = "+registry.getConflictList().toString());
        Logger.debug("[EntityImportLogic.importConfig] PersistedList = "+registry.getPersistedList().toString());
        ImportEntity conflictEntity =
          registry.getConflictList().getEntity(entityName, entityToImport.getOldUid());
        if (conflictEntity != null)
        {
          Logger.debug("[EntityImportLogic.importConfig] conflictEntity.isOverwrite() ="+conflictEntity.isOverwrite());
          if (!conflictEntity.isOverwrite())
          {
            Logger.debug("[EntityImportLogic.importConfig] dun overwrite so load from database");
             // Don't overwrite so load existing one from database
            ImportEntity loadedEntity = loadEntityFromDB(conflictEntity);
            registry.getPersistedList().addImportEntity(loadedEntity);
            registry.getImportList().removeImportEntity(entityToImport);
            registry.getConflictList().removeImportEntity(entityToImport);
            removeDependency(entityToImport, registry);
            continue;
          }
        }
        checkFields(entityToImport, registry);
      }

      importEntites = registry.getImportList().getEntities();
      Logger.debug("[EntityImportLogic.importConfig] importEntites.isEmpty() = "+importEntites.isEmpty());
    }
    Logger.debug("[EntityImportLogic.importConfig] End");
  }


  /**
   * This method will be called when there is a conflict and the user choose
   * not to overwrite the existing entity, therefore we will need to remove
   * away all the dependent entites that ties only to this entity.
   *
   * This method will attempt to find the EntityImporter class of the import
   * entity. If found it will trigger the removeDependency method of that
   * EntityImporter to remove away any dependency entities in the import list.
   */
  public static void removeDependency(ImportEntity entityToImport,
    ImportRegistry registry)
    throws Exception
  {
    String entityName = entityToImport.getEntity().getEntityName();
    AbstractEntityImporter importer = null;
    try
    {
      String entityClassName = entityToImport.getEntity().getClass().getName();
      String modulePackage = entityClassName.substring(0,
                                entityClassName.indexOf(IMetaInfoConstants.PACKAGE_MODEL_DOT+entityName));
      String importerClassName =
        modulePackage+IMetaInfoConstants.PACKAGE_IMPORTS_DOT+entityName+IMetaInfoConstants.CLASS_ENTITY_IMPORTER;
      Logger.debug("[EntityImportLogic.checkFields] Loading importer : "+importerClassName);
      Class importClass = Class.forName(importerClassName);
      importer = (AbstractEntityImporter)
        importClass.getDeclaredMethod(IMetaInfoConstants.MTD_GET_INSTANCE, (Class[])null).invoke(null, (Object[])null);
      importer.removeDependency(entityToImport, registry);
    }
    catch (ClassNotFoundException ex)
    {
      Logger.debug("[EntityImportLogic.removeDependency] Importer not found, don't have to remove dependencies");
    }
  }

  /**
   * This method will attempt to find the EntityImporter of the import entity,
   * if found, it will trigger the checkFields method to perform special
   * handling that is unique to that entity and cannot be handle generically.
   */
  public static boolean checkFields(ImportEntity entityToImport,
    ImportRegistry registry)
    throws Exception
  {
    String entityName = entityToImport.getEntity().getEntityName();
    AbstractEntityImporter importer = null;
    try
    {
      String entityClassName = entityToImport.getEntity().getClass().getName();
      String modulePackage = entityClassName.substring(0,
                                entityClassName.indexOf(IMetaInfoConstants.PACKAGE_MODEL_DOT+entityName));
      String importerClassName =
        modulePackage+IMetaInfoConstants.PACKAGE_IMPORTS_DOT+entityName+IMetaInfoConstants.CLASS_ENTITY_IMPORTER;
      Logger.debug("[EntityImportLogic.checkFields] Loading importer : "+importerClassName);
      Class importClass = Class.forName(importerClassName);
      importer = (AbstractEntityImporter)
        importClass.getDeclaredMethod(IMetaInfoConstants.MTD_GET_INSTANCE, (Class[])null).invoke(null, (Object[])null);
    }
    catch (ClassNotFoundException ex)
    {
      Logger.debug("[EntityImportLogic.checkFields] Importer not found using DefaultEntityImporter");
      importer = DefaultEntityImporter.getInstance();
    }
    return importer.checkFields(entityToImport, registry);
  }

  private static ImportEntity loadEntityFromDB(ImportEntity conflictEntity)
    throws Exception
  {
    try
    {
      String entityName = conflictEntity.getEntity().getEntityName();
      Long uid = conflictEntity.getNewUid(); //uid of conflict in database

      AbstractEntityHandler handler = EntityHandlerHelper.getHandler(entityName);
      IEntity conflictEntityFromDB = handler.getEntityByKeyForReadOnly(uid);
      conflictEntity.setEntity(conflictEntityFromDB);

      return conflictEntity;
    }
    catch (Throwable ex)
    {
      throw new ImportConfigException("Error loading entity from database", ex);
    }
  }

  private static File unzipConfigFile(File configFile)
    throws Exception
  {
    File tempDir = FileUtil.getFile(IPathConfig.PATH_TEMP, "");
    if (tempDir != null)
    {
      try
      {
        String subDir = String.valueOf(System.currentTimeMillis());
        File unzipToDir = new File(tempDir, subDir);
        unzipToDir.mkdir();
        ZipHelper.unzip(configFile, unzipToDir);
        return unzipToDir;
      }
      catch (Exception ex)
      {
        throw new ImportConfigException("Error unzipping configuration file", ex);
      }
    }
    else
    {
      throw new ImportConfigException("Unable to access configuration file, temp directory not found");
    }
  }

   private static Collection deserializeEntities(File entityXMLFile)
    throws Exception
  {
    Logger.debug("[EntityImportLogic.deserializeEntities] Start ");

    String filename = entityXMLFile.getName();
    Logger.debug("[EntityImportLogic.deserializeEntities] importing filename = "+filename);
    String entityName = filename.substring(0, filename.indexOf("."));
    Logger.debug("[EntityImportLogic.deserializeEntities] importing entityName = "+entityName);
    String map = ExportConfigLoader.getInstance().getMapping(entityName);

    File mapFile = new File(SystemUtil.getWorkingDirPath(), map); //NSL20051027
    Logger.debug("[EntityImportLogic.deserializeEntities] mapFile URL ="+mapFile.toURL());
    if (!mapFile.exists())
    {
      throw new ImportConfigException("map file "+map+
        " for entity "+entityName+" does not exists");
    }

    Mapping mapping = new Mapping();
    mapping.loadMapping(mapFile.toURL());
    XmlObjectDeserializer deserializer = new XmlObjectDeserializer();
    ExportEntityList entityList =
      (ExportEntityList)deserializer.deserialize(ExportEntityList.class,
                                                 entityXMLFile.getAbsolutePath(),
                                                 mapping);
    Collection entities = entityList.getEntities();
    Logger.debug("[EntityImportLogic.deserializeEntities] End ");
    return entities;
  }

  private static IEntity checkConflict(IEntity entity)
    throws Exception
  {
    IEntity duplicateEntity = null;

    String className = entity.getClass().getName();
    Logger.debug("[EntityImportLogic.checkConflict] className = "+className);
    String packageName = className.substring(0, className.lastIndexOf(IMetaInfoConstants.PACKAGE_MODEL));
    String helperClassName =
      packageName+IMetaInfoConstants.PACKAGE_HELPERS_DOT+entity.getEntityName()+IMetaInfoConstants.CLASS_ENTITY_HELPER;
    Logger.debug("[EntityImportLogic.checkConflict] helperClassName ="+helperClassName+"=");
    try
    {
      Class helperClass = Class.forName(helperClassName);
      ICheckConflict helper = (ICheckConflict)
        helperClass.getDeclaredMethod(IMetaInfoConstants.MTD_GET_INSTANCE, (Class[])null).invoke(null, (Object[])null);
      IEntity entityObj = helper.checkDuplicate(entity);
      if (entityObj != null)
      {
        duplicateEntity = (IEntity)entityObj;
      }
    }
    catch (Exception ex)
    {
      //no checkDuplicate class/method found
      Logger.debug("[EntityImportLogic.checkConflict] "+helperClassName+" not found, no performing duplcate check");
    }
    return duplicateEntity;
  }

  private static void patchSchema(File xmlFile)
	{
		if(xmlFile!=null && xmlFile.exists() && xmlFile.getName().endsWith(".xml"))
		{
			try
			{
				File tempFile =  File.createTempFile("patch",".tmp");
				PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(tempFile)));
				BufferedReader br=new BufferedReader(new FileReader(xmlFile));
				String str =null;
				while((str=br.readLine())!=null)
				{
					str= str.replaceAll("http://www.w3.org/1999/XMLSchema-instance","http://www.w3.org/2001/XMLSchema-instance");
					System.out.println(str);
					out.println(str);
				}
				br.close();
				out.close();
        
        //NSL20070208
        out = new PrintWriter(new BufferedWriter(new FileWriter(xmlFile)));
        br=new BufferedReader(new FileReader(tempFile));
        while((str=br.readLine())!=null)
        {
          out.println(str);
        }
        out.close();
        br.close();
				//xmlFile.delete();
				//tempFile.renameTo(xmlFile);
				tempFile.delete();
			}
			catch(Exception ex)
			{
				Logger.debug("[EntityImportLogic.patchSchema] Error while renaming schema",ex);
			}
		}
	}

}