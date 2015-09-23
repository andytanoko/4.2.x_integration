package com.gridnode.gtas.server.backend.openapi.services;

import java.rmi.RemoteException;

import com.gridnode.gtas.server.backend.helpers.Logger;
import com.gridnode.gtas.server.backend.openapi.core.APIParams;
import com.gridnode.gtas.server.backend.openapi.core.IAPIServiceFactory;
import com.gridnode.gtas.server.rdm.ejb.IGridTalkClientControllerHome;
import com.gridnode.gtas.server.rdm.ejb.IGridTalkClientControllerObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.rpf.event.EventException;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * Title:        Open API
 * Description:  Open Application Programmer's Interface for GridTalk Server
 * Copyright:    Copyright (c) 2001
 * Company:      GridNode Pte Ltd
 * @author Shannon Koh
 * @version 1.0
 */

public class GTServiceFactory implements IAPIServiceFactory
{
  private IGridTalkClientControllerObj _gtClientCtrl = null;

  public GTServiceFactory (boolean sessionNeeded)
         throws ServiceLookupException
  {
    if (sessionNeeded)
    {
      _gtClientCtrl = (IGridTalkClientControllerObj)ServiceLocator.instance(
                      ServiceLocator.CLIENT_CONTEXT).getObj(
                        IGridTalkClientControllerHome.class.getName(),
                        IGridTalkClientControllerHome.class,
                        new Object[0]);
    }
  }

  public IGridTalkClientControllerObj getGridTalkClientController()
  {
    return _gtClientCtrl;
  }

  public APIParams initiateService(int serviceID, APIParams params)
  {
    Logger.debug("[GTServiceFactory.initiateService] serviceID = "+serviceID);
    IGTAPIService service = null;
    APIParams returnParams = null;

    switch (serviceID)
    {
      case IGTAPIService.CONNECT:
        if (_gtClientCtrl != null)
        {
          Logger.debug("[GTServiceFactory.initiateService] gtClientCtrl not null");
          service = new ConnectService(this);
        }
        else
        {
          Logger.debug("[GTServiceFactory.initiateService] gtClientCtrl is null!!!!");
        }
        break;

      case IGTAPIService.DISCONNECT:
        // service = new DisconnectService(); break;

      case IGTAPIService.IMPORT:
        if (_gtClientCtrl != null)
          service = new ImportDocService(this);
        break;

      case IGTAPIService.EXPORT:
        //service = new ExportDocService(); KHS171202
        break;
    }

    if (service != null)
    {
      returnParams = service.handleService(params);
      System.out.println(returnParams);
    }
    return returnParams;
  }

  /**
   * feeds the specified event to the state machine of the business logic.
   *
   * @param ese is the current event
   * @return a EventResponse object which can be extend to carry any payload.
   * @exception com.sun.j2ee.blueprints.waf.event.EventException <description>
   * @exception com.sun.j2ee.blueprints.waf.exceptions.GeneralFailureException
   */
  public final synchronized IEventResponse handleEvent(IEvent event)
      throws EventException, RemoteException, ServiceLookupException
  {
    if(event == null)
    {
      throw new NullPointerException("null event");
    }
    String eventClass = event.getClass().getName();
    IEventResponse response =  _gtClientCtrl.processEvent(event);
    return response;
  }
}