package com.gridnode.gtas.server.backend.openapi.services;

import com.gridnode.gtas.server.backend.openapi.core.IAPIService;

/**
 * Title:        Open API
 * Description:  Open Application Programmer's Interface for GridTalk Server
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode Pte Ltd
 * @author Shannon Koh
 * @version 1.0
 */

public interface IGTAPIService extends IAPIService
{
  //public APIParams handleService(APIParams params);

  // GridFolder Services
  public static final int GETDOC          = 301;
  public static final int PUTDOC          = 302;
  public static final int SYNCHRONIZE     = 303;
  public static final int CREATEFOLDER    = 304;
  public static final int DELETEFOLDER    = 305;
  public static final int SEARCHFOLDER    = 306;


}