/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultResponseExceptionFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-07-18     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.DeleteEntityListEvent;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;

class DefaultResponseExceptionFactory implements IResponseExceptionFactory 
{
  private static DefaultResponseExceptionFactory _instance;
  
  private DefaultResponseExceptionFactory()
  {
    //singleton
  }
  
  public static synchronized DefaultResponseExceptionFactory getInstance()
  {
    if(_instance == null)
    {
      _instance = new DefaultResponseExceptionFactory();
    }
    return _instance;
  }
  
  public ResponseException getResponseException(IEventResponse response,
                                                IGTSession session,
                                                IEvent event)
    throws GTClientException
  {
    if (response == null)
      throw new NullPointerException("response is null");
    if (session == null)
      throw new NullPointerException("session is null");
    if (event == null)
      throw new NullPointerException("event is null");
    try
    {
      BasicEventResponse ber = (BasicEventResponse)response;
      DefaultGTSession gtSession = (DefaultGTSession)session;
      ResponseException responseException = getResponseException(ber, gtSession, event);
      responseException.setEvent(event);
      return responseException;
    }
    catch(Throwable t)
    {
      throw new GTClientException("Exception occured preparing a ResponseException for failed event "
                                  + event.getClass().getName(),t);
    }
  }
  
  protected ResponseException getResponseException(BasicEventResponse response,
                                                    DefaultGTSession session,
                                                    IEvent event)
    throws GTClientException
  {
    if( (event instanceof DeleteEntityListEvent) && (response.getReturnData() != null) )
    {
      try
      {
        return DeleteException.newDeleteException(response);
      }
      catch(Throwable t)
      {
        throw new GTClientException("Unable to instantiate DeleteException for response:" + response,t);
      }  
    }
    else
    {
      try
      {
        return new ResponseException(response);
      }
      catch(Throwable t)
      {
        throw new GTClientException("Unable to instantiate ResponseException for response:" + response,t);
      } 
    }
  }

}
