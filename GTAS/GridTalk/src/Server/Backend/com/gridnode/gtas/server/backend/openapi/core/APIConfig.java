package com.gridnode.gtas.server.backend.openapi.core;

/**
 * Title:        Open API
 * Description:  Open Application Programmer's Interface for GridTalk Server
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode Pte Ltd
 * @author Shannon Koh
 * @version 1.0
 */

public class APIConfig
{
  String serverIP = "127.0.0.1";
  int serverPort = 6763;
  String username = "GridTalk";
  String password = "1.0";

  public APIConfig()
  {
//    this("127.0.0.1", 6763, "GridTalk", "1.0");
  }

  public APIConfig(
    String serverIPSrc,
    int serverPortSrc,
    String usernameSrc,
    String passwordSrc)
  {
    serverIP = serverIPSrc;
    serverPort = serverPortSrc;
    username = usernameSrc;
    password = passwordSrc;
  }

  public String getUserName()
  {
    return username;
  }

  public String getServerIP()
  {
    return serverIP;
  }

  public int getServerPort()
  {
    return serverPort;
  }
}