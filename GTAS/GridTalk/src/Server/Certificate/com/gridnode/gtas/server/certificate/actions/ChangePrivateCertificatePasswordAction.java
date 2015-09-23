package com.gridnode.gtas.server.certificate.actions;

import java.util.*;

import com.gridnode.gtas.events.certificate.*;
import com.gridnode.gtas.exceptions.*;
import com.gridnode.gtas.server.certificate.helpers.*;
import com.gridnode.gtas.server.rdm.ejb.actions.*;
import com.gridnode.pdip.base.certificate.facade.ejb.*;
import com.gridnode.pdip.base.certificate.exceptions.*;
import com.gridnode.pdip.base.certificate.model.*;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.rpf.event.*;
import com.gridnode.pdip.framework.util.*;

/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ChangePrivateCertificatePasswordAction.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * April 28 2003    Qingsong              Created
 */

public class ChangePrivateCertificatePasswordAction extends AbstractGridTalkAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5271526686478604748L;
	private static final String ACTION_NAME = "ChangePrivateCertificatePasswordAction";
  private ChangePrivateCertificatePasswordEvent _event = null;
  private String _newPassword = "";

  public IEventResponse doProcess(IEvent event)
    throws Throwable
  {
    _event = (ChangePrivateCertificatePasswordEvent) event;

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
    ChangePrivateCertificatePasswordEvent certEvent = (ChangePrivateCertificatePasswordEvent)event;
    try
    {
      String OldPass    = certEvent.getOldPassword();
      String NewPass    = certEvent.getNewPassword();
      getManager().changePrivatePassword(OldPass, NewPass);
      return NewPass;
    }
    catch(Exception aex)
    {
      throw aex;
    }
  }

  private IEventResponse constructEventResponse(Object[] params)
  {
    CertificateLogger.debug("[ChangePrivateCertificatePasswordAction][constructEventResponse] Event Successful ");
    BasicEventResponse response = null;

    HashMap map = new HashMap();
    map.put(ChangePrivateCertificatePasswordEvent.PRIVATE_PASSEWORD_NEW ,_newPassword);
    response = new BasicEventResponse(
                   IErrorCode.NO_ERROR,
                   params,
                   map);
    return response;
  }

  private IEventResponse constructEventResponse(Object[] params, IEvent event, TypedException ex)
  {
    CertificateLogger.warn("[ChangePrivateCertificatePasswordAction][perform] Event Error ", ex);
    short errorCode =  IErrorCode.CHANGE_PRIVATE_CERT_PASSWORD_ERROR;
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
             IErrorCode.CHANGE_PRIVATE_CERT_PASSWORD_ERROR,
             params,
             ex);
  }

  protected Class getExpectedEventClass()
  {
    return ChangePrivateCertificatePasswordEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }
}