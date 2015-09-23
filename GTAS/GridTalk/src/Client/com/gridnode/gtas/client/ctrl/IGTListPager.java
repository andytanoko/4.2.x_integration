/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTListPager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-03-20     Andrew Hill         Created
 * 2003-03-31     Andrew Hill         Default pageSize now comes from GlobalContext
 */
package com.gridnode.gtas.client.ctrl;

import java.util.Collection;

import com.gridnode.gtas.client.GTClientException;

public interface IGTListPager
{
  public static final int UNKNOWN = -1;
  //public static final int DEFAULT_PAGE_SIZE = 6;

  public IGTManager getManager();
  public IGTSession getSession();
  public void free();

  public Collection getPage() throws GTClientException;
  public int getPageSize();
  public void setPageSize(int pageSize);
  public int getPageStart();
  public void setPageStart(int pageStart);
  public int getTotalItemCount();

  public boolean nextPage();
  public boolean prevPage();
  public boolean hasNext();
  public boolean hasPrev();
}