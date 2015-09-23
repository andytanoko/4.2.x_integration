/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: RmocConfirmationFlagDivMsg.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-02-21     Andrew Hill         Created
 * 2003-07-16     Andrew Hill         Implement the new ISetOpConAttributeDivMsg interface
 */
package com.gridnode.gtas.client.web.strutsbase;

/**
 * This divMsg acts as a flag to lower diversions that a diversion higher up the stack is in
 * 'update' mode and may have unsaved changes. Knowing this the renderers (CommonContentRenderer
 * usually) can ask the user for confirmation should they try to navigate to a link that
 * would result in removal of the operation (via the rmoc parameter in the link which is
 * picked up by the OperationContextFilter). Code in the TaskDispatchActions processForwardDivMsg
 * method will set a flag in the operation context for the renderers to check for.
 */
public class RmocConfirmationFlagDivMsg implements ISetOpConAttributeDivMsg
{
  private OperationContext _source;

  public RmocConfirmationFlagDivMsg(OperationContext source)
  {
    _source = source;
  }

  public OperationContext getSource()
  {
    return _source;
  }
  
  public Object getAttribute()
  { //20030716AH
    return TaskDispatchAction.FLAG_RMOC_CONFIRMATION;
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