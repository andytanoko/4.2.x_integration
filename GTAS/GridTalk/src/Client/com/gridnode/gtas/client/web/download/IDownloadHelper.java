/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDownloadHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-20     Andrew Hill         Created
 * 2003-01-27     Andrew Hill         Refactored
 */
package com.gridnode.gtas.client.web.download;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;
 
public interface IDownloadHelper
{
  /**
   * Key for a request parameter that contains the key for a session attribute that
   * contains an IDownloadHelper implementation to be used to assist the download
   * operation.
   * Helpers are stored as session attributes rather than request attributes as its inconvienient
   * (but certainly not impossible) to create it when the download link needs to target a
   * new window - otherwise we would just make the download links submit the form together with
   * info on which file to download, create our helper and do a server side redirect, but this
   * wont work as then we would be opening in same window, and given what weve done to the back
   * button... (It is possible to submit with a target, but I think that this would create other
   * problems... and abusing the session a little more seems easier. May have to refactor later if
   * it turns out this is a very bad idea (as opposed to merely a bad idea ;->))
   */
  public static final String DOWNLOAD_HELPER_ID_KEY = "dlhKey"; //20030127AH


  public void doPreDownload(ActionContext actionContext, DownloadAForm form)
    throws GTClientException;

  public void doPostDownload(ActionContext actionContext, DownloadAForm form)
    throws GTClientException;
}