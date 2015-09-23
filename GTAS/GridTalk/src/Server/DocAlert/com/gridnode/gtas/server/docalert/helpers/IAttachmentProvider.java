/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAttachmentProvider.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 23, 2005   i00107             Created
 */

package com.gridnode.gtas.server.docalert.helpers;

import java.io.Serializable;

import com.gridnode.gtas.server.document.model.GridDocument;

public interface IAttachmentProvider extends Serializable
{

  public String getAlertAttachment(GridDocument gdoc) throws Exception;
}
