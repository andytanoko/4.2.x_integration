/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractGTSession.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-12     Andrew Hill         Created
 * 2002-10-24     Andrew Hill         GlobalContext
 * 2002-11-26     Andrew Hill         getUserUid()
 * 2003-04-25     Andrew Hill         Allow client to specify if wants pcpKnown check on login
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

abstract class AbstractGTSession implements IGTSession
{
  protected String _userId;
  protected boolean _isLoggedIn = false;
  protected int _sessionType;
  protected IGTHostInfo _hostInfo;
  protected IGTManagerFactory _managerFactory;
  protected GlobalContext _context;

  public AbstractGTSession(int sessionType, GlobalContext context)
  {
    _sessionType = sessionType;
    if(context == null)
    {
      throw new java.lang.NullPointerException("Null context");
    }
    else
    {
      _context = context;
    }
  }

  GlobalContext getContext()
  {
    return _context;
  }

  public boolean isLoggedIn()
  {
    return _isLoggedIn;
  }

  public abstract void login(IGTHostInfo host, String userName, char[] password, boolean checkPcpKnown)
    throws GTClientException;

  public abstract void logout() throws GTClientException;

  public int getSessionType()
  {
    return _sessionType;
  }

  public String getUserId()
  {
    return _userId;
  }

  public abstract IGTUserEntity getUser() throws GTClientException;
  public abstract Long getUserUid() throws GTClientException;

  public IGTHostInfo getHostInfo()
  {
    return _hostInfo;
  }

  public String toString()
  {
    return "GridTalk Session (" + getSessionType() + ")";
  }

}