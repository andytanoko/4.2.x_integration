/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractEntityImporter.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 03 2003    Koh Han Sing        Created
 * Dec 16 2003    Koh Han Sing        GNDB00016308 : Importing of FTP
 *                                    configuration throws a RunTime Exception
 * May 18 2004    Neo Sok Lay         Handle file.subPath constraint.
 *                                    Refactor.
 */
package com.gridnode.pdip.base.exportconfig.imports;

import com.gridnode.pdip.base.exportconfig.exception.ImportConfigException;
import com.gridnode.pdip.base.exportconfig.helpers.*;
import com.gridnode.pdip.base.exportconfig.helpers.EntityImportLogic;
import com.gridnode.pdip.base.exportconfig.helpers.EntityRelationshipHelper;
import com.gridnode.pdip.base.exportconfig.helpers.EntityHandlerHelper;
import com.gridnode.pdip.base.exportconfig.helpers.ExportConfigLoader;
import com.gridnode.pdip.base.exportconfig.helpers.Logger;
import com.gridnode.pdip.base.exportconfig.model.ImportEntity;

import com.gridnode.pdip.framework.db.AbstractEntityHandler;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.meta.EntityMetaInfo;
import com.gridnode.pdip.framework.db.meta.FieldMetaInfo;
import com.gridnode.pdip.framework.db.meta.MetaInfoFactory;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;

import java.io.File;
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

public abstract class AbstractEntityImporter
{

  public abstract boolean checkFields(
    ImportEntity entityToImport,
    ImportRegistry registry)
    throws Exception;

  /**
   * This method is use to remove any dependent entities from the importlist
   * if the parent entity is not going to be imported. Only entities that are
   * use solely by this parent entity can be removed.
   */
  public void removeDependency(
    ImportEntity entityToImport,
    ImportRegistry registry)
    throws Exception
  {
  }

  protected ImportEntity loadEntityFromDB(ImportEntity conflictEntity)
    throws Exception
  {
    try
    {
      String entityName = conflictEntity.getEntity().getEntityName();
      Long uid = conflictEntity.getNewUid(); //uid of conflict in database

      AbstractEntityHandler handler =
        EntityHandlerHelper.getHandler(entityName);
      IEntity conflictEntityFromDB = handler.getEntityByKeyForReadOnly(uid);
      conflictEntity.setEntity(conflictEntityFromDB);

      return conflictEntity;
    }
    catch (Throwable ex)
    {
      throw new ImportConfigException("Error loading entity from database", ex);
    }
  }

  protected boolean checkForeignEntity(
    ImportEntity entityToImport,
    FieldMetaInfo fMetaInfo,
    ImportRegistry registry,
    Properties properties)
    throws Exception
  {
    Logger.debug("[AbstractEntityImporter.checkForeignEntity] type foreign");
    boolean isForeignFieldLinked = false;
    Number foreignField = fMetaInfo.getFieldId();
    String cache =
      properties.getProperty(
        IMetaInfoConstants.FOREIGN_CACHED,
        IMetaInfoConstants.FALSE);
    if (cache.equals(IMetaInfoConstants.TRUE))
    {
      Logger.debug("[AbstractEntityImporter.checkForeignEntity] cache true");
      Object foreignObj =
        entityToImport.getEntity().getFieldValue(foreignField);
      if ((foreignObj != null) && (!foreignObj.toString().equals(IMetaInfoConstants.ESTR)))
      {
        if (foreignObj instanceof Collection)
        {
          Collection foreignObjs = (Collection) foreignObj;
          isForeignFieldLinked =
            processForeignCachedCollection(
              foreignObjs,
              entityToImport,
              foreignField,
              registry);
        }
        else
        {
          IEntity foreignEntity = (IEntity) foreignObj;
          isForeignFieldLinked =
            processForeignCachedEntity(
              foreignEntity,
              entityToImport,
              foreignField,
              registry);
        }
      }
      else
      {
        isForeignFieldLinked = true;
      }
    }
    else
    {
      Logger.debug("[AbstractEntityImporter.checkForeignEntity] cache false");
      // Get the FieldMetaInfo of the field stated by the label in foreign.key in
      // the Constraints field of the import entity
      FieldMetaInfo foreignKeyField =
        EntityRelationshipHelper.getForeignKeyField(fMetaInfo);
      boolean exportable =
        ExportConfigLoader.getInstance().isExportable(
          foreignKeyField.getEntityName());
      if (exportable)
      {

        Object foreignKeyObj =
          entityToImport.getEntity().getFieldValue(foreignField);
        //Logger.debug(
        //  "[AbstractEntityImporter.checkForeignEntity] foreignKeyObj ="
        //    + foreignKeyObj
        //    + "=");
        if ((foreignKeyObj != null) && (!foreignKeyObj.toString().equals(IMetaInfoConstants.ESTR)))
        {
          if (foreignKeyObj instanceof Collection)
          {
            Collection foreignKeys = (Collection) foreignKeyObj;
            isForeignFieldLinked =
              processForeignNonCachedCollection(
                foreignKeys,
                entityToImport,
                foreignField,
                registry,
                foreignKeyField);
          }
          else
          {
            isForeignFieldLinked =
              processForeignNonCachedEntity(
                foreignKeyObj,
                entityToImport,
                foreignField,
                registry,
                foreignKeyField);
          }
        }
        else
        {
          isForeignFieldLinked = true;
        }
      }
      else
      {
        isForeignFieldLinked = true;
      }
    }

    return isForeignFieldLinked;
  }

  protected boolean checkEmbeddedEntity(
    ImportEntity entityToImport,
    FieldMetaInfo fMetaInfo,
    ImportRegistry registry)
    throws Exception
  {
    Logger.debug("[AbstractEntityImporter.checkEmbeddedEntity] type embedded");
    boolean noUnlinkForeignEntities = true;
    Number embeddedField = fMetaInfo.getFieldId();
    Object embeddedObj =
      entityToImport.getEntity().getFieldValue(embeddedField);
    if (embeddedObj != null)
    {
      if (embeddedObj instanceof Collection)
      {
        Collection embeddedEntities = (Collection) embeddedObj;
        Collection toPersistEntities = new ArrayList();
        for (Iterator i = embeddedEntities.iterator(); i.hasNext();)
        {
          IEntity embeddedEntity = (IEntity) i.next();
          ImportEntity embedded =
            new ImportEntity(embeddedEntity, null, null, false, true);
          if (!EntityImportLogic.checkFields(embedded, registry))
          {
            noUnlinkForeignEntities = false;
          }
          else
          {
            // update the links in the embedded entity
            embedded.updateEntity();
            toPersistEntities.add(embedded.getEntity());
          }
        }

        if (noUnlinkForeignEntities)
        {
          //Logger.debug("[AbstractEntityImporter.checkEmbeddeembeddedFielddEntity] --Setting to PersistedEntity--");
          entityToImport.setPersistedValue(embeddedField, toPersistEntities);
        }

      }
      else
      {
        IEntity embeddedEntity = (IEntity) embeddedObj;
        // Must clone the embeddedEntity first so that when the embeddedEntity
        // is updated, it will not update the one in the parent entity. The
        // one in the parent entity will be updated when saving the parent
        // entity.
        ImportEntity embedded =
          new ImportEntity(
            (IEntity) embeddedEntity.clone(),
            null,
            null,
            false,
            true);
        if (!EntityImportLogic.checkFields(embedded, registry))
        {
          noUnlinkForeignEntities = false;
        }
        else
        {
          // update the links in the embedded entity then set it into the
          // persisted values, it will be updated to the parent entity when
          // the parent entity is saved.
          embedded.updateEntity();
          entityToImport.setPersistedValue(embeddedField, embedded.getEntity());
        }
      }
    }
    else
    {
      Logger.debug("[AbstractEntityImporter.checkFields] embeddedObj is null");
    }
    return noUnlinkForeignEntities;
  }

  protected boolean processForeignCachedCollection(
    Collection foreignObjs,
    ImportEntity entityToImport,
    Number foreignField,
    ImportRegistry registry)
    throws Exception
  {
    Logger.debug(
      "[AbstractEntityImporter.processForeignCachedCollection] Start ");
    boolean allForeignEntitiesLinked = true;
    Class collectionClass = foreignObjs.getClass();
    Collection newForeignObjs = (Collection) collectionClass.newInstance();
    for (Iterator j = foreignObjs.iterator(); j.hasNext();)
    {
      boolean foreignEntityLinked = false;
      IEntity foreignEntity = (IEntity) j.next();
      Logger.debug(
        "[AbstractEntityImporter.processForeignCachedCollection] foreignEntity name ="
          + foreignEntity.getEntityName()
          + " Uid = "
          + foreignEntity.getKeyId());
      ImportEntity persistedForeign =
        registry.getPersistedList().getEntity(foreignEntity);
      if (persistedForeign != null)
      {
        Logger.debug(
          "[AbstractEntityImporter.processForeignCachedCollection] already persisted");
        // Foreign entity already processed, so can link back to entity
        newForeignObjs.add(foreignEntity);
        foreignEntityLinked = true;
      }
      else
      {
        Logger.debug(
          "[AbstractEntityImporter.processForeignCachedCollection] not yet persisted");
        ImportEntity conflictEntity =
          registry.getConflictList().getEntity(foreignEntity);
        //Logger.debug("[AbstractEntityImporter.processForeignCachedCollection][---EntityToImport---]"+entityToImport.getEntity());
        if (conflictEntity != null)
        {
          // Foreign entity is a conflicting entity
          if (!conflictEntity.isOverwrite())
          {
            // Don't overwrite, load from database
            ImportEntity loadedEntity = loadEntityFromDB(conflictEntity);
            registry.getPersistedList().addImportEntity(loadedEntity);
            //Logger.debug("[AbstractEntityImporter.processForeignCachedCollection][---LoadedEntity---]"+loadedEntity.getEntity());
            registry.getImportList().removeImportEntity(loadedEntity);
            registry.getConflictList().removeImportEntity(loadedEntity);
            entityToImport.getEntity().setFieldValue(
              foreignField,
              loadedEntity.getEntity());
            foreignEntityLinked = true;
          }
        }
      }

      if (!foreignEntityLinked)
      {
        allForeignEntitiesLinked = false;
      }
    }

    if (allForeignEntitiesLinked)
    {
      entityToImport.setPersistedValue(foreignField, newForeignObjs);
    }

    Logger.debug(
      "[AbstractEntityImporter.processForeignCachedCollection] allForeignEntitiesLinked = "
        + allForeignEntitiesLinked);
    return allForeignEntitiesLinked;
  }

  protected boolean processForeignCachedEntity(
    IEntity foreignEntity,
    ImportEntity entityToImport,
    Number foreignField,
    ImportRegistry registry)
    throws Exception
  {
    Logger.debug("[AbstractEntityImporter.processForeignCachedEntity] Start ");
    boolean foreignEntityLinked = false;
    ImportEntity persistedForeign =
      registry.getPersistedList().getEntity(foreignEntity);
    Logger.debug(
      "[AbstractEntityImporter.processForeignCachedEntity] foreignEntity type = "
        + foreignEntity.getEntityName()
        + "  Uid = "
        + foreignEntity.getKeyId());
    if (persistedForeign != null)
    {
      Logger.debug(
        "[AbstractEntityImporter.processForeignCachedEntity] already persisted");
      // Foreign entity already processed, so can link back to entity
      entityToImport.setPersistedValue(
        foreignField,
        persistedForeign.getEntity());
      foreignEntityLinked = true;
    }
    else
    {
      Logger.debug(
        "[AbstractEntityImporter.processForeignCachedEntity] not yet persisted");
      ImportEntity conflictEntity =
        registry.getConflictList().getEntity(foreignEntity);
      if (conflictEntity != null)
      {
        // Foreign entity is a conflicting entity
        if (!conflictEntity.isOverwrite())
        {
          Logger.debug(
            "[AbstractEntityImporter.processForeignCachedEntity] is not overwrite");
          ImportEntity loadedEntity = loadEntityFromDB(conflictEntity);
          registry.getPersistedList().addImportEntity(loadedEntity);
          registry.getImportList().removeImportEntity(entityToImport);
          registry.getConflictList().removeImportEntity(entityToImport);
          entityToImport.setPersistedValue(
            foreignField,
            loadedEntity.getEntity());
          foreignEntityLinked = true;
        }
      }
    }
    Logger.debug(
      "[AbstractEntityImporter.processForeignCachedEntity] foreignEntityLinked = "
        + foreignEntityLinked);
    return foreignEntityLinked;
  }

  protected boolean processForeignNonCachedCollection(
    Collection foreignKeys,
    ImportEntity entityToImport,
    Number foreignField,
    ImportRegistry registry,
    FieldMetaInfo foreignKeyField)
    throws Exception
  {
    Logger.debug(
      "[AbstractEntityImporter.processForeignNonCachedCollection] Start ");
    boolean allForeignEntitiesLinked = true;
    Class collectionClass = foreignKeys.getClass();
    Collection newForeignKeys = (Collection) collectionClass.newInstance();
    for (Iterator j = foreignKeys.iterator(); j.hasNext();)
    {
      boolean foreignEntityLinked = false;
      Object foreignKey = j.next();
      Collection presults =
        registry.getPersistedList().getEntity(
          foreignKeyField.getEntityName(),
          foreignKeyField.getFieldId(),
          foreignKey);
      if (!presults.isEmpty())
      {
        ImportEntity persistedForeign =
          (ImportEntity) presults.iterator().next();
        Logger.debug(
          "[AbstractEntityImporter.processForeignNonCachedCollection] already persisted");
        // Foreign entity already processed, so can link back to entity
        Object newKeyValue =
          persistedForeign.getEntity().getFieldValue(
            foreignKeyField.getFieldId());
        newForeignKeys.add(newKeyValue);
        foreignEntityLinked = true;
      }
      else
      {
        Collection cresults =
          registry.getConflictList().getEntity(
            foreignKeyField.getEntityName(),
            foreignKeyField.getFieldId(),
            foreignKey);
        //Logger.debug("[AbstractEntityImporter.processForeignCachedCollection][---EntityToImport---]"+entityToImport.getEntity());
        if (!cresults.isEmpty())
        {
          ImportEntity conflictEntity =
            (ImportEntity) cresults.iterator().next();
          Logger.debug(
            "[AbstractEntityImporter.processForeignNonCachedCollection] in conflict");
          if (!conflictEntity.isOverwrite())
          {
            Logger.debug(
              "[AbstractEntityImporter.processForeignNonCachedCollection] is overwrite");
            ImportEntity loadedEntity = loadEntityFromDB(conflictEntity);
            registry.getPersistedList().addImportEntity(loadedEntity);
            //Logger.debug("[AbstractEntityImporter.processForeignCachedCollection][---LoadedEntity---]"+loadedEntity.getEntity());
            registry.getImportList().removeImportEntity(loadedEntity);
            registry.getConflictList().removeImportEntity(loadedEntity);

            Object newKeyValue =
              loadedEntity.getEntity().getFieldValue(
                foreignKeyField.getFieldId());

            newForeignKeys.add(newKeyValue);
            foreignEntityLinked = true;
          }
        }
      }

      if (!foreignEntityLinked)
      {
        allForeignEntitiesLinked = false;
      }
    }

    if (allForeignEntitiesLinked)
    {
      Logger.debug(
        "[AbstractEntityImporter.processForeignCachedEntity] allForeignEntitiesLinked!!!");
      entityToImport.setPersistedValue(foreignField, newForeignKeys);
    }

    Logger.debug(
      "[AbstractEntityImporter.processForeignCachedEntity] allForeignEntitiesLinked = "
        + allForeignEntitiesLinked);
    return allForeignEntitiesLinked;
  }

  protected boolean processForeignNonCachedEntity(
    Object foreignKeyObj,
    ImportEntity entityToImport,
    Number foreignField,
    ImportRegistry registry,
    FieldMetaInfo foreignKeyField)
    throws Exception
  {
    Logger.debug(
      "[AbstractEntityImporter.processForeignNonCachedEntity] Start ");
    Logger.debug(
      "[AbstractEntityImporter.processForeignNonCachedEntity] foreignKeyField.getEntityName() ="
        + foreignKeyField.getEntityName());
    Logger.debug(
      "[AbstractEntityImporter.processForeignNonCachedEntity] foreignKeyField.getFieldId() ="
        + foreignKeyField.getFieldId());
    //Logger.debug("[AbstractEntityImporter.processForeignNonCachedEntity] foreignKeyObj ="+foreignKeyObj);
    boolean foreignEntityLinked = false;
    Collection results =
      registry.getPersistedList().getEntity(
        foreignKeyField.getEntityName(),
        foreignKeyField.getFieldId(),
        foreignKeyObj);
    if (!results.isEmpty())
    {
      Logger.debug(
        "[AbstractEntityImporter.processForeignNonCachedEntity] already persisted");
      ImportEntity persistedForeign = (ImportEntity) results.iterator().next();
      Object newKeyValue =
        persistedForeign.getEntity().getFieldValue(
          foreignKeyField.getFieldId());
      entityToImport.setPersistedValue(foreignField, newKeyValue);
      foreignEntityLinked = true;
    }
    else
    {
      Collection cresults =
        registry.getConflictList().getEntity(
          foreignKeyField.getEntityName(),
          foreignKeyField.getFieldId(),
          foreignKeyObj);
      if (!cresults.isEmpty())
      {
        Logger.debug(
          "[AbstractEntityImporter.processForeignNonCachedEntity] in conflict");
        ImportEntity conflictEntity = (ImportEntity) cresults.iterator().next();
        if (!conflictEntity.isOverwrite())
        {
          Logger.debug(
            "[AbstractEntityImporter.processForeignNonCachedEntity] is not overwrite");
          ImportEntity loadedEntity = loadEntityFromDB(conflictEntity);
          registry.getPersistedList().addImportEntity(loadedEntity);
          registry.getImportList().removeImportEntity(conflictEntity);
          registry.getConflictList().removeImportEntity(conflictEntity);
          Object newKeyValue =
            loadedEntity.getEntity().getFieldValue(
              foreignKeyField.getFieldId());
          entityToImport.setPersistedValue(foreignField, newKeyValue);
          foreignEntityLinked = true;
        }
      }
    }
    Logger.debug(
      "[AbstractEntityImporter.processForeignNonCachedEntity] foreignEntityLinked = "
        + foreignEntityLinked);
    return foreignEntityLinked;
  }

  protected ImportEntity saveEntityToDB(
    ImportEntity importEntity,
    ImportRegistry registry)
    throws Exception
  {
    try
    {
      String entityName = importEntity.getEntity().getEntityName();
      Logger.debug(
        "[AbstractEntityImporter.saveEntityToDB] entityName : " + entityName);
      AbstractEntityHandler handler =
        EntityHandlerHelper.getHandler(entityName);
      importEntity.updateEntity();
      IEntity entityToSave = importEntity.getEntity();
      Long oldUid = importEntity.getNewUid();

      if (oldUid != null)
      {
        Collection oldFiles = getFilesUsedByOldEntity(oldUid, handler);
        for (Iterator i = oldFiles.iterator(); i.hasNext();)
        {
          File oldFile = (File) i.next();
          if (oldFile.exists())
          {
            Logger.debug(
              "[AbstractEntityImporter.saveEntityToDB] Deleting old file : "
                + oldFile.getAbsolutePath());
            oldFile.delete();
          }
        }
        // set uid to uid of entity to overwrite
        IEntity oldEntity = handler.getEntityByKeyForReadOnly(oldUid);
        double oldVersion = oldEntity.getVersion();
        entityToSave =
          EntityRelationshipHelper.setVersion(entityToSave, oldVersion);
        //entityToSave.setFieldValue(new Integer(0), oldUid);
        entityToSave.setFieldValue(oldEntity.getKeyId(), oldUid);
        saveFilesUsedByEntity(entityToSave, registry);
        Logger.debug(
          "[AbstractEntityImporter.saveEntityToDB] oldUid : " + oldUid);
        Logger.debug(
          "[AbstractEntityImporter.saveEntityToDB] oldVersion : " + oldVersion);
        handler.updateEntity(entityToSave);
      }
      else
      {
        saveFilesUsedByEntity(entityToSave, registry);
        entityToSave = handler.createEntity(entityToSave);
      }

      importEntity.setEntity(entityToSave);
      importEntity.setNewUid((Long) entityToSave.getKey());

      return importEntity;
    }
    catch (Throwable ex)
    {
      throw new ImportConfigException("Error saving entity to database", ex);
    }
  }

  protected IEntity saveFilesUsedByEntity(
    IEntity entityToSave,
    ImportRegistry registry)
    throws Exception
  {
    Logger.debug("[AbstractEntityImporter.saveFilesUsedByEntity] Start ");

    String unzipDir = registry.getUnzipDir().getName() + IMetaInfoConstants.FSLASH;

    EntityMetaInfo metaInfo =
      MetaInfoFactory.getInstance().getMetaInfoFor(
        entityToSave.getEntityName());
    FieldMetaInfo[] fMetaInfos = metaInfo.getFieldMetaInfo();
    for (int j = 0; j < fMetaInfos.length; j++)
    {
      FieldMetaInfo fMetaInfo = fMetaInfos[j];
      Properties properties = fMetaInfo.getConstraints();
      String type = properties.getProperty(IMetaInfoConstants.CONSTRAINT_TYPE);
      if (type != null && type.equals(IMetaInfoConstants.TYPE_FILE))
      {
        String filename =
          entityToSave.getFieldValue(fMetaInfo.getFieldId()).toString();
        Logger.debug(
          "[AbstractEntityImporter.saveFilesUsedByEntity] filename = "
            + filename);

        //identify any subPath for the file field
        String subSubPath =
          EntityRelationshipHelper.getSubPath(
            entityToSave,
            fMetaInfos,
            properties.getProperty(IMetaInfoConstants.FILE_SUBPATH, IMetaInfoConstants.ESTR));

        String pathKey =
          properties.getProperty(IMetaInfoConstants.FILE_FIXEDKEY, IMetaInfoConstants.ESTR);
        if (pathKey.trim().length() > 0)
        {
          String subPath = FileUtil.getPath(pathKey);
          String newFilename =
            FileUtil.copy(
              IPathConfig.PATH_TEMP,
              unzipDir + subPath + subSubPath,
              filename,
              pathKey,
              subSubPath,
              filename);
          entityToSave.setFieldValue(fMetaInfo.getFieldId(), newFilename);
          Logger.debug(
            "[AbstractEntityImporter.saveFilesUsedByEntity] newFilename = "
              + newFilename);
        }
        else
        {
          String fieldWithPath =
            properties.getProperty(IMetaInfoConstants.FILE_PATHKEY, IMetaInfoConstants.ESTR);
          if (fieldWithPath.trim().length() > 0)
          {
            for (int i = 0; i < fMetaInfos.length; i++)
            {
              FieldMetaInfo fMetaInfoWithPathKey = fMetaInfos[i];
              String label = fMetaInfoWithPathKey.getLabel();
              if (fieldWithPath.equals(label))
              {
                pathKey =
                  entityToSave
                    .getFieldValue(fMetaInfoWithPathKey.getFieldId())
                    .toString();
                String subPath = FileUtil.getPath(pathKey);
                String newFilename =
                  FileUtil.copy(
                    IPathConfig.PATH_TEMP,
                    unzipDir + subPath + subSubPath,
                    filename,
                    pathKey,
                    subSubPath,
                    filename);
                entityToSave.setFieldValue(fMetaInfo.getFieldId(), newFilename);
                Logger.debug(
                  "[AbstractEntityImporter.saveFilesUsedByEntity] newFilename 2 = "
                    + newFilename);
              }
            }
          }
        }
      }
    }
    return entityToSave;
  }

  protected Collection getFilesUsedByOldEntity(
    Long oldUid,
    AbstractEntityHandler handler)
    throws Exception
  {
    Collection oldFiles = new ArrayList();
    IEntity oldEntity = handler.getEntityByKeyForReadOnly(oldUid);
    EntityMetaInfo metaInfo =
      MetaInfoFactory.getInstance().getMetaInfoFor(oldEntity.getEntityName());
    FieldMetaInfo[] fMetaInfos = metaInfo.getFieldMetaInfo();
    for (int j = 0; j < fMetaInfos.length; j++)
    {
      FieldMetaInfo fMetaInfo = fMetaInfos[j];
      Properties properties = fMetaInfo.getConstraints();
      String type = properties.getProperty(IMetaInfoConstants.CONSTRAINT_TYPE);
      if (type != null && type.equals(IMetaInfoConstants.TYPE_FILE))
      {
        String filename =
          oldEntity.getFieldValue(fMetaInfo.getFieldId()).toString();

        String subPath =
          EntityRelationshipHelper.getSubPath(
            oldEntity,
            fMetaInfos,
            properties.getProperty(IMetaInfoConstants.FILE_SUBPATH, IMetaInfoConstants.ESTR));

        String pathKey =
          properties.getProperty(IMetaInfoConstants.FILE_FIXEDKEY, IMetaInfoConstants.ESTR);
        if (pathKey.trim().length() > 0)
        {
          File oldFile = FileUtil.getFile(pathKey, subPath, filename);
          if (oldFile != null)
          {
            oldFiles.add(oldFile);
          }
        }
        else
        {
          String fieldWithPath =
            properties.getProperty(IMetaInfoConstants.FILE_PATHKEY, IMetaInfoConstants.ESTR);
          if (fieldWithPath.trim().length() > 0)
          {
            for (int i = 0; i < fMetaInfos.length; i++)
            {
              FieldMetaInfo fMetaInfoWithPathKey = fMetaInfos[i];
              String label = fMetaInfoWithPathKey.getLabel();
              if (fieldWithPath.equals(label))
              {
                pathKey =
                  oldEntity
                    .getFieldValue(fMetaInfoWithPathKey.getFieldId())
                    .toString();
                File oldFile = FileUtil.getFile(pathKey, subPath, filename);
                if (oldFile != null)
                {
                  oldFiles.add(oldFile);
                }
              }
            }
          }
        }
      }
    }
    return oldFiles;
  }
}