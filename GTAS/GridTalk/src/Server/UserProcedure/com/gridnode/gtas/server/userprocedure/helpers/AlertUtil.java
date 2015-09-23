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
 * May 12 2002    Neo Sok Lay         Created
 * May 27 2003    Jagadeesh           Added : raiseUserDefinedAlert To raise userdefined
 *                                    Alert.
 * Jun 26 2003    Neo Sok Lay         GNDB00014171: Duplicate document
 *                                    before raising USER_DEFINED alert.
 * May 27 2004    Neo Sok Lay         GNDB00024897: Cater for case when Gdoc is null.
 */

package com.gridnode.gtas.server.userprocedure.helpers;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.notify.Notifier;
import com.gridnode.gtas.server.notify.AlertRequestNotification;
import com.gridnode.gtas.server.userprocedure.exceptions.AbortUserProcedureException;
import com.gridnode.gtas.server.userprocedure.model.UserProcData;
import com.gridnode.gtas.server.document.helpers.AlertDelegate;
import com.gridnode.gtas.server.document.model.GridDocument;

import com.gridnode.pdip.app.alert.providers.ProcessingErrorData;

import com.gridnode.pdip.base.userprocedure.model.UserProcedure;
import com.gridnode.pdip.framework.jms.JMSRetrySender;

import java.util.ArrayList;
import java.util.List;

/**
 * This is an utility class that handles alert related tasks for Rnif module.
 *
 * @author Neo Sok Lay
 *
 * @version GT 2.3.4
 * @since 2.1
 */
public class AlertUtil
{
  public static String ERR_USER_PROC_ABORTED = "1";
  public static String ERR_USER_PROC_EXE_FAILED = "2";

  public static void alertUserProcFailed(
    GridDocument gdoc,
    UserProcedure userProc,
    Throwable t)
  {
    raiseFailureAlert(gdoc, userProc, t);
  }

  private static List createProviders(UserProcedure userProcedure, Throwable t)
  {
    List providers = new ArrayList();
    String reason =
      (t instanceof AbortUserProcedureException)
        ? ERR_USER_PROC_ABORTED
        : ERR_USER_PROC_EXE_FAILED;

    providers.add(
      new ProcessingErrorData(
        AlertRequestNotification.USER_PROCEDURE_FAILURE,
        reason,
        t));
    providers.add(new UserProcData(userProcedure));
    return providers;
  }

  private static List createProviders(UserProcedure userProcedure)
  {
    List providers = new ArrayList();
    providers.add(new UserProcData(userProcedure));
    return providers;
  }

  private static void raiseFailureAlert(
    GridDocument gdoc,
    UserProcedure userProc,
    Throwable t)
  {
    try
    {
      AlertRequestNotification notification;
      if (gdoc != null)
      {
        gdoc = AlertDelegate.duplicate(gdoc);
        //this will go to DocAlert handler
        notification =
          new AlertRequestNotification(
            AlertRequestNotification.USER_PROCEDURE_FAILURE,
            gdoc,
            createProviders(userProc, t));
      }
      else
      {
        //this will go to Alert handler, using Level0 trigger
        notification =
          new AlertRequestNotification(
            AlertRequestNotification.USER_PROCEDURE_FAILURE,
            createProviders(userProc, t));
      }
      
      if(! JMSRetrySender.isSendViaDefMode()) //TWX 23 Nov 2007 To enable the jms sending with retry
      {
        Logger.debug("[AlertUtil.raiseFailureAlert] broadcast via JMSRetrySender");
        JMSRetrySender.broadcast(notification);
      }
      else
      {
        Notifier.getInstance().broadcast(notification);
      }
    }
    catch (Exception ex)
    {
      Logger.error(ILogErrorCodes.GT_USER_PROC_ALERT_UTILITY,
                   "[AlertUtil.raiseFailureAlert] Error: "+ex.getMessage(), ex);
    }
  }

  /**
   * Raise a UserDefined Alert (Alert Type == USER_DEFINED).
   * @param alertId - the alert to raise.
   * @param gdoc - GridDocument as Object.
   * @param userProc - UserProcedure as Object.
   */
  public static void raiseUserDefinedAlert(
    Long alertId,
    GridDocument gdoc,
    UserProcedure userProc)
  {
    try
    {
      AlertRequestNotification notification;
      if (gdoc != null)
      {
        gdoc = AlertDelegate.duplicate(gdoc);
        //this will go to DocAlert handler
        notification =
          new AlertRequestNotification(
            alertId,
            gdoc,
            createProviders(userProc));
      }
      else
      {
        //this will go to Alert handler
        notification =
          new AlertRequestNotification(alertId, createProviders(userProc));
      }

      Notifier.getInstance().broadcast(notification);
    }
    catch (Exception ex)
    {
      Logger.error(ILogErrorCodes.GT_USER_PROC_ALERT_UTILITY,
                   "[AlertUtil.raiseUserDefinedAlert] Error: "+ex.getMessage(), ex);
    }

  }

}