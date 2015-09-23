/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GTWatchListActionMapping.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-26     Andrew Hill         Created (Moved Pwl refresh attributes from GTActionMapping)
 */
package com.gridnode.gtas.client.web.strutsbase;


public class GTWatchListActionMapping extends GTActionMapping
{
  long _pwlRefreshInterval = 0; //20030116AH

  public void setPwlRefreshInterval(long interval)
  { _pwlRefreshInterval = interval; }

  public long getPwlRefreshInterval()
  { return _pwlRefreshInterval; }

}