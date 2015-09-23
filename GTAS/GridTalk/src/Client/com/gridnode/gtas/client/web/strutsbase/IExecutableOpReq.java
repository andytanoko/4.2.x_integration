/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IExecutableOpReq.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-27     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.strutsbase;

import com.gridnode.gtas.client.GTClientException;
 
/**
 * Interface for OpReqs that can be executed (command pattern).
 */
public interface IExecutableOpReq extends IOpReqDivMsg
{
  /**
   * This method will be called when the opReqQueue is processed during operation completion
   * by the OperationDispatchAction.
   * If returns false, will ALSO be passed to subclass processOpReq() method. If returns true
   * it is assumed further processing of this opReq is not required. If false it is removed
   * from the opReqQueue when this call returns.
   * @param actrionContext
   * @param operationDispatchAction
   * @param opCon
   * @return processingCompleted
   * @throws GTClientException
   */
  public boolean execute( ActionContext actionContext,
                          OperationDispatchAction oda,
                          OperationContext opCon)
    throws GTClientException;
}