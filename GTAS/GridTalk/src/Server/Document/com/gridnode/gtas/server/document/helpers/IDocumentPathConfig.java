/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IDocumentPathConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 14 2002    Koh Han Sing        Create
 * Apr 13 2004    Guo Jianyu          Added PATH_AUDIT
 */
package com.gridnode.gtas.server.document.helpers;

import com.gridnode.pdip.framework.file.helpers.IPathConfig;

public interface IDocumentPathConfig extends IPathConfig
{
  // Documents path
  public static final String PATH_GDOC             = "document.path.gdoc";
  public static final String PATH_UDOC             = "document.path.udoc";
  public static final String PATH_ATTACHMENT       = "document.path.attachment";
  public static final String PATH_AUDIT            = "common.path.audit";
}