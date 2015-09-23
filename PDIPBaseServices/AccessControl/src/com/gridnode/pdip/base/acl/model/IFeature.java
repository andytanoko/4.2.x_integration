/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IFeature.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 20 2002    Goh Kan Mun             Created
 */

package com.gridnode.pdip.base.acl.model;

/**
 * This Interface defines the properties and FieldIds for accessing fields
 * in Feature entity.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public interface IFeature
{
  /**
   * Name for Role entity.
   */
  public static final String ENTITY_NAME = "Feature";

  /**
   * FieldId for uid.
   */
  public static final Number UID = new Integer(0); // bigint

  /**
   * FieldId for role.
   */
  public static final Number FEATURE = new Integer(1); // String 30

  /**
   * FieldId for description.
   */
  public static final Number DESCRIPTION = new Integer(2); // String 80

  /**
   * FieldId for actions.
   */
  public static final Number ACTIONS = new Integer(3); // text

  /**
   * FieldId for data types.
   */
  public static final Number DATA_TYPES = new Integer(4); // text

  /**
   * FieldId for data types.
   */
  public static final Number VERSION = new Integer(5); // double


}