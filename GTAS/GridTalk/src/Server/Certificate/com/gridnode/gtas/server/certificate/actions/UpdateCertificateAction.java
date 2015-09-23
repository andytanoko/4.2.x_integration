package com.gridnode.gtas.server.certificate.actions;

import java.util.*;

import com.gridnode.gtas.events.certificate.*;
import com.gridnode.gtas.exceptions.*;
import com.gridnode.gtas.server.certificate.helpers.*;
import com.gridnode.gtas.server.rdm.ejb.actions.*;
import com.gridnode.pdip.base.certificate.facade.ejb.*;
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
 * File: UpdateCertificateAction.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * March 29 2004  Guo Jianyu              Created
 * Aug 01 2008	  Wong Yee Wah			 #38   Added: line no. 79-80, get Swap Date and Swap time from event
 */

public class UpdateCertificateAction extends AbstractGridTalkAction
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -5048544826588256160L;
	private static final String ACTION_NAME = "UpdateCertificateAction";
  private UpdateCertificateEvent _event = null;
  private String _CertName = null;

  public IEventResponse doProcess(IEvent event)
    throws Throwable
  {
    _event = (UpdateCertificateEvent) event;

    IEventResponse response = null;
    Object[] params = null;
    try
    {
      _CertName = (String)performAction(_event);
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
    UpdateCertificateEvent certEvent = (UpdateCertificateEvent)event;
    try
    {
      Long uid    = certEvent.getCertificateUID();
      String NewName    = certEvent.getNewName();
      Long relatedCertUid = certEvent.getRelatedCertUid();
      String swapDate = certEvent.getSwapDate();
      String swapTime = certEvent.getSwapTime();
      ICertificateManagerObj mgr = getManager();
      mgr.updateCertificate(uid, NewName, relatedCertUid, swapDate, swapTime);
      return NewName;
    }
    catch(Exception aex)
    {
      throw aex;
    }
  }

  private IEventResponse constructEventResponse(Object[] params)
  {
    CertificateLogger.debug("[UpdateCertificateAction][constructEventResponse] Event Successful ");
    BasicEventResponse response = null;

    HashMap map = new HashMap();
    map.put(ICertificate.NAME,_CertName);
    response = new BasicEventResponse(
                   IErrorCode.NO_ERROR,
                   params,
                   map);
    return response;
  }

  private IEventResponse constructEventResponse(Object[] params, IEvent event, TypedException ex)
  {
    CertificateLogger.warn("[UpdateCertificateAction][perform] Event Error ", ex);
    BasicEventResponse response = null;
    response = new BasicEventResponse(
                 IErrorCode.CHANGE_CERTIFICATE_NAME_ERROR,
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
             IErrorCode.CHANGE_CERTIFICATE_NAME_ERROR,
             params,
             ex);
  }

  protected Class getExpectedEventClass()
  {
    return UpdateCertificateEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }
}