package com.gridnode.gtas.server.certificate.actions;

import java.util.HashMap;

import com.gridnode.gtas.events.certificate.GetPrivateCertificatePasswordStateEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.exceptions.InvalidStateException;
import com.gridnode.gtas.server.certificate.helpers.CertificateLogger;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;

 

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: GetPrivateCertificatePasswordStateAction.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * April 28 2003    Qingsong              Created
 */

public class GetPrivateCertificatePasswordStateAction extends AbstractGridTalkAction
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 867496912686833687L;
	private static final String ACTION_NAME = "GetPrivateCertificatePasswordStateAction";
  private GetPrivateCertificatePasswordStateEvent _event = null;
  private Boolean _issetPassword = new Boolean(false);

  protected void validateCurrentState()
    throws InvalidStateException
  {
    checkSessionID(false);
    //checkUserID();
  }

  public IEventResponse doProcess(IEvent event)
    throws Throwable
  {
    _event = (GetPrivateCertificatePasswordStateEvent) event;

    IEventResponse response = null;
    Object[] params = null;
    try
    {
      _issetPassword = (Boolean)performAction(_event);
      params = new Object[] { };
      response = constructEventResponse(params);
    }
    catch (TypedException ex)
    {
      response = constructEventResponse(params, _event, ex);
    }
    catch (Throwable ex)
    {
      response = constructEventResponse(params, _event, new SystemException(ex));
    }
    return response;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
  }

  private Object performAction(IEvent event)
    throws Exception
  {
    GetPrivateCertificatePasswordStateEvent certEvent = (GetPrivateCertificatePasswordStateEvent)event;
    try
    {
      boolean isset    = getManager().issetPrivatePassword();
      return new Boolean(isset);
    }
    catch(Exception aex)
    {
      throw aex;
    }
  }

  private IEventResponse constructEventResponse(Object[] params)
  {
    CertificateLogger.debug("[GetPrivateCertificatePasswordStateAction][constructEventResponse] Event Successful ");
    BasicEventResponse response = null;

    HashMap map = new HashMap();
    map.put(GetPrivateCertificatePasswordStateEvent.PRIVATE_PASSEWORD_STATE, _issetPassword);
    response = new BasicEventResponse(
                   IErrorCode.NO_ERROR,
                   params,
                   map);
    return response;
  }

  private IEventResponse constructEventResponse(Object[] params, IEvent event, TypedException ex)
  {
    CertificateLogger.warn("[GetPrivateCertificatePasswordStateAction][perform] Event Error ", ex);
    short errorCode =  IErrorCode.GET_PRIVATE_CERT_PASSWORD_STATE_ERROR;
    BasicEventResponse response = null;
    response = new BasicEventResponse(
                 errorCode,
                 params,
                 ex.getType(),
                 ex.getLocalizedMessage(),
                 ex.getStackTraceString());
    return response;
  }

  private ICertificateManagerObj getManager()
    throws ServiceLookupException
  {
    return (ICertificateManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
               ICertificateManagerHome.class.getName(),
               ICertificateManagerHome.class,
               new Object[0]);
  }


  protected IEventResponse constructErrorResponse(IEvent event, TypedException ex)
  {
    Object[] params = new Object[]
                      {
                      };

    return constructEventResponse(
             IErrorCode.GET_PRIVATE_CERT_PASSWORD_STATE_ERROR,
             params,
             ex);
  }

  protected Class getExpectedEventClass()
  {
    return GetPrivateCertificatePasswordStateEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }
}