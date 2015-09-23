/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IPartnerFunctionPathConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Aug 14 2002    Koh Han Sing        Create
 */
package com.gridnode.gtas.server.partnerfunction.helpers;

import com.gridnode.pdip.framework.file.helpers.IPathConfig;

public interface IPartnerFunctionPathConfig extends IPathConfig
{
  // PartnerFunction paths
  public static final String PATH_XPDL  = "partnerfunction.path.xpdl";
  public static final String PATH_XSL   = "partnerfunction.path.xsl";
}