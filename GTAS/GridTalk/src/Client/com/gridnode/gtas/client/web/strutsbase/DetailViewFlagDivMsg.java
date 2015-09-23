/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DetailViewFlagDivMsg.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-12     Andrew Hill         Created
 * 2003-07-16     Andrew Hill         Support the new ISetOpConAttributeDivMsg interface
 */
package com.gridnode.gtas.client.web.strutsbase;

public class DetailViewFlagDivMsg implements ISetOpConAttributeDivMsg
{
  private OperationContext _source;

  public DetailViewFlagDivMsg(OperationContext source)
  {
    _source = source;
  }

  public OperationContext getSource()
  {
    return _source;
  }
  
  public Object getAttribute()
  { //20030716AH
    return TaskDispatchAction.IS_DETAIL_VIEW;
  }

  public Object getValue()
  { //20030716AH
    return Boolean.TRUE;
  }

  public boolean isPropagating()
  { //20030716AH
    return true;
  }

}