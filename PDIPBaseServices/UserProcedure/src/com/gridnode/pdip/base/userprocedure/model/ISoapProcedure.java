/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISoapProcedure.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jul 23 2003    Koh Han Sing            Created
 * Del 01 2003    Koh Han Sing            Add in username and password
 */


package com.gridnode.pdip.base.userprocedure.model;

public interface ISoapProcedure
{
  public static final String ENTITY_NAME = "SoapProcedure";

  public static final Number METHOD_NAME = new Integer(0);

  public static final Number TYPE = new Integer(1);

  public static final Number USER_NAME = new Integer(2);

  public static final Number PASSWORD = new Integer(3);
}