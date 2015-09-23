/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IGFPathConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jul 29 2002    Daniel D'Cotta      Created (Moved from gtas-common project)
 */
package com.gridnode.pdip.app.gridform.helpers;

import com.gridnode.pdip.framework.file.helpers.IPathConfig;

public interface IGFPathConfig extends IPathConfig
{
  // GridForm paths
  public static final String PATH_LDF  = "gridform.path.ldf";
  public static final String PATH_LTF  = "gridform.path.ltf";
}