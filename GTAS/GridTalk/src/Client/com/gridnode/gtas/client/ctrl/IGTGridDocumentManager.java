/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTGridDocumentManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-22     Andrew Hill         Created
 * 2002-08-27     Andrew Hill         Added importDocuments() method to interface
 * 2003-03-24     Andrew Hill         getAllInFolder() now returns a list pager
 * 2003-11-17     Daniel D'Cotta      Added support for Search
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.client.GTClientException;

public interface IGTGridDocumentManager extends IGTManager
{
  //public IGTListPager getAllInFolder(String folder) throws GTClientException; //20030324AH - Now return ListPager // 20031127 DDJ
  public void importDocuments(IGTImportDocuments importDocs) throws GTClientException;
  public void send(long[] uids) throws GTClientException;
  public void export(long[] uids) throws GTClientException;
  public IGTListPager getSearchQueryListPager(Long searchUid, Number[] sortField, boolean[] sortAscending) throws GTClientException; // 20031127 DDJ
}