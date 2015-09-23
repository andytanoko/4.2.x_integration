/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 - 2007 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GNAuthenticator.java
 *
 ******************************************************************************
 * Date         Author            Changes
 ******************************************************************************
 * Jan 11 2007  Lim Soon Hsiung   Created
 */
package com.gridnode.util.net;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public final class GNAuthenticator extends Authenticator
{
  private static GNAuthenticator instance;

  /**
       The "Authenticator" for Proxy request i.e., <code>RequestorType.PROXY</code>
   **/
  private GNProxySelector proxySelector;

  private GNAuthenticator()
  {
  }

  private static synchronized void init()
  {
    if (instance == null)
    {
      instance = new GNAuthenticator();
      setDefault(instance);
    }
  }

  /**
   * Called when password authorization is needed.  Subclasses should
   * override the default implementation, which returns null.
   * @return The PasswordAuthentication collected from the
   *		user, or null if none is provided.
   */
  protected PasswordAuthentication getPasswordAuthentication()
  {
    if (getRequestorType() == RequestorType.PROXY && proxySelector != null)
    {
      return proxySelector.requestPasswordAuthentication(getRequestingHost(),
                                                         getRequestingSite(),
                                                         getRequestingPort(),
                                                         getRequestingProtocol(),
                                                         getRequestingPrompt(),
                                                         getRequestingScheme(),
                                                         getRequestingURL());
    }
    else
    {
      System.out.println("No Authenticator configured for request type " + getRequestorType());
    }

    // PasswordAuthentication pa = null;
    // do something about it, no implimentation for now
    // return pa;

    return null;
  }

  /**
   * Register a "Authenticator" for <code>RequestorType.PROXY</code>.
   *
   * @param proxySelector The GNProxySelector that can provide <code>PasswordAuthentication</code> for
   * Proxy authentication
   */
  static void setProxySelector(GNProxySelector proxySelector)
  {
    init();
    instance.proxySelector = proxySelector;
  }
}
