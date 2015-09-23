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
 * Oct 23 2002    Daniel D'Cotta          Created
 * Sep 02 2003    Koh Han Sing            Added new data types : DataHandler,
 *                                        DataHandler[], String[]
 * Nov 26 2003    Koh Han Sing            Added new data types : byte[],
 *                                        byte[][]
 */
package com.gridnode.gtas.model.userprocedure;

public interface IDataType
{
  public static final Integer DATA_TYPE_STRING   = new Integer(1);

  public static final Integer DATA_TYPE_INTEGER  = new Integer(2);

  public static final Integer DATA_TYPE_LONG     = new Integer(3);

  public static final Integer DATA_TYPE_DOUBLE   = new Integer(4);

  public static final Integer DATA_TYPE_BOOLEAN  = new Integer(5);

  public static final Integer DATA_TYPE_DATE     = new Integer(6);

  public static final Integer DATA_TYPE_OBJECT   = new Integer(7);

  public static final Integer DATA_TYPE_DATAHANDLER         = new Integer(8);

  public static final Integer DATA_TYPE_DATAHANDLER_ARRAY   = new Integer(9);

  public static final Integer DATA_TYPE_STRING_ARRAY        = new Integer(10);

  public static final Integer DATA_TYPE_BYTE_ARRAY          = new Integer(11);

  public static final Integer DATA_TYPE_BYTE_ARRAY_ARRAY    = new Integer(12);
}