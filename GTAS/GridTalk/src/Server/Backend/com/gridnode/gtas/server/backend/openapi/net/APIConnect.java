package com.gridnode.gtas.server.backend.openapi.net;

/**
 * Title:        Open API
 * Description:  Open Application Programmer's Interface for GridTalk Server
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode Pte Ltd
 * @author Shannon Koh
 * @version 1.0
 */

import com.gridnode.gtas.server.backend.openapi.core.APIConfig;

import java.net.Socket;

public class APIConnect
{
  private APIComm comm = null;

  public APIConnect(APIConfig config)
  {
    this(config.getServerIP(), config.getServerPort());
  }

  public APIConnect(String serverIP, int serverPort)
  {
    boolean notConnected = true;
    int retries = 0;
    while (notConnected)
    {
      retries++;
      try
      {
        Socket socket = new Socket(serverIP, serverPort);
        if (socket != null)
          comm = new APIComm(socket);
        else
          comm = null;

        notConnected = false;
      }
      catch (Exception e)
      {
        try
        {
          Thread.sleep(10000);
        }
        catch (InterruptedException ex)
        {}
        if (retries > 30)
        {
          e.printStackTrace();
          break;
        }
      }
    }
  }

  public APIComm getCommunication()
  {
    return comm;
  }
}