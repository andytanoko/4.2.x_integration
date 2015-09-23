/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: StringUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 27 2003    Neo Sok Lay         Created
 */
package com.gridnode.ext.util;

import java.util.StringTokenizer;

/**
 * Utility for string manipulations.
 * 
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class StringUtil
{
  /**
   * Checks if a string value is blank, i.e. <b>null</b> or contains
   * only whitespaces.
   * 
   * @param str The string value to check.
   * @return <b>true</b> if <code>str</code> is blank, <b>false</b> otherwise.
   */
  public static boolean isBlank(String str)
  {
    return str == null || str.trim().length() == 0;
  }

  /**
   * Return the left substring of a string before a string token.
   * 
   * @param str The string.
   * @param token The string token.
   * @return The left substring, or empty string if the <code>token</code> 
   * is not found in <code>str</code>.
   */
  public static String left(String str, String token)
  {
    int pos = str.indexOf(token);

    return (pos > -1) ? str.substring(0, pos) : "";
  }

  /**
   * Return the right substring of a string after a string token.
   * 
   * @param str The string.
   * @param token The string token.
   * @return The right substring, or empty string if <code>token</code>
   * is not found in <code>str</code>.
   */
  public static String right(String str, String token)
  {
    int pos = str.indexOf(token);
    int tokenEndPos = pos + token.length();

    return (pos > -1) ? str.substring(tokenEndPos) : "";
  }

  /**
   * Split a string into tokens based on the specified delimiters.
   * 
   * @param str The string to split.
   * @param delims The delimiters.
   * @return Array of tokens after the splitting, excluding the delimiters.
   */
  public static String[] split(String str, String delims)
  {
    StringTokenizer st = new StringTokenizer(str, delims);
    String[] tokens = new String[st.countTokens()];
    for (int i = 0; i < tokens.length; i++)
    {
      tokens[i] = st.nextToken();
    }

    return tokens;
  }

  /**
   * Substitute a region of a string with another string.
   * 
   * @param str The string.
   * @param match The region to be substituted.
   * @param replace The string to take the place of <code>match</code>
   * if <code>match</code> is found in <code>str</code>.
   * @return The result of the substitution if <code>match</code> is
   * found in <code>str</code>, or <code>str</code> otherwise. 
   */
  public static String substitute(String str, String match, String replace)
  {
    StringBuffer buff = new StringBuffer();
    int matchStartPos = str.indexOf(match);
    String substr = str;
    while (matchStartPos > -1)
    {
      buff.append(substr.substring(0, matchStartPos));
      buff.append(replace);
      substr = substr.substring(matchStartPos + match.length());
      matchStartPos = substr.indexOf(match);
    }
    buff.append(substr);
    return buff.toString();
  }
}