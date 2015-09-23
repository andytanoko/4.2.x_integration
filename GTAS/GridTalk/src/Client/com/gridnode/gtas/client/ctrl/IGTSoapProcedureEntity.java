/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTSoapProcedureEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-07-30     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.userprocedure.IProcedureType;
import com.gridnode.gtas.model.userprocedure.ISoapProcedure;

//Dynamic entity in a userProcedure
public interface IGTSoapProcedureEntity extends IGTEntity
{
  public static final Integer TYPE_EXECUTABLE           = IProcedureType.PROC_TYPE_EXEC;
  public static final Integer TYPE_JAVA                 = IProcedureType.PROC_TYPE_JAVA;
  public static final Integer TYPE_SOAP                 = IProcedureType.PROC_TYPE_SOAP;

  // Fields
  public static final Number SOAP_METHOD_NAME           = ISoapProcedure.METHOD_NAME;
  public static final Number SOAP_TYPE                  = ISoapProcedure.TYPE;
  public static final Number SOAP_USER_NAME             = new Integer(2);//ISoapProcedure.; // 20031205 DDJ
  public static final Number SOAP_PASSWORD              = new Integer(3);//ISoapProcedure.; // 20031205 DDJ 
}
