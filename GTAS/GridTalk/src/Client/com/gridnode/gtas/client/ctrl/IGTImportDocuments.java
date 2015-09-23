/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTImportDocuments.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-08-24     Andrew Hill         Created
 * 2002-12-05     Andrew Hill         Added attachments field
 */
package com.gridnode.gtas.client.ctrl;


/**
 * A virtual entity used to assist in collating the information necesarry for an import
 * and for passing this to the GridDocument manager to perform the import operation.
 */
public interface IGTImportDocuments extends IGTEntity
{
  public static final Number SENDER_ID = new Integer(-10);
  public static final Number DOC_TYPE = new Integer(-20);
  public static final Number FILENAMES = new Integer(-30);
  public static final Number RECIPIENTS = new Integer(-40);
  public static final Number IS_MANUAL = new Integer(-50);
  public static final Number G_DOC_UIDS = new Integer(-60);
  public static final Number ATTACHMENTS = new Integer(-70);
}