package com.gridnode.gtas.server.backend.openapi.core;

/**
 * Title:        Open API
 * Description:  Open Application Programmer's Interface for GridTalk Server
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode Pte Ltd
 * @author Shannon Koh
 * @version 1.0
 */

public interface IAPIServiceFactory
{
  public APIParams initiateService(int serviceID, APIParams params);
}