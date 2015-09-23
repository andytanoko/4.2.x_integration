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
 * File: ExportCertificateTrustStoreAction.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * April 28 2003    Qingsong              Created
 */

public class ExportCertificateTrustStoreAction extends AbstractGridTalkAction
{
 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3636700767125021374L;
	private static final String ACTION_NAME = "ExportCertificateTrustStoreAction";
  private ExportCertificateTrustStoreEvent _event = null;
  private String _exportedCertName = null;

  public IEventResponse doProcess(IEvent event)
    throws Throwable
  {
    _event = (ExportCertificateTrustStoreEvent) event;

    IEventResponse response = null;
    Object[] params = null;
    try
    {
      _exportedCertName = (String)performAction(_event);
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
    ExportCertificateTrustStoreEvent certEvent = (ExportCertificateTrustStoreEvent)event;
    try
    {
      Long uid    = certEvent.getCertificateUID();
      String exportedCertName = getManager().exportCertificateTrustStore(uid);
      return exportedCertName;
    }
    catch(Exception aex)
    {
      throw aex;
    }
  }

  private IEventResponse constructEventResponse(Object[] params)
  {
    CertificateLogger.debug("[ExportCertificateTrustStoreAction][constructEventResponse] Event Successful ");
    BasicEventResponse response = null;

    HashMap map = new HashMap();
    map.put(ICertificate.NAME,_exportedCertName);
    response = new BasicEventResponse(
                   IErrorCode.NO_ERROR,
                   params,
                   map);
    return response;
  }

  private IEventResponse constructEventResponse(Object[] params, IEvent event, TypedException ex)
  {
    CertificateLogger.warn("[ExportCertificateTrustStoreAction][perform] Event Error ", ex);
    BasicEventResponse response = null;
    response = new BasicEventResponse(
                 IErrorCode.EXPORT_CERTIFICATE_TRUSTSTORE_ERROR,
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
             IErrorCode.EXPORT_CERTIFICATE_TRUSTSTORE_ERROR,
             params,
             ex);
  }

  protected Class getExpectedEventClass()
  {
    return ExportCertificateTrustStoreEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }
}