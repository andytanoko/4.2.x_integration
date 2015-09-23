/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRequestKeys.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-15     Andrew Hill         Created
 * 2002-09-02     Andrew Hill         Removed some unused keys
 * 2002-10-25     Andrew Hill         Add exception keys
 * 2003-01-27     Andrew Hill         Refer to DownloadAForm for DOMAIN and FILE_PATH
 * 2003-07-25     Daniel D'Cotta      added
 */
package com.gridnode.gtas.client.web;

import com.gridnode.gtas.client.web.download.DownloadAForm;
import com.gridnode.gtas.client.web.download.SaveGridDocumentAForm;

public interface IRequestKeys
{
  public static final String ENTITY               = "com.gridnode.gtas.ctrl.IGTEntity";
  public static final String RENDERERS            = "com.gridnode.gtas.web.renderers.IRenderingPipeline";

  // For use by downloading files
  // These are the names of parameters expected by the download servlet.
  public static final String DOMAIN               = DownloadAForm.DOMAIN; //20030127AH
  public static final String FILE_PATH            = DownloadAForm.FILE_PATH; //20030127AH
  public static final String GRID_DOC_ID          = SaveGridDocumentAForm.GRID_DOC_ID; // 20030725 DDJ
  public static final String GAIA_DOC_ID          = SaveGridDocumentAForm.GAIA_DOC_ID; // 20030725 DDJ

  public static final String SAVE_ROOT_EXCEPTION = "request_root-exception";
  public static final String SAVE_EXCEPTION = "request_exception";
  public static final String REQUEST_ROOT_EXCEPTION = SAVE_ROOT_EXCEPTION;
  public static final String REQUEST_EXCEPTION = SAVE_EXCEPTION;
}