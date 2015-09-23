package com.gridnode.gtas.server.backend.openapi.core;

import com.gridnode.gtas.server.backend.openapi.net.APIComm;
import com.gridnode.gtas.server.backend.openapi.net.APIListener;

/**
 * Title:        Open API
 * Description:  Open Application Programmer's Interface for GridTalk Server
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode Pte Ltd
 * @author Shannon Koh
 * @version 1.0
 */

public class GTAPIServer extends APIListener
{
  public GTAPIServer(int port)
  {
    super(port);
  }

  public void handleConnection(APIComm comm)
  {
    try
    {
      GTAPIHandler apiHandler = new GTAPIHandler(comm);
      apiHandler.start();
    }
    catch (Throwable t)
    {
      t.printStackTrace();
      if (comm != null)
        comm.close();
    }
//    GTHandlerMgr.addHandler(apiHandler); GKM (21/12/2002) Resource Not Released
  }

 /* public static void main(String args[])
  {
    GTAPIServer apiServer = new GTAPIServer();
    System.out.println("Server started at " + apiServer.getIPAddress());
    apiServer.start();
    try { sleep(5000); } catch (Exception e) {}
    apiServer.stopListener();
  }*/
}