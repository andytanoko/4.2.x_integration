/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISubjectRole.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * May 15 2002    Goh Kan Mun             Created
 */

package com.gridnode.pdip.base.acl.model;

/**
 * This Interface defines the properties and FieldIds for accessing fields
 * in SubjectRole entity.
 *
 * @author Goh Kan Mun
 *
 * @version 2.0
 * @since 2.0
 */


public interface ISubjectRole
{
  /**
   * Name for SubjectRole entity.
   */
  public static final String ENTITY_NAME = "SubjectRole";

  /**
   * FieldId for uid.
   */
  public static final Number UID = new Integer(0); // long

  /**
   * FieldId for subject.
   */
  public static final Number SUBJECT = new Integer(1); // long

  /**
   * FieldId for role.
   */
  public static final Number ROLE = new Integer(2); // long

  /**
   * FieldId for subject_type.
   */
  public static final Number SUBJECT_TYPE = new Integer(3); // String(30)

  /**
   * FieldId for version.
   */
  public static final Number VERSION = new Integer(4); // double

}