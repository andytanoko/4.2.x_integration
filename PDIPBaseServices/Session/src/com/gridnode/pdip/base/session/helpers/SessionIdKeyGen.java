/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SessionIdKeyGen.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * June 14 2002    Ooi Hui Linn         Created
 */

package com.gridnode.pdip.base.session.helpers;

import java.util.Random;

import com.gridnode.pdip.framework.db.keygen.KeyGen;

/**
 * Generate unique Session id.
 * Session id format : String[30], uid (from key-gen) - randomstring
 *
 * @author Ooi Hui Linn
 *
 * @version 2.0
 * @since 2.0
 */
public class SessionIdKeyGen
{
  private static final String KEYNAME = "session_audit.SessionId";
  private static final int SESSION_ID_LENGTH = 30;
  private static final String SEPARATOR = "-";
  private static final String RAND_STRING = "1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final int RAND_VARIABLE = RAND_STRING.length();
  private static Random RAND = new Random();

  public static String getNextId() throws Exception
  {
    return getNextId(KEYNAME, true);
  }

  private static String getNextId(String key, boolean isLocal) throws Exception
  {

    try
    {
      Long keygenUid = KeyGen.getNextId(key, isLocal);

      StringBuffer sessionId = new StringBuffer(SESSION_ID_LENGTH);
      sessionId.append(keygenUid.toString());
      sessionId.append(SEPARATOR);
      randomBuffer(sessionId, SESSION_ID_LENGTH);
      return sessionId.toString();
    }
    catch(Exception ex)
    {
      Logger.warn("[KeyGen.getNextId] Error in getting next UId for " + key, ex);
      throw ex;
    }
  }

  /**
   * Fill the rest of the StringBuffer with randomly generated alphanumeric characters.
   * The StringBuffer will be returned with string length "maxLength".
   *
   * @param buff        String Buffer to append the random characters.
   * @param maxLength   Desired length of resultant string.
   *                    If the StringBuffer is longer than maxLength,
   *                    no changes will be made to buff.
   */
  private static void randomBuffer(StringBuffer buff, int maxLength)
  {
    int numOfChars = maxLength - buff.toString().length();
    int randNum;

    for(int i=0; i<numOfChars; i++)
    {
      randNum = RAND.nextInt(RAND_VARIABLE);
      buff.append(RAND_STRING.charAt(randNum));
    }
  }
}