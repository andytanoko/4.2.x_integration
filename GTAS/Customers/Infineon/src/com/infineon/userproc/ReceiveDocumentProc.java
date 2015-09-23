/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: ReceiveDocumentProc.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 06 2003    Jagadeesh/Neo Sok Lay   Created
 */

package com.infineon.userproc;

import com.infineon.userproc.helpers.Logger;
import com.infineon.userproc.helpers.ServiceLookupHelper;
import com.infineon.userproc.helpers.NotificationConfig;

import com.gridnode.gtas.server.userprocedure.exceptions.AbortUserProcedureException;
import com.gridnode.gtas.server.document.model.GridDocument;

import com.gridnode.pdip.framework.log.FacadeLogger;

import java.util.List;

/**
 * This User Procedure is written for the Infineon Project.
 * <p>
 * Example:<p>
 * When Infineon GTAS receives an Invoice, the system will send an
 * email notification, attaching the Invoice document, to the Infineon
 * Finance users.
 * <p>
 * This user procedure is used to perform customized tasks on receival of
 * documents whose document types are configured in the "receive-doc.properties" file.
 * <p>
 * Currently, there is only a sendEmailNotification() task.
 * if emailcode xpath is configured, the received document must be in Xml format.
 * <p>
 */
public class ReceiveDocumentProc
{
  private static final String CONFIG_FILE = "receive-doc.properties";

  private NotificationConfig _config = null;

  public ReceiveDocumentProc()
  {
    _config = NotificationConfig.getNotificationConfig(CONFIG_FILE);
  }

  /**
   * If the document type is configured, the corresponding configured Alert will
   * be raised and the email code xpath (optional) will be used to extract the
   * email code from the received document. The extracted email code will be used
   * to map to user role and all users of that role (with email address configured)
   * will be sent an email notification. The received document will be attached
   * in the email notification.
   *
   * @param gdocObj The GridDocument object for the received document.
   * @throws AbortUserProcedureException Application error while executing the method.
   * @throws SystemException System error while executing the method.
   */
  public void sendEmailNotification(GridDocument gdocObj) throws Exception
  {
    FacadeLogger logger = FacadeLogger.getLogger(
                            "ReceiveDocumentProc",
                            Logger.CATEGORY_MAIN);
    String methodName   = "sendEmailNotification";
    Object[] params     = { gdocObj };

    try
    {
      logger.logEntry(methodName, params);

      GridDocument gdoc = (GridDocument)gdocObj;

      // check the receive-doc config whether the received document is of
      // type that needs to send email notification.
      // if yes, the corresponding alert is raised.
      String docType = gdoc.getUdocDocType();
      if(_config.isDocTypeExists(docType))
      {
        String emailCode  = _config.getEmailCodeXpath(docType);
        String alertName  = _config.getAlertToRaise(docType);

        raiseAlert(gdoc, alertName, emailCode);
      }
    }
    catch (Throwable t)
    {
      logger.logError(methodName, params, t);
      throw new AbortUserProcedureException("Error in [ReceiveDocumentProc.sendEmailNotification]", t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }
  }

  /**
   * Raise a received document alert.
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
                            "ReceiveDocumentProc",
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
                   gdoc, emailCode, alertName, true, null);

        logger.logMessage(methodName, params, "Result: "+result);
      }
      else
      {
        logger.logMessage(methodName, params, "Missing Alert Name in config file "+CONFIG_FILE
          + " for document type " +gdoc.getUdocDocType());
      }
    }
    catch (Throwable t)
    {
      logger.logError(methodName, params, t);
      throw new AbortUserProcedureException("Error in [ReceiveDocumentProc.raiseAlert]", t);
    }
    finally
    {
      logger.logExit(methodName, params);
    }

  }

}