/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IProcedureType.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jul 31 2002    Jagadeesh               Created
 * Jul 23 2003    Koh Han Sing            Added procedure type SOAP.
 */


package com.gridnode.pdip.base.userprocedure.model;


public interface IProcedureType
{

  public static final int PROC_TYPE_EXEC = 1;

  public static final int PROC_TYPE_JAVA = 2;

  public static final int PROC_TYPE_SOAP = 3;

}