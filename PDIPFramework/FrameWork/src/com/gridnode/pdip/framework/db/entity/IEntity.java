/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 28 2001    Neo Sok Lay         Created
 * Apr 29 2002    Neo Sok Lay         Add version checking.
 * Jun 12 2002    Ang Meng Hua        Rename getName() to getEntityName()
 *                                    Rename getDescription to getEntityDescr()
 */
package com.gridnode.pdip.framework.db.entity;

import java.util.Enumeration;
import com.gridnode.pdip.framework.db.meta.*;

/**
 * This interface defines the basic functionality of a data entity.<P>
 * A data entity is one that the application is interested in keeping
 * track of and displaying. Meta information are required for each type of
 * data entity.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0
 * @since 1.0a build 0.9.9.6
 */
public interface IEntity
{
  /**
   * Get the meta info for this entity.
   *
   * @return Meta info the entity
   *
   * @since 1.0a build 0.9.9.6
   */
  EntityMetaInfo getMetaInfo();

  /**
   * Gives a short description of this entity.
   *
   * @return Short description of the entity instance
   *
   * @since 1.0a build 0.9.9.6
   */
  String          getEntityDescr();

  /**
   * Gives the identity of this entity.
   *
   * @return The value of the key of the entity
   *
   * @since 1.0a build 0.9.9.6
   */
  Object          getKey();

  /**
   * Gives the name of this type of entity.
   *
   * @return The name of the entity
   *
   * @since 1.0a build 0.9.9.6   *
   */
  String            getEntityName();


  /**
   * Get the value of a fieild in this entity.
   *
   * @param fieldId FieldId of the field
   * @return The value of the field. <B>null</B> if FieldId does not exists
   * in the entity.
   *
   * @since 1.0a build 0.9.9.6
   */
  Object          getFieldValue(Number fieldId);

  /**
   * Get the meta info for a field in this entity.
   *
   * @param fieldId FieldId of the field
   * @return The meta info for a field in the entity. <B>null</B> if
   * FieldId does not exists
   *
   * @since 1.0a build 0.9.9.6
   */
  FieldMetaInfo  getFieldMetaInfo(Number fieldId);

  /**
   * Sets the value of a field in the entity.
   *
   * @param fieldId FieldId of the field to set
   * @param value   Value to set in the field
   *
   * @since 1.0a build 0.9.9.6
   */
  void            setFieldValue(Number fieldId, Object value);

  /**
   * Get the fieldIds of all fields currently set in this entity.
   *
   * @return The FieldIds of all fields that currently have values set in
   * the entity
   *
   * @since 1.0a build 0.9.9.6
   */
  Enumeration     getFieldIds();

  /**
   * Get the fieldId of the identity field of this entity.
   *
   * @return The FieldId of the key field of the entity
   *
   * @since 1.0a build 0.9.9.6
   */
  Number          getKeyId();

  /**
   * Whether the entity instance contains all persistent fields (all persistent
   * fields are retrieved).
   *
   * @return <B>true</B> if all persistent fields are contained in this
   * entity instance. <B>false</B> otherwise.
   *
   * @since 1.0a build 0.9.9.6
   */
  boolean         isComplete();

  /**
   * Make a copy of the entity instance. Follows the contract of
   * <CODE>Object.clone()</CODE>.
   *
   * @return An exact copy of this entity instance.
   *
   * @since 1.0a build 0.9.9.6
   */
  Object          clone();

  /**
   * Creates a new instance of this entity type.
   *
   * @return A newly created instance of the same entity type. This new
   * instance has no fields in it.
   *
   * @since 1.0a build 0.9.9.6
   */
  IEntity         createNew();

  /**
   * Get the entity factory that can handle entity of this type.
   *
   * @return The Entity factory that is capable of handling this entity type
   *
   * @since 1.0a build 0.9.9.6
   */
  IMetaInfoFactory  getMetaInfoFactory();

  /**
   * Sets the Entity factory for this entity instance.
   *
   * @param entityFactory The entity factory to set
   *
   * @since 1.0a build 0.9.9.6
   */
  void            setMetaInfoFactory(IMetaInfoFactory metaInfoFactory);

  /**
   * Get the version of this data object.
   *
   * @return The version of this data object.
   *
   * @since 2.0
   */
  double  getVersion();

  /**
   * Increment the current version of this entity.
   *
   * @since 2.0
   */
  void increaseVersion();

}