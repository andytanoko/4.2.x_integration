/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IMetaInfoFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 11 2002    Neo Sok Lay         Add getMetaInfoFor(entityName,fieldObjName)
 */
package com.gridnode.pdip.framework.db.meta;

/**
 * This interface defines the basic functionality for a factory to
 * handle data entities.
 *
 * @author Neo Sok Lay
 * @version 2.0 I7
 * @since 2.0
 */
public interface IMetaInfoFactory
{
  /**
   * Get the EntityMetaInfo for the specified entity.
   *
   * @param entityName Name of the entity.
   *
   * @return the EntityMetaInfo of the specified entity, or <b>null</b> if
   * none exists.
   */
  public EntityMetaInfo getMetaInfoFor(String entityName);

  /**
   * Get the EntityMetaInfo for the specified entity.
   *
   * @param objectName Full qualified class name of the entity.
   *
   * @return the EntityMetaInfo of the specified entity, or <b>null</b> if
   * none exists.
   */
  public EntityMetaInfo getEntityMetaInfo(String objectName);

  /**
   * Get all currently loaded/cached EntityMetaInfo from the factory.
   *
   * @return All EntityMetaInfo(s) cached.
   */
  public EntityMetaInfo[] getAllMetaInfo();

  /**
   * Find the meta info for a field in an entity.
   *
   * @param entityName Name of the entity class
   * @param fieldObjName Declared name of the field in the entity.
   * @return The field meta info found, or <B>null</B> if not found.
   */
  public FieldMetaInfo getMetaInfoFor(String entityName, String fieldObjName);
}