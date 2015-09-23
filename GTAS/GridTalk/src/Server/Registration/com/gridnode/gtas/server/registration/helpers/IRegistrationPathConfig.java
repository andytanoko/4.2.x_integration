/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IRegistrationPathConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 09 2003    Koh Han Sing        Create
 */
package com.gridnode.gtas.server.registration.helpers;

import com.gridnode.pdip.framework.file.helpers.IPathConfig;

public interface IRegistrationPathConfig extends IPathConfig
{
  public static final String PATH_CERT           = "registration.path.cert";
  public static final String PATH_LICENSE        = "registration.path.license";
}