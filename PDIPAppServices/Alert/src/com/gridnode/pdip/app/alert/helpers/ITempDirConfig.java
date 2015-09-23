/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ITempDirConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Dec 06 2002   Srinath R         Created
 * Jan 23 2003   Neo Sok Lay       Keep directory config keys.
 * Apr 16 2003   Neo Sok Lay          Add EMAIL_DIR.
 * May 14 2003   Neo Sok Lay          Add LOG_DIR.
 */
package com.gridnode.pdip.app.alert.helpers;

import com.gridnode.pdip.framework.file.helpers.IPathConfig;

public interface ITempDirConfig extends IPathConfig
{
    //public static final String TEMP_DIR_CONFIG = "tempdir.properties";
    //public static final String TEMP_DIR = "temp.dir";
  /**
   * Key for attachment directory. For use with FileUtil.
   */
  public static final String ATTACHMENT_DIR = "alert.path.attachment";

  /**
   * Key for email directory. For use with FileUtil.
   */
  public static final String EMAIL_DIR      = "alert.path.email";

  /**
   * Key for log directory. For use with FileUtil.
   */
  public static final String LOG_DIR      = "alert.path.log";

}