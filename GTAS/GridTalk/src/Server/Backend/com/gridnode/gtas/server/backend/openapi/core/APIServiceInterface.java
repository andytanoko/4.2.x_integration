package com.gridnode.gtas.server.backend.openapi.core;

/**
 * Title:        Open API
 * Description:  Open Application Programmer's Interface for GridTalk Server
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode Pte Ltd
 * @author Shannon Koh
 * @version 1.0
 */

//import com.gridnode.openapi.core.*;

public class APIServiceInterface
{
  APIConfig config = null;
  private APIServiceCore core = null;

  public APIServiceInterface()
  {
    this(new APIConfig());
  }

  public APIServiceInterface(APIConfig configSrc)
  {
    core = new APIServiceCore(config);
    setConfig(configSrc);
  }

  public void setConfig(APIConfig configSrc)
  {
    config = configSrc;
    core.setConfig(configSrc);
  }

  public APIParams serviceConnect() throws Exception
  {
    return core.serviceConnect(config.username, config.password);
  }

  public void serviceDisconnect() throws Exception
  {
    core.serviceDisconnect();
  }

  public APIParams performService(int serviceID, APIParams params)
    throws Exception
  {
    return (core.performService(serviceID, params));
  }

  public void registerCallback(IAPICallback callback) throws Exception
  {
    core.registerCallback(callback);
  }
}