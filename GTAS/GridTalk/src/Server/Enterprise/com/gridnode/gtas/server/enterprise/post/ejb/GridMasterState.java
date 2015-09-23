/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GridMasterState.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 06 2002    Neo Sok Lay         Created
 * Jan 23 2003    Neo Sok Lay         Add my GNCI.
 */
package com.gridnode.gtas.server.enterprise.post.ejb;

 
/**
 * Keeps the current state of the GridMaster connection: whether GridMaster
 * is online/offline, and if online what is its GridNodeID.
 *
 * @author Neo Sok Lay
 *
 * @version 2.0 I7
 * @since 2.0 I6
 */
public class GridMasterState
{
  private static GridMasterState _self = null;
  private static final Object    _lock = new Object();
  private String _currLock = null;

  private String _gmNodeID;
  private boolean _gmOnline;
  private String _myGNCI;

  private GridMasterState()
  {
  }

  public static GridMasterState getInstance()
  {
    if (_self == null)
    {
      synchronized(_lock)
      {
        if (_self == null)
          _self = new GridMasterState();
      }
    }

    return _self;
  }

  synchronized void setGridMaster(
    String gmNodeID,
    boolean online,
    String myGNCI)
  {
    _gmNodeID = gmNodeID;
    _gmOnline = online;
    _myGNCI   = myGNCI;
  }

  boolean isOnline()
  {
    return _gmOnline;
  }

  String getGmNodeID()
  {
    return _gmNodeID;
  }

  String getMyGNCI()
  {
    return _myGNCI;
  }

  synchronized void lock()
  {
    _currLock = String.valueOf(System.currentTimeMillis());
  }

  synchronized void releaseLock()
  {
    _currLock = null;
  }

  synchronized boolean isLocked()
  {
    return _currLock != null;
  }
}