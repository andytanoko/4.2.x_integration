package com.gridnode.gtas.server.certificate.actions;

import java.util.HashMap;

import com.gridnode.gtas.events.certificate.SetPrivateCertificatePasswordEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.exceptions.InvalidStateException;
import com.gridnode.gtas.server.certificate.helpers.CertificateLogger;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.base.certificate.exceptions.InvalidPasswordException;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.certificate.model.Certificate;
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
 * File: SetPrivateCertificatePasswordAction.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * April 28 2003    Qingsong              Created
 */

public class SetPrivateCertificatePasswordAction extends AbstractGridTalkAction
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -6150271524986413869L;
	private static final String ACTION_NAME = "SetPrivateCertificatePasswordAction";
  private SetPrivateCertificatePasswordEvent _event = null;
  private String _newPassword = "";

  protected void validateCurrentState()
    throws InvalidStateException
  {
    checkSessionID(false);
    //checkUserID();
  }


  public IEventResponse doProcess(IEvent event)
    throws Throwable
  {
    _event = (SetPrivateCertificatePasswordEvent) event;

    IEventResponse response = null;
    Object[] params = null;
    try
    {
      _newPassword = (String)performAction(_event);
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
    Certificate certEntity = null;
    SetPrivateCertificatePasswordEvent certEvent = (SetPrivateCertificatePasswordEvent)event;
    try
    {
      String NewPass    = certEvent.getPassword();
      getManager().setPrivatePassword(NewPass);
      return NewPass;
    }
    catch(Exception aex)
    {
      throw aex;
    }
  }

  private IEventResponse constructEventResponse(Object[] params)
  {
    CertificateLogger.debug("[SetPrivateCertificatePasswordAction][constructEventResponse] Event Successful ");
    BasicEventResponse response = null;

    HashMap map = new HashMap();
    map.put(SetPrivateCertificatePasswordEvent.PRIVATE_PASSEWORD ,_newPassword);
    response = new BasicEventResponse(
                   IErrorCode.NO_ERROR,
                   params,
                   map);
    return response;
  }

  private IEventResponse constructEventResponse(Object[] params, IEvent event, TypedException ex)
  {
    CertificateLogger.warn("[SetPrivateCertificatePasswordAction][perform] Event Error ", ex);
    short errorCode =  IErrorCode.SET_PRIVATE_CERT_PASSWORD_ERROR;
    if(ex instanceof InvalidPasswordException)
        errorCode = IErrorCode.INCORRECT_PRIVATE_CERT_PASSWORD;

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
             IErrorCode.SET_PRIVATE_CERT_PASSWORD_ERROR,
             params,
             ex);
  }

  protected Class getExpectedEventClass()
  {
    return SetPrivateCertificatePasswordEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }
}