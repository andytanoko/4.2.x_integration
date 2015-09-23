/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEntityFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 28 2001    Neo Sok Lay         Created
 */
package com.gridnode.pdip.framework.db.entity;
import com.gridnode.pdip.framework.db.meta.*;

/**
 * This interface defines the basic functionality for a factory to
 * handle data entities.
 *
 * @author Neo Sok Lay
 *
 * @version 1.0a build 0.9.9.6
 * @since 1.0a build 0.9.9.6
 */
public interface IEntityFactory extends java.rmi.Remote
{
  /**
   * The ObjectID to use for remote accesses.
   */
  final static String OBJECT_ID = "EntityFactory";

  /**
   * Creates an entity (with no fields).
   *
   * @param entityName Name of the entity
   * @return The new entity created.
   *
   * @since 1.0a build 0.9.9.6
   */
  public IEntity newEntity(String entityName)
    throws java.rmi.RemoteException;

  /**
   * Creates an entity (with no fields).
   *
   * @param entityClass The class of the entity
   *
   * @since 1.0a build 0.9.9.6
   */
  public IEntity newEntity(Class entityClass)
    throws java.rmi.RemoteException;

  /**
   * Get the meta info for an entity type handled by this factory.
   *
   * @param entityName Name of the entity
   * @return The meta info for an entity
   *
   * @since 1.0a build 0.9.9.6
   */
  public EntityMetaInfo getMetaInfoFor(String entityName)
    throws java.rmi.RemoteException;
}