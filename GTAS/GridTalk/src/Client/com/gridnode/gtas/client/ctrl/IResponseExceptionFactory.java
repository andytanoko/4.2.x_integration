/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IResponseExceptionFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-07-18    Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;


public interface IResponseExceptionFactory
{
  ResponseException getResponseException(IEventResponse response, IGTSession session, IEvent event)
      throws GTClientException;
}
