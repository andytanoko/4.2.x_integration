/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SimpleDownloadHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-20     Andrew Hill         Created
 * 2003-01-27     Andrew Hill         Refactored (Rather empty now!)
 */
package com.gridnode.gtas.client.web.download;

import com.gridnode.gtas.client.GTClientException;
import com.gridnode.gtas.client.web.strutsbase.ActionContext;

public class SimpleDownloadHelper implements IDownloadHelper
{

  public void doPreDownload(ActionContext actionContext, DownloadAForm form)
    throws GTClientException
  {
    
  }

  public void doPostDownload(ActionContext actionContext, DownloadAForm form)
    throws GTClientException
  {
    
  }

}