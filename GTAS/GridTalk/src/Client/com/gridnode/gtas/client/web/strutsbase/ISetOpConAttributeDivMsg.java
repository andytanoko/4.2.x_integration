/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ISetOpConAttributeDivMsg.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-07-16     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.strutsbase;

/**
 * Will be interpreted by TaskDispatchAction and used to set the value of an attribute
 * in the OperationContext. A simple implementation of this suitable for general use is the
 * class SetOpConAttributeDivMsg, and the RmocConfirmationFlagDivMsg and DetailViewDivMsg classes
 * will also be refactored to implement this interface and their support in TaskDispatchAction
 * modified to use the common handling.
 */
public interface ISetOpConAttributeDivMsg
{
  /**
   * Returns the identity of the opCon attribute to be set.
   * Nb: opCon attributes may be any object however our convention is to use strings
   * wherever possible. In future it may be that only strings are allowed!
   * @return attribute to set
   */
  public Object getAttribute();
  
  /**
   * Returns the value with which to set the opCon attribute
   * @return value
   */
  public Object getValue();
  
  /**
   * Returns true if this divMsg is to be left in the queue to propogate to further
   * diversions
   * @return propogates
   */
  public boolean isPropagating();
  
  /**
   * Returns a reference to the OperationContext that originated the message.
   * This may be null - or may even refer to a completed operation though it is strongly recomended
   * that operations setting such messages on the backDivMsgQueue do not set pass this value.
   * @return originatingOpCon
   */
  public OperationContext getSource();
}
