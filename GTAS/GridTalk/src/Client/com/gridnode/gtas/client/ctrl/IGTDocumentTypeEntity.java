/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGTDocumentTypeEntity.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-06-21     Andrew Hill         Created
 */
package com.gridnode.gtas.client.ctrl;

import com.gridnode.gtas.model.document.IDocumentType;

public interface IGTDocumentTypeEntity extends IGTEntity
{
  public static final Number UID          = IDocumentType.UID;
  public static final Number DOC_TYPE     = IDocumentType.DOC_TYPE;
  public static final Number DESCRIPTION  = IDocumentType.DESCRIPTION;
  public static final Number CAN_DELETE   = IDocumentType.CAN_DELETE;
}