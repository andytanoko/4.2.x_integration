/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: SignOnResourceManager.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 15 2002    Neo Sok Lay         Created
 * Jul 28 2003    Neo Sok Lay         Multi-threading issue.
 */
package com.gridnode.pdip.app.user.login;

import java.util.Hashtable;

import com.gridnode.pdip.app.user.helpers.Logger;

/**
 * Manages the SignOnResource objects allocated for each session.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.1
 * @since 2.0
 */
public class SignOnResourceManager
{
  private static final SignOnResourceManager _self = new SignOnResourceManager();

  private Hashtable _resourceTable;

  private SignOnResourceManager()
  {
    _resourceTable = new Hashtable();
  }

  /**
   * Get an instance of this SignOnResourceManager manager.
   */
  public static SignOnResourceManager getInstance()
  {
    /*030728NSL: Instantiate self as "final". 
    if (_self == null)
    {
      _self = new SignOnResourceManager();
      _self._resourceTable = new Hashtable();
    }
    */
    return _self;
  }

  /**
   * Add a SignOnResource object to manage. This will overwrite any SignOnResource
   * object having the same Session ID.
   *
   * @param resource The SignOnResource object.
   *
   * @since 2.0
   */
  public void addSignOnResource(SignOnResource resource)
  {
    Logger.debug("[SignOnResourceManager.addSignOnResource] Adding for session: "+
      resource.getSessionID());
    _resourceTable.put(resource.getSessionID(), resource);
  }

  /**
   * Removes a SignOnResource object.
   *
   * @param sessionID the SessionID of the SignOnResource.
   *
   * @since 2.0
   */
  public void removeSignOnResource(String sessionID)
  {
    SignOnResource resource = getSignOnResource(sessionID);
    if (resource != null)
    {
      Logger.debug("[SignOnResourceManager.removeSignOnResource] Removing for session: "+
        sessionID);
      _resourceTable.remove(sessionID);
    }
  }

  /**
   * Get the SignOnResource object for a session.
   * @param sessionID The sessionID of the session.
   */
  public SignOnResource getSignOnResource(String sessionID)
  {
    SignOnResource found = (SignOnResource)_resourceTable.get(sessionID);
    return found;
  }
}