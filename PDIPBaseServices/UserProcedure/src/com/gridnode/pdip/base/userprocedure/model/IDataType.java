/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDataType.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jul 30 2002    Jagadeesh               Created
 * Sep 02 2003    Koh Han Sing            Added new data types : DataHandler,
 *                                        DataHandler[], String[]
 * Nov 26 2003    Koh Han Sing            Added new data types : byte[],
 *                                        byte[][]
 */


package com.gridnode.pdip.base.userprocedure.model;


public interface IDataType
{
/**
 * @see     java.lang.Byte
 * @see     java.lang.Double
 * @see     java.lang.Float
 * @see     java.lang.Integer
 * @see     java.lang.Long
 * @see     java.lang.Short
 */

  public static final int DATA_TYPE_STRING   = 1;

  public static final int DATA_TYPE_INTEGER  = 2;

  public static final int DATA_TYPE_LONG     = 3;

  public static final int DATA_TYPE_DOUBLE   = 4;

  public static final int DATA_TYPE_BOOLEAN  = 5;

  public static final int DATA_TYPE_DATE     = 6;

  public static final int DATA_TYPE_OBJECT   = 7;

  public static final int DATA_TYPE_DATAHANDLER         = 8;

  public static final int DATA_TYPE_DATAHANDLER_ARRAY   = 9;

  public static final int DATA_TYPE_STRING_ARRAY        = 10;

  public static final int DATA_TYPE_BYTE_ARRAY          = 11;

  public static final int DATA_TYPE_BYTE_ARRAY_ARRAY    = 12;

}