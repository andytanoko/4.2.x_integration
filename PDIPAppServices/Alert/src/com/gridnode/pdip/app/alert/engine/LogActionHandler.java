/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: LogActionHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 20 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.alert.engine;

import com.gridnode.pdip.app.alert.helpers.AlertLogger;
import com.gridnode.pdip.app.alert.model.MessageTemplate;
import com.gridnode.pdip.app.alert.providers.IProviderList;

/**
 * An AlertAction handler for Log AlertAction.
 * 
 * @author Neo Sok Lay
 * @since GT 2.1
 */
public class LogActionHandler extends AbstractAlertActionHandler
{
  private final static String LOG_FAILURE =
    "Falied to enter details into the log file";
  private final static String LOG_SUCCESS =
    "Successfully entered the details into the log file";

  /**
   * Executes the Log AlertAction.
   * 
   * @see com.gridnode.pdip.app.alert.helpers.IAlertActionHandler#execute(IProviderList, String)
   */
  public String execute(IProviderList providers, String attachment)
  {
    MessageTemplate msg = getMessageTemplate();

    boolean isAppend = false;
    if (msg.getAppend() != null)
      isAppend = msg.getAppend().booleanValue();

    return writeToLog(msg.getLocation(), msg.getMessage(), isAppend, providers)
      ? LOG_SUCCESS
      : LOG_FAILURE;
  }

  /**
   * Write the message to a file.
   *
   * @param logPath  The log file path relative to the alert log location.
   * @param message The message to be written in the file
   * @param isAppend  Whether the message needs to be appended to the file or not.(True means the message will be appended)
   * @param providerList The list of data providers.
   *
   * @return <b>true</b> if the log is written successfully, <b>false</b> otherwise.
   *
   */
  protected boolean writeToLog(
    String logPath,
    String message,
    boolean isAppend,
    IProviderList providerList)
  {
    try
    {
      Repository repos = new Repository();
      String[] formatted =
        repos.format(new String[] { message, logPath }, providerList);

      return WriteLog.getInstance().writeToLog(
        formatted[1], //logPath
        formatted[0], //message
        isAppend);
    }
    catch (Throwable t)
    {
      AlertLogger.warnLog("LogActionHandler", "writeToLog", "Error", t);
      return false;
    }
  }

}
