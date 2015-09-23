/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDocumentEvents.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Sep 30 2002    Koh Han Sing        Created
 * Jan 14 2003    Neo Sok Lay         Add events for upload/download
 */
package com.gridnode.gtas.server.document.helpers;


public interface IDocumentEvents
{
  /**
   * Send Grid Document. (GT1->GT2)
   */
  public static final String SEND_GRIDDOC = "3200";

  /**
   * Send Grid Document response. (GT2->GT1)
   */
  public static final String SEND_GRIDDOC_ACK = "3201";

  /**
   * Event for Upload Grid Document (GT1->GM)
   */
  public static final String UPLOAD_GRIDDOC   = "3202";

  /**
   * Response for Upload Grid Document (GM->GT1)
   */
  public static final String UPLOAD_GRIDDOC_ACK = "3203";

  /**
   * Event for Download Grid Document (GM->GT2)
   */
  public static final String DOWNLOAD_GRIDDOC   = "3204";

  /**
   * Response for Download Grid Document (GT2->GM)
   */
  public static final String DOWNLOAD_GRIDDOC_ACK = "3205";

  /**
   * Event for Informing Grid Document downloaded completed by partner (GM->GT1)
   */
  public static final String TRANS_COMPLETED      = "3206";

  /**
   * Response for Grid Document downloaded completed by partner event (GT1->GM)
   */
  public static final String TRANS_COMPLETED_ACK  = "3207";

  /**
   * Sub id for Grid Document.
   */
  public static final String GRIDDOC_SUB_EVENT       = "51200";
}