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
 * May 21 2003    Koh Han Sing        Created
 * Dec 16 2003    Koh Han Sing        GNDB00016308 : Importing of FTP
 *                                    configuration throws a RunTime Exception
 * May 17 2004    Neo Sok Lay         Handle get file with subpath.
 *                                    Refactor.
 * Nov 02 2005    Neo Sok Lay         Load FMI from session bean MetaInfoBean instead
 *                                    of EntityBean FieldMetaInfoBean
 * Dec 22 2005    Neo Sok Lay         Retrieve FMI from MetaInfoFactory instead of bean direct.                                                                      
 */
package com.gridnode.pdip.base.exportconfig.helpers;

import com.gridnode.pdip.base.exportconfig.exception.ExportConfigException;
import com.gridnode.pdip.framework.db.AbstractEntityHandler;
import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.db.meta.*;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.util.*;

/**
 * This class contain logic for doing complex entity processing.
 *
 * @author Koh Han Sing
 *
 * @version GT 4.0
 * @since 2.1 I1
 */

public class EntityRelationshipHelper
{

  public EntityRelationshipHelper()
  {
  }

  /**
   * This method return a Hashtable of pathKeys and filenames used in the given
   * entity.
   */
  public static Hashtable retrieveFilesUsedByEntity(IEntity entity)
    throws Exception
  {
    Logger.debug("[EntityRelationshipHelper.retrieveFilesUsedByEntity] Start");
    Hashtable fileTable = new Hashtable();

    EntityMetaInfo metaInfo =
      MetaInfoFactory.getInstance().getMetaInfoFor(entity.getEntityName());
    FieldMetaInfo[] fMetaInfos = metaInfo.getFieldMetaInfo();
    for (int j = 0; j < fMetaInfos.length; j++)
    {
      FieldMetaInfo fMetaInfo = fMetaInfos[j];
      Properties properties = fMetaInfo.getConstraints();
      String type =
        properties.getProperty(
          IMetaInfoConstants.CONSTRAINT_TYPE,
          IMetaInfoConstants.ESTR);
      if (type.equals(IMetaInfoConstants.TYPE_FILE))
      {
        String filename =
          entity.getFieldValue(fMetaInfo.getFieldId()).toString();

        //identify any subpath defined for the file field
        //concat with the filename  
        String subPath =
          getSubPath(
            entity,
            fMetaInfos,
            properties.getProperty(
              IMetaInfoConstants.FILE_SUBPATH,
              IMetaInfoConstants.ESTR));
        filename = subPath + filename;

        String pathKey =
          properties.getProperty(
            IMetaInfoConstants.FILE_FIXEDKEY,
            IMetaInfoConstants.ESTR);
        if (pathKey.trim().length() > 0)
        {
          fileTable = addToFileTable(pathKey, filename, fileTable);
        }
        else
        {
          String fieldWithPath =
            properties.getProperty(
              IMetaInfoConstants.FILE_PATHKEY,
              IMetaInfoConstants.ESTR);
          if (fieldWithPath.trim().length() > 0)
          {
            for (int i = 0; i < fMetaInfos.length; i++)
            {
              fMetaInfo = fMetaInfos[i];
              String label = fMetaInfo.getLabel();
              if (fieldWithPath.equals(label))
              {
                pathKey =
                  entity.getFieldValue(fMetaInfo.getFieldId()).toString();
                fileTable = addToFileTable(pathKey, filename, fileTable);
              }
            }
          }
        }
      }
    }
    Logger.debug("[EntityRelationshipHelper.retrieveFilesUsedByEntity] End");
    return fileTable;
  }

  public static String getSubPath(
    IEntity entity,
    FieldMetaInfo[] fMetaInfos,
    String subPathField)
  {
    String subPath = IMetaInfoConstants.ESTR;
    if (subPathField != null && subPathField.trim().length() > 0)
    {
      String suffix = IMetaInfoConstants.DOT + subPathField;
      FieldMetaInfo fmi;
      for (int i = 0; i < fMetaInfos.length; i++)
      {
        fmi = fMetaInfos[i];
        if (fmi.getLabel() != null && fmi.getLabel().endsWith(suffix)) //match
        {
          //get the value of the subpath
          subPath = (String) entity.getFieldValue(fmi.getFieldId());
          if (subPath == null || subPath.trim().length() == 0)
            subPath = IMetaInfoConstants.ESTR;
          else if (!subPath.endsWith(IMetaInfoConstants.FSLASH))
            subPath += IMetaInfoConstants.FSLASH;
          break;
        }
      }
    }

    return subPath;
  }

  /**
   * This method tells you whether a entity contains foreign entities.
   */
  public static boolean hasForeignEntity(IEntity entity) throws Exception
  {
    return hasForeignEntity(entity.getEntityName());
  }

  /**
   * This method tells you whether a entity contains foreign entities.
   */
  public static boolean hasForeignEntity(String entityName) throws Exception
  {
    boolean hasForeignEntity = false;

    EntityMetaInfo metaInfo =
      MetaInfoFactory.getInstance().getMetaInfoFor(entityName);
    FieldMetaInfo[] fMetaInfos = metaInfo.getFieldMetaInfo();
    for (int j = 0; j < fMetaInfos.length; j++)
    {
      FieldMetaInfo fMetaInfo = fMetaInfos[j];
      Properties properties = fMetaInfo.getConstraints();
      String type =
        properties.getProperty(
          IMetaInfoConstants.CONSTRAINT_TYPE,
          IMetaInfoConstants.ESTR);
      if (type.equals(IMetaInfoConstants.TYPE_FOREIGN)
        || type.equals(IMetaInfoConstants.TYPE_DYNAMIC)
        || type.equals(IMetaInfoConstants.TYPE_EMBEDDED))
      {
        hasForeignEntity = true;
        break;
      }
    }

    return hasForeignEntity;
  }

  /**
   * This method retrieve a non-cached foreign entity based on the field and the
   * unique identifier stored in that field.
   */
  public static Collection getNonCachedForeignEntities(IEntity entity)
    throws Exception
  {
    Collection foreignEntities = new ArrayList();

    String entityName = entity.getEntityName();
    Logger.debug(
      "[EntityRelationshipHelper.getNonCachedForeignEntities] entityName ="
        + entityName
        + "=");
    Collection foreignNonCacheFields = getForeignFields(entityName, false);
    for (Iterator j = foreignNonCacheFields.iterator(); j.hasNext();)
    {
      Number foreignFieldId = (Number) j.next();
      Object foreignEntitesFromFieldObj =
        getNonCachedForeignEntitiesFromField(entity, foreignFieldId);
      if (foreignEntitesFromFieldObj != null)
      {
        if (foreignEntitesFromFieldObj instanceof Collection)
        {
          foreignEntities.addAll((Collection) foreignEntitesFromFieldObj);
        }
        else
        {
          foreignEntities.add(foreignEntitesFromFieldObj);
        }
      }
    }
    return foreignEntities;
  }

  public static Object getNonCachedForeignEntitiesFromField(
    IEntity entity,
    Number foreignFieldId)
    throws Exception
  {
    Logger.debug(
      "[EntityRelationshipHelper.getNonCachedForeignEntitiesFromField] entityName ="
        + entity.getEntityDescr()
        + "=");
    Object result = null;
    Collection foreignEntities = new ArrayList();

    EntityMetaInfo metaInfo = entity.getMetaInfo();
    FieldMetaInfo fMetaInfo = metaInfo.findFieldMetaInfo(foreignFieldId);
    FieldMetaInfo fieldMetaInfo = getForeignKeyField(fMetaInfo);
    if (fieldMetaInfo != null)
    {
      String foreignEntityFullName = fieldMetaInfo.getEntityName();
      String foreignEntityName =
        foreignEntityFullName.substring(
          foreignEntityFullName.lastIndexOf(IMetaInfoConstants.DOT) + 1);
      Logger.debug(
        "[EntityRelationshipHelper.getNonCachedForeignEntitiesFromField] foreignEntityName ="
          + foreignEntityName);
      if (ExportConfigLoader.getInstance().isExportable(foreignEntityName))
      {
        AbstractEntityHandler handler =
          EntityHandlerHelper.getHandler(foreignEntityName);

        Object fieldKeyValue = entity.getFieldValue(foreignFieldId);
        if ((fieldKeyValue != null)
          && (!fieldKeyValue.equals(IMetaInfoConstants.ESTR)))
        {
          if (fieldKeyValue instanceof Collection)
          {
            Logger.debug(
              "[EntityRelationshipHelper.getNonCachedForeignEntitiesFromField] instanceof Collection");
            Collection fieldKeyValues = (Collection) fieldKeyValue;
            for (Iterator i = fieldKeyValues.iterator(); i.hasNext();)
            {
              Object keyValue = i.next();
              IEntity foreignEntity =
                getEntity(handler, fieldMetaInfo, keyValue);
              foreignEntities.add(foreignEntity);
            }
            result = foreignEntities;
          }
          else
          {
            Logger.debug(
              "[EntityRelationshipHelper.getNonCachedForeignEntitiesFromField] not Collection");
            IEntity foreignEntity =
              getEntity(handler, fieldMetaInfo, fieldKeyValue);
            result = foreignEntity;
          }
        }
      }
    }
    else
    {
      throw new ExportConfigException(
        "Unable to find foreign.key in field "
          + fMetaInfo.getFieldName()
          + " of "
          + entity.getEntityName());
    }
    return result;
  }

  /**
   * This method returns a collection of fields Id which are either storing a
   * foreign entity or a unique identifier to a foreign entity, depending on
   * whether it is cached or not.
   */
  public static Collection getForeignFields(
    String entityName,
    boolean foreignCache)
    throws Exception
  {
    ArrayList foreignCachedFields = new ArrayList();
    ArrayList foreignNonCachedFields = new ArrayList();

    EntityMetaInfo metaInfo =
      MetaInfoFactory.getInstance().getMetaInfoFor(entityName);
    FieldMetaInfo[] fMetaInfos = metaInfo.getFieldMetaInfo();
    for (int j = 0; j < fMetaInfos.length; j++)
    {
      FieldMetaInfo fMetaInfo = fMetaInfos[j];
      Properties properties = fMetaInfo.getConstraints();
      String type =
        properties.getProperty(
          IMetaInfoConstants.CONSTRAINT_TYPE,
          IMetaInfoConstants.ESTR);
      if (type.equals(IMetaInfoConstants.TYPE_FOREIGN))
      {
        String cache =
          properties.getProperty(
            IMetaInfoConstants.FOREIGN_CACHED,
            IMetaInfoConstants.FALSE);
        if (cache.equals(IMetaInfoConstants.TRUE))
        {
          foreignCachedFields.add(fMetaInfo.getFieldId());
        }
        else
        {
          foreignNonCachedFields.add(fMetaInfo.getFieldId());
        }
      }
    }

    if (foreignCache)
    {
      return foreignCachedFields;
    }
    return foreignNonCachedFields;
  }

  public static boolean canDelete(IEntity entity) throws Exception
  {
    boolean canDelete = true;
    String entityName = entity.getEntityName();
    EntityMetaInfo metaInfo = entity.getMetaInfo();
    String objectClassName = metaInfo.getObjectName();
    String packageName =
      objectClassName.substring(0, objectClassName.indexOf(entityName) - 1);
    Class interfaceClass =
      Class.forName(packageName + IMetaInfoConstants.DOT_I + entityName);
    try
    {
      Integer deleteField =
        (Integer) interfaceClass.getField(
          IMetaInfoConstants.FIELD_CAN_DELETE).get(
          null);
      Object value = entity.getFieldValue(deleteField);
      if (value != null)
      {
        canDelete = ((Boolean) value).booleanValue();
      }
    }
    catch (NoSuchFieldException ex)
    {
      //No such field than assume can delete
    }
    return canDelete;
  }

  public static IEntity setVersion(IEntity entity, double version)
    throws Exception
  {
    String entityName = entity.getEntityName();
    EntityMetaInfo metaInfo = entity.getMetaInfo();
    String objectClassName = metaInfo.getObjectName();
    String packageName =
      objectClassName.substring(0, objectClassName.indexOf(entityName) - 1);
    Class interfaceClass =
      Class.forName(packageName + IMetaInfoConstants.DOT_I + entityName);

    try
    {
      Integer versionField =
        (Integer) interfaceClass.getField(
          IMetaInfoConstants.FIELD_VERSION).get(
          null);
      entity.setFieldValue(versionField, new Double(version));
    }
    catch (NoSuchFieldException ex)
    {
      //No such field than assume no version checking for this entity
    }
    return entity;
  }

  private static Hashtable addToFileTable(
    String pathKey,
    String filename,
    Hashtable fileTable)
    throws Exception
  {
    Object filenameObj = fileTable.get(pathKey);
    if (filenameObj != null)
    {
      Collection filenameList = (Collection) filenameObj;
      boolean found = filenameList.contains(filename);
      if (!found)
      {
        filenameList.add(filename);
        fileTable.put(pathKey, filenameList);
      }
    }
    else
    {
      ArrayList filenameList = new ArrayList();
      filenameList.add(filename);
      fileTable.put(pathKey, filenameList);
    }

    return fileTable;
  }

  private static IEntity getEntity(
    AbstractEntityHandler handler,
    FieldMetaInfo fieldMetaInfo,
    Object fieldValue)
    throws Exception
  {
    String entityName = fieldMetaInfo.getEntityName();
    entityName =
      entityName.substring(entityName.lastIndexOf(IMetaInfoConstants.DOT) + 1);
    EntityMetaInfo metaInfo =
      MetaInfoFactory.getInstance().getMetaInfoFor(entityName);
    String objectClassName = metaInfo.getObjectName();
    String packageName =
      objectClassName.substring(0, objectClassName.indexOf(entityName) - 1);
    Class interfaceClass =
      Class.forName(packageName + IMetaInfoConstants.DOT_I + entityName);

    IDataFilter filter = new DataFilterImpl();
    try
    {
      filter.addSingleFilter(
        null,
        interfaceClass.getField(fieldMetaInfo.getFieldName()).get(null),
        filter.getEqualOperator(),
        fieldValue,
        false);

      Collection entities = handler.getEntityByFilterForReadOnly(filter);
      if (entities.isEmpty())
      {
        throw new ExportConfigException(
          "Unable to find "
            + fieldMetaInfo.getEntityName()
            + " with field "
            + fieldMetaInfo.getFieldName()
            + " = "
            + fieldValue);
      }
      IEntity foreignEntity = (IEntity) entities.iterator().next();
      return foreignEntity;
    }
    catch (NoSuchFieldException ex)
    {
      throw new ExportConfigException(
        "Unable to find "
          + fieldMetaInfo.getEntityName()
          + " with field "
          + fieldMetaInfo.getFieldName()
          + " = "
          + fieldValue);
    }

  }

  public static FieldMetaInfo getForeignKeyField(FieldMetaInfo fMetaInfo)
    throws Exception
  {
    FieldMetaInfo foreignFieldMetaInfo = null;
    Properties properties = fMetaInfo.getConstraints();
    String foreignKey =
      properties.getProperty(
        IMetaInfoConstants.FOREIGN_KEY,
        IMetaInfoConstants.ESTR);
    Logger.debug(
      "[EntityRelationshipHelper.getForeignKeyField] foreignKey = "
        + foreignKey);
    if (foreignKey.trim().length() > 0)
    {
      Collection fieldMetaInfos =
      	MetaInfoFactory.getInstance().getFieldMetaInfoByLabel(foreignKey);
      	//getMetaInfoBean().findFieldMetaInfoByLabel(foreignKey);
        //getFieldMetaInfoBean().findByLabel(foreignKey);
      if (fieldMetaInfos != null && !fieldMetaInfos.isEmpty())
      {
      	/*
        IFieldMetaInfoLocalObj obj =
          (IFieldMetaInfoLocalObj) fieldMetaInfos.iterator().next();
        foreignFieldMetaInfo = obj.getData();
        */
      	foreignFieldMetaInfo = (FieldMetaInfo)fieldMetaInfos.iterator().next();
      	
        Logger.debug(
          "[EntityRelationshipHelper.getForeignKeyField] getFieldName = "
            + foreignFieldMetaInfo.getFieldName());
        String entityName = foreignFieldMetaInfo.getEntityName();
        StringTokenizer st =
          new StringTokenizer(entityName, IMetaInfoConstants.DOT);
        while (st.hasMoreTokens())
        {
          entityName = st.nextToken();
        }

        String objName = foreignFieldMetaInfo.getObjectName();
        Logger.debug(
          "[EntityRelationshipHelper.getForeignKeyField] entityName = "
            + entityName);
        foreignFieldMetaInfo =
          MetaInfoFactory.getInstance().getMetaInfoFor(entityName, objName);
      }
    }
    return foreignFieldMetaInfo;
  }
  /*
  public static IMetaInfoObj getMetaInfoBean() throws Exception
  {
    return (IMetaInfoObj) ServiceLocator
      .instance(ServiceLocator.CLIENT_CONTEXT)
      .getObj(
        IMetaInfoHome.class.getName(),
        IMetaInfoHome.class,
        new Object[0]);
  }*/

  /*
  public static IFieldMetaInfoLocalHome getFieldMetaInfoBean() throws Exception
  {
    return (IFieldMetaInfoLocalHome) ServiceLocator
      .instance(ServiceLocator.CLIENT_CONTEXT)
      .getHome(
        IFieldMetaInfoLocalHome.class.getName(),
        IFieldMetaInfoLocalHome.class);
  }*/
}