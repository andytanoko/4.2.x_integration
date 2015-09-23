/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExceptionUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 13, 2006   i00107              Created
 */

package com.gridnode.util;

import java.io.CharArrayWriter;
import java.io.PrintWriter;

/**
 * @author i00107
 * This class provides utilities to manipulate exceptions.
 */
public class ExceptionUtil
{

  public static String getStackStraceStr(Throwable t)
  {
    CharArrayWriter writer = new CharArrayWriter();
    t.printStackTrace(new PrintWriter(writer, true));
    return writer.toString();
  }
}
