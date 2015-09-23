package com.gridnode.gtas.server.backend.openapi.net;

/**
 * Title:        Open API
 * Description:  Open Application Programmer's Interface for GridTalk Server
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode Pte Ltd
 * @author Shannon Koh
 * @version 1.0
 */

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class APIListener extends Thread
{
  public static final int DEFAULT_PORT = 6763;

  private ServerSocket serverSocket;
  private String ipAddress;
  private int portNum;
  public boolean runListener;

  public APIListener()
  {
    this(DEFAULT_PORT);
  }

  public APIListener(int port)
  {
    System.out.println("Creating Listener");

    try
    {
      System.out.println("Serversocket is "+serverSocket);
      serverSocket = new ServerSocket(port);
      System.out.println("Server Socket Created");
      runListener = true;
      portNum = port;
      ipAddress = (InetAddress.getLocalHost()).getHostAddress();
    }
    catch (Exception e)
    {
      runListener = false;
      System.out.println("Fail to start serversocket");
      e.printStackTrace();
    }
  }

  public int getPortNum()
  {
    return portNum;
  }

  public String getIPAddress()
  {
    return ipAddress;
  }

  public void run()
  {
    try
    {
      Socket socket;
      APIComm comm;

      System.out.println("Running Listener");

      while (true)
      {
        synchronized (this)
        {
          if (!(runListener))
            break;
        }

        socket = serverSocket.accept();
        System.out.println(socket);

        if (runListener)
        {
          System.out.println("Socket connection accepted from " + (socket.getInetAddress()).getHostAddress());
          comm = new APIComm(socket);
          handleConnection(comm);
        }
        else
          socket.close();
      }
    }
    catch (Exception e)
    {
      System.out.println("socketlistening Exception");
      e.printStackTrace();
    }

    System.out.println("Listener Stopped.");
  }

  public void stopListener()
  {
    synchronized (this)
    {
      runListener = false;
    }

    try
    {
      Socket socket = new Socket(ipAddress, portNum);
      socket.close();
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  public void endListener()
  {
    try
    {
      System.out.println("Start to close listener");
      serverSocket.close();
      serverSocket = null;
      runListener = false;
      System.out.println("runListener " + runListener);
    }
    catch(Exception e)
    {
      System.out.println("Fail to close listener");
      e.printStackTrace();
    }
  }
  public abstract void handleConnection(APIComm comm);
}