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
 * Jul 31 2002    Jagadeesh               Created
 * Jan 21 2003    Daniel D'Cotta          Added constants for SOURCE
 * Sep 02 2003    Koh Han Sing            Added new source Attachments
 */


package com.gridnode.pdip.base.userprocedure.model;

public interface IParamDef
{
  public static final String ENTITY_NAME = "ParamDef";

  public static final Number NAME = new Integer(1);

  public static final Number DESCRIPTION = new Integer(2);

  public static final Number SOURCE = new Integer(3);

  public static final Number TYPE = new Integer(4);

  public static final Number VALUE = new Integer(5);

  public static final Number DATE_FORMAT = new Integer(6);

  public static final Number ACTUAL_VALUE = new Integer(7);


  // 20030121 DDJ: Added constants for SOURCE
  public static final Integer SOURCE_USER_DEFINED = new Integer(1);
  public static final Integer SOURCE_GDOC         = new Integer(2);
  public static final Integer SOURCE_UDOC         = new Integer(3);
  public static final Integer SOURCE_ATTACHMENTS  = new Integer(4);
}