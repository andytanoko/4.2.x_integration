/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ImportCertificateAction.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Jan 03 2003    Jagadeesh              Created
 * Mar 26 2004    Guo Jianyu             Added relatedCertUid
 * Jul 28 2006    Tam Wei Xiang          Added isCA. New event response 
 *                                       INVALID_CA_CERTIFICATE_ERROR.
 */


package com.gridnode.gtas.server.certificate.actions;

import com.gridnode.gtas.events.certificate.ImportCertificateEvent;
import com.gridnode.gtas.exceptions.IErrorCode;
import com.gridnode.gtas.model.certificate.CertificateEntityFieldID;
import com.gridnode.gtas.server.certificate.helpers.CertificateLogger;
import com.gridnode.gtas.server.rdm.ejb.actions.AbstractGridTalkAction;
import com.gridnode.pdip.base.certificate.exceptions.DuplicateCertificateException;
import com.gridnode.pdip.base.certificate.exceptions.InvalidCACertificateException;
import com.gridnode.pdip.base.certificate.exceptions.InvalidFileTypeException;
import com.gridnode.pdip.base.certificate.exceptions.InvalidPasswordOrFileTypeException;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerHome;
import com.gridnode.pdip.base.certificate.facade.ejb.ICertificateManagerObj;
import com.gridnode.pdip.base.certificate.helpers.ICertificateFilePathConfig;
import com.gridnode.pdip.base.certificate.model.Certificate;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.exceptions.SystemException;
import com.gridnode.pdip.framework.exceptions.TypedException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;
import com.gridnode.pdip.framework.file.helpers.IPathConfig;
import com.gridnode.pdip.framework.rpf.event.BasicEventResponse;
import com.gridnode.pdip.framework.rpf.event.IEvent;
import com.gridnode.pdip.framework.rpf.event.IEventResponse;
import com.gridnode.pdip.framework.util.ServiceLocator;


public class ImportCertificateAction extends AbstractGridTalkAction
{

  /**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 4659090651100503719L;
	private ImportCertificateEvent _event = null;
  private Certificate _certificate = null;
  private static final String ACTION_NAME = "ImportCertificateAction";

  public IEventResponse doProcess(IEvent event)
    throws Throwable
  {
    _event = (ImportCertificateEvent) event;

    IEventResponse response = null;
    Object[] params = null;
    try
    {
      _certificate = (Certificate) performAction(_event);
      params = new Object[] { _certificate.getEntityName(),
                              _certificate.getEntityDescr()
                            };
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
    Certificate cert = null;
    ImportCertificateEvent certEvent = (ImportCertificateEvent)event;
    String certName    = certEvent.getCertName();
    String passWord    = certEvent.getPassword();
    String certFile    = certEvent.getCertFile();
    Long relatedCertUid = certEvent.getRelatedCertUid();
    String actualFile  = moveCertFile(certFile,passWord);
    Boolean isCA       = certEvent.isCA(); //TWX 28072006
    
    if(passWord == null)
    {
      cert = getManager().importCertificate(certName, actualFile, relatedCertUid,isCA);
    }
    else
    {
      cert = getManager().importCertificate(certName, actualFile, passWord, relatedCertUid);
    }
    return cert;
  }

  private String moveCertFile(String certFile,String passWord)
      throws FileAccessException
  {
    String actualFileName = null;
    try
    {
//      if(passWord == null)
//      {
//        String newFilename = FileUtil. move(IPathConfig.PATH_TEMP,
//                                         _userID+"/in/",
//                                         ICertificateFilePathConfig.PATH_IMPORT_CERTIFICATE,
//                                         "",
//                                         certFile);
//       CertificateLogger.debug("FilePath-->" + newFilename);
//       return newFilename;
//
//      }
//      else
//      {
//        String newFilename = FileUtil.move(IPathConfig.PATH_TEMP,
//                                         _userID+"/in/",
//                                         ICertificateFilePathConfig.PATH_IMPORT_PKCS12,
//                                         "",
//                                         certFile);
//       CertificateLogger.debug("FilePath-->" + newFilename);
//        return newFilename;
//
//      }

      String newFilename = FileUtil. move(IPathConfig.PATH_TEMP,
                                       _userID+"/in/",
                                       ICertificateFilePathConfig.PATH_IMPORT_CERTIFICATE,
                                       "",
                                       certFile);
      CertificateLogger.debug("FilePath-->" + newFilename);
      return newFilename;
    }
    catch(FileAccessException ex)
    {
      CertificateLogger.debug("[CreateProcedureDefFileAction][prepareCreationData]"+
      "Cannot Perform Action"+ex.getMessage() );
      ex.printStackTrace();
      throw ex;
    }

  }


  private IEventResponse constructEventResponse(Object[] params)
  {
    CertificateLogger.debug("[ImportCertificateAction][constructEventResponse] Event Successful ");
    BasicEventResponse response = null;

    response = new BasicEventResponse(
                   IErrorCode.NO_ERROR,
                   params,
                   _certificate.convertToMap(_certificate, CertificateEntityFieldID.getEntityFieldID(), null));
    return response;
  }

  private IEventResponse constructEventResponse(Object[] params, IEvent event, TypedException ex)
  {
    CertificateLogger.warn("[ImportCertificateAction][perform] Event Error ", ex);
    BasicEventResponse response = null;
    short errorCode =  IErrorCode.CREATE_ENTITY_ERROR;
    if(ex instanceof DuplicateCertificateException)
        errorCode = IErrorCode.DUPLICATE_CERTIFICATE_IMPORT_ERROR;
    if(ex instanceof InvalidFileTypeException)
        errorCode = IErrorCode.INVALID_FILETYPE_ERROR;
    if(ex instanceof InvalidPasswordOrFileTypeException)
        errorCode = IErrorCode.INVALID_PASSWORD_OR_FILETYPE_ERROR;
    if(ex instanceof InvalidCACertificateException) //TWX 20060728
    	  errorCode = IErrorCode.INVALID_CA_CERTIFICATE_ERROR;
    
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
             IErrorCode.CREATE_ENTITY_ERROR,
             params,
             ex);
  }


  protected Class getExpectedEventClass()
  {
    return ImportCertificateEvent.class;
  }

  protected String getActionName()
  {
    return ACTION_NAME;
  }


}


