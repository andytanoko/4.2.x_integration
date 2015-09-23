package com.gridnode.gtas.server.backend.openapi.net;

/**
 * Title:        Open API
 * Description:  Open Application Programmer's Interface for GridTalk Server
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode Pte Ltd
 * @author Shannon Koh
 * @version 1.0
 */

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class APIComm
{
  private Socket socket;
  private ObjectInputStream input;
  private ObjectOutputStream output;

  public APIComm(Socket socketSrc) throws Exception
  {
    try
    {
    socket = socketSrc;
    output = new ObjectOutputStream(socket.getOutputStream());
    output.flush();
    input = new ObjectInputStream(socket.getInputStream());
    System.out.println(output);
    }
    catch(Exception ex)
    {
     ex.printStackTrace(System.out);
     System.out.println("fail to create outputstream");
     }
  }

  public ObjectInputStream getInput()
  {
    return input;
  }

  public ObjectOutputStream getOutput()
  {
    return output;
  }

  public void close()
  {
    try
    {
      input.close();
      output.close();
      socket.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}