/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DivMsgList.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-03     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.strutsbase;

 
public class OpReqDivMsgWrapper implements IOpReqDivMsg
{
  private Object _opReq;

  public OpReqDivMsgWrapper(Object opReq)
  {
    if (opReq == null)
      throw new NullPointerException("opReq is null"); //20030424AH
    _opReq = opReq;
  }

  public Object getOpReq()
  {
    return _opReq;
  }
}