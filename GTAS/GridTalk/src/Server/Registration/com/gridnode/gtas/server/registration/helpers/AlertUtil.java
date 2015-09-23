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
 * May 08 2003    Neo Sok Lay         Created
 * Mar 28 2006    Neo Sok Lay         Add broadcasting of license state
 */
package com.gridnode.gtas.server.registration.helpers;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.registration.model.LicenseData;
import com.gridnode.gtas.server.registration.model.RegistrationInfo;
import com.gridnode.gtas.server.notify.LicenseStateNotification;
import com.gridnode.gtas.server.notify.Notifier;
import com.gridnode.gtas.server.notify.AlertRequestNotification;
import com.gridnode.gtas.server.gridnode.model.GridNode;
import com.gridnode.gtas.server.gridnode.model.GridNodeData;

import com.gridnode.pdip.app.license.model.License;
import com.gridnode.pdip.app.alert.providers.IDataProvider;
import com.gridnode.pdip.framework.jms.JMSRetrySender;

import java.util.Arrays;

/**
 * Utility class for alert related tasks.
 *
 * @author Neo Sok Lay
 *
 * @version 4.0
 * @since 2.1
 */
public class AlertUtil
{

  /**
   * Alert that the license has expired.
   *
   * @param license The license record.
   */
  public static void alertLicenseExpired(License license)
  {
    raiseAlert(license, AlertRequestNotification.LICENSE_EXPIRED);
  }

  /**
   * Alert that the license will be expiring soon.
   *
   * @param license The license record.
   */
  public static void alertLicenseExpiring(License license)
  {
    raiseAlert(license, AlertRequestNotification.LICENSE_EXPIRE_SOON);
  }

  /**
   * Raise an alert regarding a license record. The License and GridNode data
   * providers will be provided for raising alert.
   *
   * @param license The license record.
   * @param alertType The type of alert to raise.
   */
  private static void raiseAlert(License license, String alertType)
  {
    try
    {
      GridNode gridnode = ServiceLookupHelper.getGridNodeManager().findMyGridNode();

      IDataProvider[] providers = new IDataProvider[] {
                                      new LicenseData(license),
                                      new GridNodeData(gridnode)};

      AlertRequestNotification notification = new AlertRequestNotification(
                                                  alertType,
                                                  Arrays.asList(providers));
      if(! JMSRetrySender.isSendViaDefMode()) //TWX 23 Nov 2007 To enable the jms sending with retry
      {
        Logger.debug("[AlertUtil.raiseAlert] raiseAlert via JMSRetrySender");
        JMSRetrySender.broadcast(notification);
      }
      else
      {
        Notifier.getInstance().broadcast(notification);
      }
    }
    catch (Exception ex)
    {
      Logger.error(ILogErrorCodes.GT_REGISTRATION_ALERT_UTILITY,
                   "[AlertUtil.raiseAlert] Error raising alert type: "+alertType+" Error: "+ex.getMessage(),
        ex);
    }
  }

  /**
   * Broadcast the license state to interested parties
   * @param regInfo The current registration info
   */
  public static void notifyLicenseState(RegistrationInfo regInfo)
  {
  	try
  	{
  		LicenseStateNotification notification = new LicenseStateNotification(
                                                   regInfo.getProductKeyF1()+regInfo.getProductKeyF2()+
                                                   regInfo.getProductKeyF3()+regInfo.getProductKeyF4(),
                                                   regInfo.getGridnodeID(),
                                                   regInfo.getCategory(),
                                                   regInfo.getBizConnections(),
                                                   regInfo.getLicenseState(),
                                                   regInfo.getLicenseStartDate(),
                                                   regInfo.getLicenseEndDate());
      if(! JMSRetrySender.isSendViaDefMode()) //TWX 23 Nov 2007 To enable the jms sending with retry
      {
        Logger.debug("[AlertUtil.notifyLicenseState] notifyLicenseState via JMSRetrySender");
        JMSRetrySender.broadcast(notification);
      }
      else
  		{
        Notifier.getInstance().broadcast(notification);
      }
  	}
  	catch (Exception ex)
  	{
      Logger.error(ILogErrorCodes.GT_REGISTRATION_ALERT_UTILITY,
                   "[AlertUtil.notifyLicenseState] Error broadcasting license state: "+ex.getMessage(), ex);
  	}
  }
}
