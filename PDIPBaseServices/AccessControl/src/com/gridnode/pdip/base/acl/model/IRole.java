/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRole.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 07 2002    Goh Kan Mun             Created
 */

package com.gridnode.pdip.base.acl.model;

/**
 * This Interface defines the properties and FieldIds for accessing fields
 * in Role entity.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */

public interface IRole
{
  /**
   * Name for Role entity.
   */
  public static final String ENTITY_NAME = "Role";

  /**
   * FieldId for uid.
   */
  public static final Number UID = new Integer(0); // int

  /**
   * FieldId for role.
   */
  public static final Number ROLE = new Integer(1); // String 30

  /**
   * FieldId for description.
   */
  public static final Number DESCRIPTION = new Integer(2); // String 80

  /**
   * FieldId for can_delete.
   */
  public static final Number CAN_DELETE = new Integer(3); // boolean

  /**
   * FieldId for version.
   */
  public static final Number VERSION = new Integer(4); // double

}