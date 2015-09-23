/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GTSessionFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2002-05-11     Andrew Hill         Created
 * 2002-10-24     Andrew Hill         Require user to provide a global context
 */
package com.gridnode.gtas.client.ctrl;

/**
 * Entry point to the com.gridnode.gtas.client.ctrl API.
 * Factory class with static method to return IGTSession implementing object.
 * Used to create an instance of an IGTSession based on the session type passed to the
 * factory method. The session type is used within the package to instantiate different
 * implementing classes based on the special requirements of the session type.
 */
public class GTSessionFactory
{
  /**
   * Create a new IGTSession instance given the client type.
   * Valid values for client types are CLIENT_FAKE
   * The extraData parameter is not currently used.
   *
   * @param int sessionType
   * @param Object extraData required for this type of session
   * @throws SessionCreationException
   * @return IGTSession object
   */
  public static synchronized IGTSession  getSession(int sessionType, GlobalContext context)
                            throws SessionCreationException
  {
    if(context == null)
    {
      throw new SessionCreationException(SessionCreationException.NO_CONTEXT,"No global context");
    }
    try
    {
      if(sessionType == IGTSession.SESSION_DEFAULT)
      {
        DefaultGTSession session = new DefaultGTSession(sessionType, context);
        session.init();
        return session;
      }
    }
    catch(Throwable t)
    {
      throw new SessionCreationException(SessionCreationException.GURU_MEDITATION,"Error creating session",t);
    }
    // If execution reaches this point without returning then client type is unknown.
    throw new SessionCreationException(SessionCreationException.UNKNOWN_SESSION_TYPE,
                                       "Unknown session type: " + sessionType);
  }
}