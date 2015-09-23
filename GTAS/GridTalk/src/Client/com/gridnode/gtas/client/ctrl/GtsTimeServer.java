/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GtsTimeServer.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-09     Andrew Hill         Created
 * 2003-03-03     Andrew Hill         Defer retrieval of utcOffset until first use
 */
package com.gridnode.gtas.client.ctrl;

import org.apache.commons.logging.*;

import com.gridnode.gtas.client.GTClientException;

import com.gridnode.pdip.framework.util.ITimeServer;
import com.gridnode.gtas.events.locale.GetUtcTimeEvent;

class GtsTimeServer implements ITimeServer
{
  private static final Log _log = LogFactory.getLog(GtsTimeServer.class); // 20031209 DDJ

  private DefaultGTSession _session;
  //private long _lastOffset;
  private Long _lastOffset;

  /**
   * @deprecated
   */
  public GtsTimeServer()
  {
    throw new java.lang.UnsupportedOperationException("Noargs constructor not supported");
  }

  GtsTimeServer(DefaultGTSession session)
    throws GTClientException
  {
    _session = session;
    //updateUtcOffset(); 20030303AH - No longer init immediately
    _lastOffset = null; //20030303AH
  }

  synchronized void updateUtcOffset() throws GTClientException
  {
    try
    {
      GetUtcTimeEvent event = new GetUtcTimeEvent();
      Object[] result = (Object[])_session.fireEvent(event);
      Long utcOffset = (Long)result[0];
      if(utcOffset == null)
      {
        throw new NullPointerException("No utcOffset value returned from GTAS");
      }
      else
      {
        setUtcOffset(utcOffset.longValue());
      }
    }
    catch(Throwable t)
    {
      throw new GTClientException("Error updating utcOffset value",t);
    }
  }

  void setUtcOffset(long utcOffset)
  {
    if(_log.isInfoEnabled())
    {
      _log.info("Setting utcOffset to " + utcOffset);
    }
    _lastOffset = new Long(utcOffset); //20030303AH
  }

  public long getUtcOffset()
  {
    if(_lastOffset == null)
    { //20030303AH - Lookup on first use
      synchronized(this)
      {
        try
        {
          updateUtcOffset();
        }
        catch(Throwable t)
        {
          throw new java.lang.RuntimeException("Error updating utcOffset");
        }
      }
    }
    return _lastOffset.longValue(); //20030303AH
  }
}