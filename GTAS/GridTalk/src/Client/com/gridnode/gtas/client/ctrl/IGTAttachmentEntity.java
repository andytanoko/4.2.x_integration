/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTAttachmentEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-12-09     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.document.IAttachment;

public interface IGTAttachmentEntity extends IGTEntity
{
  public static final Number UID                = IAttachment.UID;
  public static final Number FILENAME           = IAttachment.FILENAME;
  public static final Number ORIGINAL_FILENAME  = IAttachment.ORIGINAL_FILENAME;

}