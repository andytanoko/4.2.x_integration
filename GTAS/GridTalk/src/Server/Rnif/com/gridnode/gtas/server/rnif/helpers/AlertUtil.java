/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertUtil.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 09 2002    Neo Sok Lay         Created
 * Jan 26 2006    Tam Wei Xiang       Added method alertFailureNotificationReceived()
 *                                    to support the sending of process instance failure alert 
 *                                    when we receive 0A1.  
 * Feb 08 2006    Neo Sok Lay         To add ERR_USER_CANCELLED_PIP for user initiated cancellation of process.                                 
 */
package com.gridnode.gtas.server.rnif.helpers;

import java.util.Collection;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.notify.Notifier;
import com.gridnode.gtas.server.notify.AlertRequestNotification;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.rnif.model.ProcessData;
import com.gridnode.gtas.server.rnif.model.RNProfile;

import com.gridnode.pdip.app.alert.providers.ProcessingErrorData;
import com.gridnode.pdip.framework.jms.JMSRetrySender;

import java.util.ArrayList;

/**
 * This is an utility class that handles alert related tasks for Rnif module.
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.1
 */
public class AlertUtil
{
  public static String ERR_MAX_RETRIES_REACHED      = "1";
  public static String ERR_TIME_TO_PERFORM_EXPIRED  = "2";
  public static String ERR_VALIDATE_DOC_FAILED      = "3";
  public static String ERR_FAILURE_PIP_FAILED       = "4";
  public static String ERR_EXCEPTION_RECEIVED       = "5";
  public static String ERR_0A1_RECEIVED             = "6";
  public static String ERR_FAILURE_N_PIP_INITIATED  = "7";
  public static String ERR_USER_CANCELLED_PIP       = "8";

  public static void alertMaxRetriesReached(GridDocument gdoc)
  {
    raiseFailureAlert(gdoc, ERR_MAX_RETRIES_REACHED, "");
  }

  public static void alertTimeToPerformExpired(GridDocument gdoc)
  {
    raiseFailureAlert(gdoc, ERR_TIME_TO_PERFORM_EXPIRED, "");
  }

  public static void alertValidateDocFailed(GridDocument gdoc, String reason)
  {
    raiseFailureAlert(gdoc, ERR_VALIDATE_DOC_FAILED, reason);
  }

  public static void alertDocReceived(GridDocument gdocNof)
  {
    try
    {
      AlertRequestNotification notification = new AlertRequestNotification(
                                                  AlertRequestNotification.DOCUMENT_RECEIVED,
                                                  gdocNof,
                                                  getDataProviders(gdocNof, null, null)
                                                  );
      if(! JMSRetrySender.isSendViaDefMode())
      {
        Logger.debug("[AlertUtil.alertDocReceived] send via JMSRetrySender.");
        JMSRetrySender.broadcast(notification);
      }
      else
      {
        Notifier.getInstance().broadcast(notification);
      }
    }
    catch (Exception ex)
    {
      Logger.error(ILogErrorCodes.GT_RNIF_ALERT_UTILITY,
                   "[AlertUtil.alertFailurePipReceived] Error: "+ex.getMessage(), ex);
    }

  }

  public static void alertFailurePipFail(GridDocument gdocNof)
  {
    raiseFailureAlert(gdocNof, ERR_FAILURE_PIP_FAILED, "");
  }

  public static void alertExceptionReceived(GridDocument gdoc)
  {
    raiseFailureAlert(gdoc, ERR_EXCEPTION_RECEIVED, "");
  }
  
  //TWX: 26012006
  public static void alertFailureNotificationReceived(GridDocument gdoc, String reason)
  {
  	raiseFailureAlert(gdoc, ERR_0A1_RECEIVED, reason);
  }
  
  public static void alertFailureNotificationInitiated(GridDocument gdoc, String reason)
  {
  	raiseFailureAlert(gdoc, ERR_FAILURE_N_PIP_INITIATED, reason);
  }

  //NSL20060208
  public static void alertUserCancelledPip(GridDocument gdoc, String reason)
  {
    raiseFailureAlert(gdoc, ERR_USER_CANCELLED_PIP, reason);
  }

  private static void raiseFailureAlert(GridDocument gdoc, String errorType, String errorReason)
  {
    try
    {
      AlertRequestNotification notification = new AlertRequestNotification(
                                                  AlertRequestNotification.PROCESS_INSTANCE_FAILURE,
                                                  gdoc,
                                                  getDataProviders(gdoc, errorType, errorReason)
                                                  );
      if(! JMSRetrySender.isSendViaDefMode())//TWX 23 Nov 2007 To enable the jms sending with retry
      {
        Logger.debug("[AlertUtil.raiseFailureAlert] raise alert via JMSRetrySender");
        JMSRetrySender.broadcast(notification);
      }
      else
      {
        Notifier.getInstance().broadcast(notification);
      }
    }
    catch (Exception ex)
    {
      Logger.error(ILogErrorCodes.GT_RNIF_ALERT_UTILITY,
                   "[AlertUtil.raiseAlert] Error: "+ex.getMessage(), ex);
    }
  }

  private static Collection getDataProviders(GridDocument gdoc,
                                      String errorType,
                                      String errorReason)
  {
    ArrayList providers = new ArrayList();
    try
    {
      ProfileUtil profileUtil = new ProfileUtil();
      RNProfile rnProfile = profileUtil.getProfileMaytExist(gdoc);
      if (rnProfile != null)
        providers.add(new ProcessData(rnProfile));

      if (errorType != null)
        providers.add(new ProcessingErrorData(errorType, errorReason, null));
    }
    catch (Exception ex)
    {
      Logger.warn("[AlertUtil.getDataProviders] Error ", ex);
    }

    return providers;
  }
}