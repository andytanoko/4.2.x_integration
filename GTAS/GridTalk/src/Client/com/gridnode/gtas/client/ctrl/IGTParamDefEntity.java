/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTParamDefEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-10-16     Daniel D'Cotta      Created
 * 2003-01-21     Daniel D'Cotta      Added constants for Source
 * 2003-11-28     Koh Han Sing        Added new data type byte[], byte[][]
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.userprocedure.IDataType;
import com.gridnode.gtas.model.userprocedure.IParamDef;
 
// These entities are embedded in a field of IGTUserProcedureEntity whose manager
// is thus responsible for them.
public interface IGTParamDefEntity extends IGTEntity
{
  // Data Type (Enumeration for PARAM_LIST_TYPE)
  public static final Integer DATA_TYPE_BOOLEAN           = IDataType.DATA_TYPE_BOOLEAN;
  public static final Integer DATA_TYPE_DATE              = IDataType.DATA_TYPE_DATE;
  public static final Integer DATA_TYPE_DOUBLE            = IDataType.DATA_TYPE_DOUBLE;
  public static final Integer DATA_TYPE_INTEGER           = IDataType.DATA_TYPE_INTEGER;
  public static final Integer DATA_TYPE_LONG              = IDataType.DATA_TYPE_LONG;
  public static final Integer DATA_TYPE_OBJECT            = IDataType.DATA_TYPE_OBJECT;
  public static final Integer DATA_TYPE_STRING            = IDataType.DATA_TYPE_STRING;
  public static final Integer DATA_TYPE_STRING_ARRAY      = IDataType.DATA_TYPE_STRING_ARRAY;
  public static final Integer DATA_TYPE_DATAHANDLER       = IDataType.DATA_TYPE_DATAHANDLER;
  public static final Integer DATA_TYPE_DATAHANDLER_ARRAY = IDataType.DATA_TYPE_DATAHANDLER_ARRAY;
  public static final Integer DATA_TYPE_BYTE_ARRAY        = IDataType.DATA_TYPE_BYTE_ARRAY;
  public static final Integer DATA_TYPE_BYTE_ARRAY_ARRAY  = IDataType.DATA_TYPE_BYTE_ARRAY_ARRAY;

  // Source (Enumeration for PARAM_LIST_SOURCE)
  public static final Integer SOURCE_USER_DEFINED         = IParamDef.SOURCE_USER_DEFINED;
  public static final Integer SOURCE_GDOC                 = IParamDef.SOURCE_GDOC;
  public static final Integer SOURCE_UDOC                 = IParamDef.SOURCE_UDOC;
  public static final Integer SOURCE_ATTACHMENTS          = IParamDef.SOURCE_ATTACHMENTS;

  // fields
  public static final Number PARAM_LIST_NAME              = IParamDef.NAME;
  public static final Number PARAM_LIST_DESCRIPTION       = IParamDef.DESCRIPTION;
  public static final Number PARAM_LIST_SOURCE            = IParamDef.SOURCE;
  public static final Number PARAM_LIST_TYPE              = IParamDef.TYPE;
  public static final Number PARAM_LIST_VALUE             = IParamDef.VALUE;
  public static final Number PARAM_LIST_DATE_FORMAT       = IParamDef.DATE_FORMAT;
}
