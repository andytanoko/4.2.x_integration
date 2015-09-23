/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AutoConnectDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Feb 6, 2004    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.connection.connect;

import com.gridnode.gtas.server.connection.exceptions.ConnectionException;
import com.gridnode.gtas.server.connection.helpers.Logger;

/**
 * Perform Auto-connect to GridMaster
 * 
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class AutoConnectDelegate extends ConnectDelegate
{
  private ConnectionContext.ConnectionToken _myToken;

  /**
   * Constructor for AutoConnectDelegate
   * 
   * @param ctx
   * @throws Throwable
   */
  public AutoConnectDelegate(ConnectionContext ctx) throws Throwable
  {
    super(ctx);
  }


  /**
   * @see com.gridnode.gtas.server.connection.connect.IConnectionSenderDelegate#execute()
   */
  public void execute() throws Throwable
  {
    Logger.debug("[AutoConnectDelegate.execute] Enter");

    try
    {
      boolean autoconnect = _helper.getProperties().getAutoConnectOnStartup();
      if (!autoconnect)
      {
        Logger.log("[AutoConnectDelegate.execute] Autoconnection not configured. No auto-connect attempt will be made.");
        return;
      }

      try
      {
        super.execute();
      }
      catch (Throwable e)
      {
        throw new ConnectionException("Unable to auto-connect to GridMaster", e);
      }
    }
    finally
    {
      Logger.debug("[AutoConnectDelegate.execute] Exit");
    }
    
  }

}
