/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IParamDef.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * 2002-10-23     Daniel D'Cotta          Created
 * 2003-01-21     Daniel D'Cotta          Added constants for SOURCE
 * Sep 02 2003    Koh Han Sing            Added new source Attachments
 */
package com.gridnode.gtas.model.userprocedure;

/**
 * This Interface defines the properties and FieldIds for accessing fields
 * in UserProcedure entity.
 *
 * @author Jagadeesh.
 *
 * @version 2.0
 * @since 2.0
 */
public interface IParamDef
{
  public static final String  ENTITY_NAME = "ParamDef";

  public static final Number NAME = new Integer(1);

  public static final Number DESCRIPTION = new Integer(2);

  public static final Number SOURCE = new Integer(3);

  public static final Number TYPE = new Integer(4);

  public static final Number VALUE = new Integer(5);

  // 20030121 DDJ: Added constants for SOURCE
  public static final Integer SOURCE_USER_DEFINED = new Integer(1);
  public static final Integer SOURCE_GDOC         = new Integer(2);
  public static final Integer SOURCE_UDOC         = new Integer(3);
  public static final Integer SOURCE_ATTACHMENTS  = new Integer(4);

  public static final Number DATE_FORMAT = new Integer(6);

  public static final Number ACTUAL_VALUE = new Integer(7);

  public static final String DATA_SOURCE_USERDEFINED="USERDEFINED";

  public static final String DATA_SOURCE_GRIDDOC="GRIDDOC";

  public static final String DATA_SOURCE_UDOC = "UDOC";

  public static final String DATA_SOURCE_ATTACHMENTS = "ATTACHMENTS";
}