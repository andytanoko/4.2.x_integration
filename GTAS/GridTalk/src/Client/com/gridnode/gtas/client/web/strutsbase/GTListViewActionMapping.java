package com.gridnode.gtas.client.web.strutsbase;
/*
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GTListViewActionMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-11     Andrew Hill         Created
 * 2003-08-18     Andrew Hill         create,delete,update mappings now specified indirectly + check if frozen
 */


/**
 * ActionMapping class for use with EntityListView actions.
 * Extends the default struts SessionActionMapping class to provide support for configuring the
 * necessary information for list view refreshing. While you could also use this for a non
 * listview action mapping there is no point...
 */
public class GTListViewActionMapping extends GTActionMapping
{
  public static final String DEFAULT_CREATE_FORWARD = "invokeCreate"; //20030818AH
  public static final String DEFAULT_UPDATE_FORWARD = "invokeUpdate"; //20030818AH
  public static final String DEFAULT_VIEW_FORWARD   = "invokeView";   //20030818AH
  public static final String DEFAULT_DELETE_FORWARD = "invokeDelete"; //20030818AH
  
  private long _refreshInterval = 0;
  private String _refreshUrl;
  private String _createForward = DEFAULT_CREATE_FORWARD; //20030818AH
  private String _updateForward = DEFAULT_UPDATE_FORWARD; //20030818AH
  private String _viewForward   = DEFAULT_VIEW_FORWARD;   //20030818AH
  private String _deleteForward = DEFAULT_DELETE_FORWARD; //20030818AH

  public void setRefreshInterval(long interval)
  {
    if (configured)
    { //20030818AH
      throw new IllegalStateException("Configuration is frozen");
    }
    _refreshInterval = interval;
  }

  public long getRefreshInterval()
  { return _refreshInterval; }

  public void setRefreshUrl(String url)
  {
    if (configured)
    { //20030818AH
      throw new IllegalStateException("Configuration is frozen");
    }
    _refreshUrl = url;
  }

  public String getRefreshUrl()
  { return _refreshUrl; }
  
  public String getCreateForward()
  {
    return _createForward;
  }

  public String getDeleteForward()
  {
    return _deleteForward;
  }

  public String getUpdateForward()
  {
    return _updateForward;
  }

  public String getViewForward()
  {
    return _viewForward;
  }

  public void setCreateForward(String string)
  { //20030818AH
    if (configured)
    { 
      throw new IllegalStateException("Configuration is frozen");
    }
    _createForward = string;
  }

  public void setDeleteForward(String string)
  { //20030818AH
    if (configured)
    { 
      throw new IllegalStateException("Configuration is frozen");
    }
    _deleteForward = string;
  }

  public void setUpdateForward(String string)
  { //20030818AH
    if (configured)
    { 
      throw new IllegalStateException("Configuration is frozen");
    }
    _updateForward = string;
  }

  public void setViewForward(String string)
  { //20030818AH
    if (configured)
    { 
      throw new IllegalStateException("Configuration is frozen");
    }
    _viewForward = string;
  }

}