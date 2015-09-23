/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTFieldMetaInfo.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-12     Andrew Hill         Created
 * 2002-10-25     Andrew Hill         Remove isEditable, isMandatory, isDisplayable methods
 *                                    replace with ones that take newEntity flag as input
 *                                    as part of refactoring to eliminate non-shared metainfo
 */
package com.gridnode.gtas.client.ctrl;

/**
 * Provides meta information about one field in an entity.
 */
public interface IGTFieldMetaInfo
{
  //public IGTEntity getEntity();

  /**
   * @depracted
   */
  public int getLength();

  /**
   * Field id used to access the field
   * @return fieldId (as defined in IGTEntity subinterface)
   */
  public Number getFieldId();

  /**
   * Field id used to access the field
   * @return fieldId (as defined in IGTEntity subinterface)
   */
  public String getFieldName();

  /**
   * Key value in resources file for label to use when displaying the field
   * @return field label (i18n key!)
   */
  public String getLabel();

  /**
   * Max length of the field data
   * @return int maximum length
   */
  //public int getLength();

  /**
   * Is the field one that should be presented to user in various circumstances or is it 'internal'
   * @return true/false
   */
  //public boolean isDisplayable();

  /**
   * Is this field editable in the entities current state?
   */
  //public boolean isEditable();

  /**
   * Is it mandatory for this field to be provided a value in the entities current state?
   * @return true/false
   */
  //public boolean isMandatory();

  public boolean isDisplayable(boolean newEntity);
  public boolean isEditable(boolean newEntity);
  public boolean isMandatory(boolean newEntity);

  /**
   * The class used to store this field
   * (ie: java.lang.String)
   * @return valueClass string
   */
  public String getValueClass();

  public String getElementClass();

  public boolean isCollection();

  public int getConstraintType();

  public IConstraint getConstraint();

  public boolean isVirtualField();

  public boolean isAvailableInCache();
}