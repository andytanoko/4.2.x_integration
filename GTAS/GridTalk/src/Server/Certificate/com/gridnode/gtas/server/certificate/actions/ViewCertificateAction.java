/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ViewCertificateAction.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jan 03 2003    Jagadeesh              Created
 * Jan 08 2004    Neo Sok Lay             Use UID to displayX500Names for existing cert.
 * Jul 26 2006    Tam Wei Xiang           Add in serialNum, startDate, endDate into the
 *                                        event response.
 */

package com.gridnode.gtas.server.certificate.actions;

import com.gridnode.gtas.events.certificate.ViewCertificateEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.certificate.ICertificate;
import com.gridnode.gtas.model.certificate.IX500Name;
import com.gridnode.gtas.server.certificate.helpers.CertificateLogger;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.base.certificate.exceptions.InvalidFileTypeException;
import com.gridnode.pdip.base.certificate.exceptions.InvalidPasswordOrFileTypeException;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.certificate.helpers.GridCertUtilities;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;

import java.io.File;
import java.util.HashMap;
import java.util.Vector;

public class ViewCertificateAction extends AbstractGridTalkAction
{ 
  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = -3517431477928096310L;
	private ViewCertificateEvent _event = null;
  private Vector _viewCertList = null;
  private static final String ACTION_NAME = "ViewCertificateAction";

  public IEventResponse doProcess(IEvent event) throws Throwable
  {
    _event = (ViewCertificateEvent) event;

    IEventResponse response = null;
    Object[] params = null;
    try
    {
      _viewCertList = (Vector) prepareActionData(_event);
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

  private Object prepareActionData(IEvent event) throws Exception
  {
    ViewCertificateEvent certEvent = (ViewCertificateEvent) event;
    Vector certList = null;
    try
    {
      String password = certEvent.getPassword();
      Boolean isFile = certEvent.isFile();

      if (isFile != null && isFile.booleanValue())
      {
        File certFile =
          FileUtil.getFile(
            IPathConfig.PATH_TEMP,
            _userID + "/in/",
            certEvent.getName());
        
        certList =
          getManager().getX500NamesAndCertDetail(certFile.getAbsolutePath(), password);

        certFile.delete();
      }
      else
      {
        certList = getManager().getX500NamesAndCertDetail(certEvent.getUId());
      }
      
      //TWX 26072006 Convert the serial number into hex format
      byte[] serialNumInByte = (byte[])certList.get(2);
      certList.set(2, convertByteToHex(serialNumInByte));
    }
    catch (Exception aex)
    {
      throw aex;
    }
    return certList;
  }

  //26072005 TWX
  private String convertByteToHex(byte[] byteArray)
  {
  	StringBuilder serialNumInHex = new StringBuilder();
  	for(int i = 0; byteArray != null && byteArray.length > i; i++)
  	{
  		serialNumInHex.append(GridCertUtilities.hexEncode(byteArray[i]));
  		if(! ((i+1) > byteArray.length) )
  		{
  			serialNumInHex.append(" ");
  		}
  	}
  	return serialNumInHex.toString();
  }
  
  private IEventResponse constructEventResponse(Object[] params)
  {
    CertificateLogger.debug(
      "[ImportCertificateAction][constructEventResponse] Event Successful ");
    BasicEventResponse response = null;
    HashMap map = new HashMap();
    map.put(IX500Name.ISSUERNAMES_ENTITY_NAME, _viewCertList.get(0));
    map.put(IX500Name.SUBJECT_ENTITY_NAME, _viewCertList.get(1));
    
    //TWX 26072006 to include cert's serialNum, startDate, endDate
    map.put(ICertificate.SERIAL_NUM_FIELD, _viewCertList.get(2));
    map.put(ICertificate.START_DATE_FIELD, _viewCertList.get(3));
    map.put(ICertificate.END_DATE_FIELD, _viewCertList.get(4));
    
    response = new BasicEventResponse(IErrorCode.NO_ERROR, params, map);
    CertificateLogger.log("Values are :" + map);
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
    short errorCode = IErrorCode.FIND_ENTITY_BY_KEY_ERROR;
    if (ex instanceof InvalidFileTypeException)
      errorCode = IErrorCode.INVALID_FILETYPE_ERROR;
    if (ex instanceof InvalidPasswordOrFileTypeException)
      errorCode = IErrorCode.INVALID_PASSWORD_OR_FILETYPE_ERROR;

    response =
      new BasicEventResponse(
        errorCode,
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

    return constructEventResponse(
      IErrorCode.FIND_ENTITY_BY_KEY_ERROR,
      params,
      ex);
  }

  protected Class getExpectedEventClass()
  {
    return ViewCertificateEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }

  //  private String moveToTemp(String certFile) throws Exception
  //  {
  //    String newFilename = FileUtil. move(IPathConfig.PATH_TEMP,
  //                                     _userID+"/in/",
  //                                     IPathConfig.PATH_TEMP,
  //                                     "",
  //                                     certFile);
  //    return newFilename;
  //  }
}
