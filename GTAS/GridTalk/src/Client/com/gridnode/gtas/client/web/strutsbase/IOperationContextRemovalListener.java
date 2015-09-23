/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IOperationContextRemovalListener.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-28     Andrew Hill         Created
 */
package com.gridnode.gtas.client.web.strutsbase;

import com.gridnode.gtas.client.GTClientException;

import javax.servlet.http.*;

public interface IOperationContextRemovalListener
{
  /**
   * This method is called when the OperationContext is removed.
   * @param request
   * @param opCon
   * @throws GTClientException
   */
  public void  onRemoveOpCon(HttpServletRequest request, OperationContext opCon)
    throws GTClientException;
}