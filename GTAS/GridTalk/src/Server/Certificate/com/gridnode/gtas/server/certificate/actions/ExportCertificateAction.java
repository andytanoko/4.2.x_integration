/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ExportCertificateEvent.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jan 03 2003    Jagadeesh               Created
 * Jan 27 2003    Jagadeesh               Modified: To Return Exported File Name.
 * Jan 08 2004    Neo Sok Lay             Use UID instead of Name to identify certificate.
 */

package com.gridnode.gtas.server.certificate.actions;

import java.util.HashMap;

import com.gridnode.gtas.events.certificate.ExportCertificateEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.certificate.ICertificate;
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
 * This class Exports the Certificate, given the CertName and Cert FileName.
 *
 */

public class ExportCertificateAction extends AbstractGridTalkAction
{
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -8692275198330948331L;
	private static final String ACTION_NAME = "ExportCertificateAction";
  private ExportCertificateEvent _event = null;
  //private Certificate _certificate = null;
  private String _exportedCertFileName = null;

  public IEventResponse doProcess(IEvent event) throws Throwable
  {
    _event = (ExportCertificateEvent) event;

    IEventResponse response = null;
    Object[] params = null;
    try
    {
      _exportedCertFileName = (String) performAction(_event);
      params = new Object[] {
      };
      response = constructEventResponse(params);
    }
    catch (TypedException ex)
    {
      response = constructEventResponse(params, _event, ex);
    }
    catch (Throwable ex)
    {
      response =
        constructEventResponse(params, _event, new SystemException(ex));
    }
    return response;
  }

  protected void doSemanticValidation(IEvent event) throws Exception
  {
  }

  private Object performAction(IEvent event) throws Exception
  {
    //Certificate certEntity = null;
    ExportCertificateEvent certEvent = (ExportCertificateEvent) event;
    try
    {
      //String certName    = certEvent.getCertName();
      Long certUid = certEvent.getCertUid();
      String certFile = certEvent.getCertFile();
      String exportedCertificate =
        getManager().exportCertificate(certUid, certFile);
      return exportedCertificate;
    }
    catch (Exception aex)
    {
      throw aex;
    }
  }

  private IEventResponse constructEventResponse(Object[] params)
  {
    CertificateLogger.debug(
      "[ImportCertificateAction][constructEventResponse] Event Successful ");
    BasicEventResponse response = null;

    HashMap map = new HashMap();
    map.put(ICertificate.EXPORT_CERTIFICATE_FILENAME, _exportedCertFileName);
    response = new BasicEventResponse(IErrorCode.NO_ERROR, params, map);
    return response;
  }

  private IEventResponse constructEventResponse(
    Object[] params,
    IEvent event,
    TypedException ex)
  {
    CertificateLogger.warn(
      "[ImportCertificateAction][perform] Event Error ",
      ex);
    BasicEventResponse response = null;
    response =
      new BasicEventResponse(
        IErrorCode.CREATE_ENTITY_ERROR,
        params,
        ex.getType(),
        ex.getLocalizedMessage(),
        ex.getStackTraceString());
    return response;
  }

  private ICertificateManagerObj getManager() throws ServiceLookupException
  {
    return (ICertificateManagerObj) ServiceLocator
      .instance(ServiceLocator.CLIENT_CONTEXT)
      .getObj(
        ICertificateManagerHome.class.getName(),
        ICertificateManagerHome.class,
        new Object[0]);
  }

  protected IEventResponse constructErrorResponse(
    IEvent event,
    TypedException ex)
  {
    Object[] params = new Object[] {
    };

    return constructEventResponse(IErrorCode.CREATE_ENTITY_ERROR, params, ex);
  }

  protected Class getExpectedEventClass()
  {
    return ExportCertificateEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }
}
