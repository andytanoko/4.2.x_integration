/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GtasFileClient.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Mar 09, 2007   i00107              Created
 */

package com.gridnode.gridtalk.httpbc.ishb.senders;

import java.io.IOException;

import com.gridnode.util.SystemUtil;
import com.gridnode.util.io.FileClient;


/**
 * @author i00107
 * This class handles transferring of files to Gtas user temp directory.
 */
public class GtasFileClient extends FileClient
{
  /**
   * Construct a GtasFileClient object instance 
   * 
   * @param props The properties that should contain the destination root folder for files to be transferred to.
   */
  public GtasFileClient(GtasLoginConfig props) throws IOException
  {
    super(SystemUtil.getWorkingDirPath() + "/" + props.getGtasUserRootDir());
  }

}
