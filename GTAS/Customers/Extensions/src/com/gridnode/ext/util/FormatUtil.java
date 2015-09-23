/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: FormatUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Apr 14 2003    Neo Sok Lay         Created
 * May 06 2003    Neo Sok Lay         Add util to tokenize a string and return
 *                                    one of the tokens.
 */
package com.gridnode.ext.util;

import java.util.StringTokenizer;

public class FormatUtil
{
  public static String replace(String str, char oldChar, char newChar)
  {
    String newString = str.replace(oldChar, newChar);

    return newString ;
  }

  public static String getToken(String str, char delim, int tokenIdx)
  {
    StringTokenizer st = new StringTokenizer(str, String.valueOf(delim), false);

    int counter = 0;

    while (counter < tokenIdx && st.hasMoreTokens())
    {
      st.nextToken();
      counter++;
    }

    String token = "";
    if (st.hasMoreTokens())
    {
      token = st.nextToken();
    }

    return token;
  }
}
