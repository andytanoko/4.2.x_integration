/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IForeignEntityConstraint.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-07-26     Andrew Hill         Created
 * 2002-12-27     Andrew Hill         getAdditionalDisplayFieldNames() & updated javadoc
 * 2003-08-27     Andrew Hill         isLocalDisplayFieldName()
 */
package com.gridnode.gtas.client.ctrl;

public interface IForeignEntityConstraint extends IEntityConstraint
{
  /**
   * Returns the name of the field in the foreign entity whose value is used as a key to refer to
   * the foreign entity instance. The values for this field should be unique, though in some cases
   * (such as approver BEs in an ActivationRecord) additional hardcoded logic is required to filter
   * out unsuitable candidate entities when determine which entity is referred to. The most common
   * field to refer to is uid. The value of this field in the foreign entity is what is stored in
   * the field in this entity.
   * @return name of the field in foreign entity whose value is used as key to that entity instance
   */
  public String getKeyFieldName();

  /**
   * Returns the name of a field in the foreign entity that should be displayed in the ui
   * for the value of this field. Foreign entities are usually linked by the uid field, but this
   * is meaningless to a user hence the value in the display field is shown on the ui, while
   * the IGTEntity instance of course stored the keyField value). Additional display fields may
   * be optionally specified. It is up to the ui to determine if showing these is practical. The
   * names of the additional fields are retrieved using the getAdditionalDisplayFieldNames()
   * method.
   * @return name of the primary display field
   */
  public String getDisplayFieldName();
  
  /**
   * Returns true if the display field belongs to this entity and not the referenced foreign entity.
   * This only affects the primary display field. Additional display fields always come from the
   * referenced foreign entity.
   * @return isLocalDisplayFieldName
   */
  public boolean isLocalDisplayFieldName(); //20030827AH

  /**
   * Returns true if the entity to which this field refers has been cached for quick access
   * to its field values for display purposes. IGTEntity.getFieldEntities() will return the
   * cached entity if this is the case. It should be noted that cached entities field values might
   * not all be populated for efficiency reasons. (IGTFieldMetaInfo.isAvailableInCache() can be
   * used to determine if a particular field in a cached entity is available.
   * IGTEntity.isFromCache() may also be used to determine if a particular IGTEntity instance is a
   * cached entity or not).
   * @return flag indicating if the foreign entity referred to is cached in this entity
   */
  public boolean isCached();

  /**
   * Returns a String[] of the fieldnames of additional fields in the foreign entity to be
   * optionally displayed in the UI. If there are no additional display fields specified, then this
   * method will always return an empty String[] to avoid the need for tedious null checking.
   * For efficiency reasons, implementations of this method might return a reference to their own
   * copy of this array instead of generating a clone. Dont modify the array returned! That
   * would be naughty!
   * @return array of strings containing fieldnames in the foreign entity that should be displayed
   */
  public String[] getAdditionalDisplayFieldNames(); //20021227AH
}