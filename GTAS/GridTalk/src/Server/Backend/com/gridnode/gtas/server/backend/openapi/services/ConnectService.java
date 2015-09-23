package com.gridnode.gtas.server.backend.openapi.services;

import com.gridnode.gtas.events.user.UserLoginEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.backend.openapi.core.APIParams;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;

public class ConnectService extends AbstractService implements IGTAPIService
{
  private static final String CLASS_NAME = "ConnectService";

  public ConnectService(GTServiceFactory factory)
  {
    super(factory);
  }

  public APIParams handleService(APIParams params)
  {
    Object[] returnParam = null;
    try
    {
      Object[] parameters = params.getParamArray();
      String username = (String) parameters[0];
      String password = (String) parameters[1];
      BasicEventResponse response = (BasicEventResponse)handleEvent(new UserLoginEvent(username, password));
      if (IErrorCode.NO_ERROR == response.getMessageCode())
      {
        returnParam = new Object[1];
        returnParam[0] = getSessionId();
        log(CLASS_NAME, "handleService", "Successful, return sessionId:" + returnParam[0]);
      }
      else
        log(CLASS_NAME, "handleService", "Not successful");
    }
    catch (Throwable t)
    {
      warn(CLASS_NAME, "handleService", "Not successful", t);
    }
    //finally
    //{
      return new APIParams(returnParam);
    //}
  }

  private Integer getSessionId()
  {
    return new Integer(1);
  }

}
