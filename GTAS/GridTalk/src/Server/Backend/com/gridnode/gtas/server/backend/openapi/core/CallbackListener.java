package com.gridnode.gtas.server.backend.openapi.core;

import com.gridnode.gtas.server.backend.openapi.net.APIComm;
import com.gridnode.gtas.server.backend.openapi.net.APIListener;

import java.io.ObjectInputStream;

/**
 * Title:        Open API
 * Description:  Open Application Programmer's Interface for GridTalk Server
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode Pte Ltd
 * @author Shannon Koh
 * @version 1.0
 */

public class CallbackListener extends APIListener
{
  public static final int DEFAULT_CALLBACK_PORT = 6963;
  private IAPICallback callback = null;

  public CallbackListener(int port)
  {
    super(port);
  }

  public CallbackListener(IAPICallback callbackObj, int port)
  {
    super(port);
    callback = callbackObj;
  }

  public CallbackListener(IAPICallback callbackObj)
  {
    super(DEFAULT_CALLBACK_PORT);
    callback = callbackObj;
  }

  public void handleConnection(APIComm comm)
  {
    synchronized (this)
    {
      try
      {
        ObjectInputStream input = comm.getInput();

        Integer serviceID = (Integer)(input.readObject());
        APIParams params   = (APIParams)(input.readObject());
        Boolean hasFileArray = (Boolean)(input.readObject());
        System.out.println("hasFileArray = " + hasFileArray);
        if(hasFileArray.booleanValue())
        {
          System.out.println("Has files");
          params.readFiles(comm);
        }

        if (callback != null)
          callback.handleCallback(serviceID.intValue(), params);
        else
          throw (new Exception("Callback unregistered: is null."));
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
  }
}