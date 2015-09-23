package com.gridnode.pdip.base.security.mime.smime;

import com.gridnode.pdip.base.security.mime.smime.helpers.GNSMimeBasic;
import com.gridnode.pdip.base.security.mime.SMimeFactory;

/**
 * <p>Title:  * This software is the proprietary information of GridNode Pte Ltd.
 * <p>Description: Peer Data Integration Platform
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: GridNode Pte Ltd</p>
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 10 Nov 2003    Zou Qingsong        Created. *
 */

public class SMimeFactory2
{
  SMimeFactory2()
  {
  }
  static public ISMimePackager getSMimePackager(String application, SMimeFactory factory)
  {
    return new GNSMimeBasic(factory);
  }
  static public ISMimeDePackager getSMimeDePackager(String application, SMimeFactory factory)
  {
    return new GNSMimeBasic(factory);
  }
}