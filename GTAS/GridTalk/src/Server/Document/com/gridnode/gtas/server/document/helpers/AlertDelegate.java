/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertDelegate.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * May 06 2003    Neo Sok Lay         Created
 * Jun 27 2003    Neo Sok Lay         GNDB00014171: For DocExportedAlert,
 *                                    make copy the user document. Set CleanUp
 *                                    option for handler to delete the user doc
 *                                    after alert is raised.
 * OCT 02 2007    Tam Wei Xiang       Added to trigger Document Resend Exhausted Alert                                   
 */
package com.gridnode.gtas.server.document.helpers;

import com.gridnode.gtas.exceptions.ILogErrorCodes;
import com.gridnode.gtas.server.notify.Notifier;
import com.gridnode.gtas.server.notify.AlertRequestNotification;
import com.gridnode.gtas.server.notify.IAlertTypes;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.gtas.server.backend.model.Port;
import com.gridnode.gtas.server.backend.model.PortData;
import com.gridnode.gtas.server.backend.model.RfcData;

import com.gridnode.pdip.app.alert.providers.IDataProvider;
import com.gridnode.pdip.app.alert.providers.ProcessingErrorData;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;

/**
 * This Delegate handles all alert related tasks in Document module.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.1
 */
public class AlertDelegate
  implements IAlertTypes
{

  /**
   * Raise an DOCUMENT_EXPORTED alert.
   *
   * @param gdoc The GridDocument to raise alert for.
   * @param port The Port that the document is exported via.
   */
  public static void raiseDocExportedAlert(GridDocument gdoc, Port port)
  {
    IDataProvider[] providers;

    PortData portData = new PortData(port);
    if (port.getRfc() != null)
    {
      RfcData rfcData = new RfcData(port.getRfc());
      providers = new IDataProvider[] { portData, rfcData };
    }
    else
    {
      providers = new IDataProvider[] { portData };
    }

    gdoc = duplicate(gdoc);
    raiseAlert(gdoc, DOCUMENT_EXPORTED, providers, true);
  }

  /**
   * Duplicate the specified GridDocument. This also makes a copy of the user
   * document in the temp directory.
   *
   * @param gdoc The GridDocument to duplicate.
   * @return The duplicated GridDocument. This contains the filename (temp)
   * of the copied user document.
   */
  public static GridDocument duplicate(GridDocument gdoc)
  {
    gdoc = (GridDocument)gdoc.clone();
    try
    {
      File oriUdocFile = FileHelper.getUdocFile(gdoc);

      String newFn = FileHelper.copyToTemp(oriUdocFile.getCanonicalPath());

      gdoc.setTempUdocFilename(newFn);
    }
    catch (Exception ex)
    {
      Logger.warn("[AlertDelegate.duplicate] File IO Error ", ex);
    }
    return gdoc;
  }

  /**
   * Raise an DOCUMENT_RECEIVED_BY_PARTNER alert.
   *
   * @param gdoc The GridDocument to raise alert for.
   */
  public static void raiseDocReceivedByPartnerAlert(GridDocument gdoc)
  {
    raiseAlert(gdoc, DOCUMENT_RECEIVED_BY_PARTNER);
  }

  /**
   * Raise a PARTNER_FUNCTION_FAILURE alert.
   *
   * @param gdoc The GridDocument to raise alert for.
   * @param type The error type
   * @param reason The reason for the error
   * @param exception Exception that occurred, if any.
   * @see IDocProcessingErrorConstants
   */
  public static void raisePartnerFunctionFailureAlert(GridDocument gdoc,
                                                      String type,
                                                      String reason,
                                                      Throwable exception)
  {
    ProcessingErrorData procError = new ProcessingErrorData(type, reason, exception);

    raiseAlert(gdoc, PARTNER_FUNCTION_FAILURE, new IDataProvider[]{procError});
  }
  
  /**
   * Raise a DOCUMENT_RESEND_EXHAUSTED alert.
   * @param gdoc The GridDocument to raise alert for.
   */
  public static void raiseDocumentResendExhaustedAlert(GridDocument gdoc)
  {
    raiseAlert(gdoc, DOCUMENT_RESEND_EXHAUSTED);
  }
  
  /**
   * Raise an alert of a specified type for a document.
   *
   * @param gdoc The GridDocuemtn to raise alert for.
   * @param alertType The type of alert to raise.
   */
  public static void raiseAlert(GridDocument gdoc, String alertType)
  {
    raiseAlert(gdoc, alertType, null);
  }

  public static void raiseAlert(GridDocument gdoc, String alertType, IDataProvider[] providers)
  {
    raiseAlert(gdoc, alertType, providers, false);
  }

  public static void raiseAlert(GridDocument gdoc, String alertType, IDataProvider[] providers, boolean cleanUp)
  {
    try
    {
      AlertRequestNotification notification = null;

      // raise normal alert request if gdoc is null
      notification = gdoc == null ?
                     new AlertRequestNotification(alertType, getCollection(providers)) :
                     new AlertRequestNotification(alertType, gdoc, getCollection(providers));

      notification.setCleanUpDocument(cleanUp);
      Notifier.getInstance().broadcast(notification);
    }
    catch (Throwable t)
    {
      Logger.error(ILogErrorCodes.GT_DOCUMENT_ALERT_DELEGATE,
                   "[AlertDelegate.raiseAlert] Error broadcasting document alert request. Error: "+t.getMessage(), t);
    }
  }

  private static Collection getCollection(Object[] array)
  {
    Collection coll = null;
    if (array != null)
      coll = Arrays.asList(array);
    return coll;
  }

}
