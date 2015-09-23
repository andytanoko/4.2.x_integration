/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractReceiveRNSignalProc.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 25 2003    Neo Sok Lay             Created
 */
package com.infineon.userproc;

import com.infineon.userproc.helpers.Logger;
import com.infineon.userproc.helpers.ServiceLookupHelper;
import com.infineon.userproc.helpers.NotificationConfig;

import com.gridnode.gtas.server.userprocedure.exceptions.AbortUserProcedureException;
import com.gridnode.gtas.server.rnif.model.RNProfile;
import com.gridnode.gtas.server.rnif.helpers.ProcessInstanceActionHelper;
import com.gridnode.gtas.server.rnif.helpers.IRnifConstant;
import com.gridnode.gtas.server.rnif.model.ProcessData;
import com.gridnode.gtas.server.document.model.GridDocument;

import com.gridnode.pdip.app.alert.providers.IDataProvider;

import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.log.FacadeLogger;

import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an abstract User Procedure for sending email notification
 * on receiving a RN Signal document. A signal document can be an RN Acknowledgement
 * or an RN Exception.
 * <p>
 */
public abstract class AbstractReceiveRNSignalProc
{
  private NotificationConfig _config = null;

  public AbstractReceiveRNSignalProc()
  {
    _config = NotificationConfig.getNotificationConfig(getConfigFileName());
  }

  protected abstract String getConfigFileName();
  protected abstract String getClassName();

  /**
   * The sent RN document is determined based on the ProcessInstanceID and
   * ProcessOriginatorID of the RN Acknowledgement. If a sent RN document is
   * found, and if the document type is configured, the corresponding configured
   * Alert will be raised and the email code xpath (optional) will be used to extract
   * the email code from the sent RN document. The extracted email code will be used
   * to map to user role and all users of that role (with email address configured)
   * will be sent an email notification.
   *
   * @param gdocObj The GridDocument object for the received document.
   * @throws AbortUserProcedureException Application error while executing the method.
   * @throws SystemException System error while executing the method.
   */
  public void sendEmailNotification(GridDocument gdocObj) throws Exception
  {
    FacadeLogger logger = FacadeLogger.getLogger(
                            getClassName(),
                            Logger.CATEGORY_MAIN);
    String methodName   = "sendEmailNotification";
    Object[] params     = { gdocObj };

    try
    {
      logger.logEntry(methodName, params);

      GridDocument gdoc = (GridDocument)gdocObj;

      Collection sentDocuments = getSentDocumentsByProcessID(gdoc);
      if(sentDocuments != null && !sentDocuments.isEmpty())
      {
        // only use the first one. all documents should be of same message.
        GridDocument sentGdoc = (GridDocument)sentDocuments.toArray()[0];
        String docType        = sentGdoc.getUdocDocType();
        boolean found         = _config.isDocTypeExists(docType);

        if (found)
        {
          String emailCode  = _config.getEmailCodeXpath(docType);
          String alertName  = _config.getAlertToRaise(docType);

          raiseAlert(sentGdoc, alertName, emailCode);
        }
      }
    }
    catch (Throwable t)
    {
      logger.logError(methodName, params, t);
      throw new AbortUserProcedureException(
                  "Error in ["+getClassName()+ ".sendEmailNotification]", t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Raise a partner received document alert.
   *
   * @param gdoc The GridDocument
   * @param alertName The alert to raise.
   * @param emailCode Email code for the custom alert recipients.
   */
  private void raiseAlert(GridDocument gdoc,
                            String alertName,
                            String emailCode)
    throws Exception
  {
    FacadeLogger logger = FacadeLogger.getLogger(
                            getClassName(),
                            Logger.CATEGORY_MAIN);
    String methodName   = "raiseAlert";
    Object[] params     = { gdoc, alertName, emailCode };

    List result = null;
    try
    {
      logger.logEntry(methodName, params);

      if (alertName != null)
      {
        result = ServiceLookupHelper.getDocAlertMgr().raiseDocAlert(
                   gdoc, emailCode, alertName, false,
                   createDataProviders(gdoc));

        logger.logMessage(methodName, params, "Result: "+result);
      }
      else
      {
        logger.logMessage(methodName, params, "Missing Alert Name in config file "+getConfigFileName()
          + " for document type " +gdoc.getUdocDocType());
      }
    }
    catch (Throwable t)
    {
      logger.logError(methodName, params, t);
      throw new AbortUserProcedureException(
                "Error in ["+getClassName()+".raiseAlert]", t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Retrieve the sent documents of a partitular process instance id.
   *
   * @param gdoc The received RN acknowledgement.
   * @return Collection of GridDocument(s) of sent documents.
   */
  private Collection getSentDocumentsByProcessID(GridDocument gdoc)
    throws Exception
  {
    ProcessInstanceActionHelper helper = new ProcessInstanceActionHelper();
    Collection sentDocKeys = helper.findAckedSendBizDocKeys(gdoc);

    return retrieveDocuments(sentDocKeys);
  }

  /**
   * Create additional data providers for raising alert.
   * @param gdoc The GridDocument to get information for creating additional
   * data providers.
   * @return The additional data providers, <b>null</b> if none is required.
   */
  private IDataProvider[] createDataProviders(GridDocument gdoc)
    throws Exception
  {
    Long rnProfileUid = gdoc.getRnProfileUid();

    RNProfile profile = ServiceLookupHelper.getRnifManager().findRNProfile(rnProfileUid);
    ProcessData processData = null;
    if (profile != null)
    {
      processData = new ProcessData(profile);
    }
    if (processData != null)
      return new IDataProvider[] {processData};
    else
      return null;
  }

  /**
   * Retrieve GridDocuments.
   *
   * @param docKeys Collection of UIDs of GridDocuments to retrieve.
   * @return Collection of GridDocument(s) retrieved.
   */
  private Collection retrieveDocuments(Collection docKeys)
    throws Exception
  {
    ArrayList result = new ArrayList();

    if (docKeys != null && !docKeys.isEmpty())
    {
      DataFilterImpl filter = new DataFilterImpl();
      filter.addDomainFilter(null, GridDocument.UID, docKeys, false);
      result.addAll(ServiceLookupHelper.getDocumentMgr().findGridDocuments(filter));
    }

    return result;
  }

}