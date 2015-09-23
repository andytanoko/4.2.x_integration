package com.gridnode.gtas.server.backend.openapi.core;

/**
 * Title:        Open API
 * Description:  Open Application Programmer's Interface for GridTalk Server
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode Pte Ltd
 * @author Shannon Koh
 * @version 1.0
 */

public interface IAPIService
{
  public APIParams handleService(APIParams params);

  // generic API Service ID
  public static final int CONNECT           = 101;
  public static final int DISCONNECT        = 102;
  public static final int REGCALLBACK       = 103;
  public static final int RECEIVE_CB        = 104;
  public static final int IMPORT            = 105;
  public static final int EXPORT            = 106;
}
