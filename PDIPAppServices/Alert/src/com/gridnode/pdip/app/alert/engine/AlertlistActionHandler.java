/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertlistActionHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 20 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.alert.engine;

import com.gridnode.pdip.app.alert.helpers.AlertListEntityHandler;
import com.gridnode.pdip.app.alert.helpers.AlertLogger;
import com.gridnode.pdip.app.alert.model.AlertList;
import com.gridnode.pdip.app.alert.model.MessageTemplate;
import com.gridnode.pdip.app.alert.providers.IProviderList;

/**
 * An AlertAction handler for AlertList AlertAction.
 * 
 * @author Neo Sok Lay
 * @since GT 2.1
 */
public class AlertlistActionHandler extends AbstractAlertActionHandler
{
  private final static String ALERTLIST_FAILURE =
    "Failed to send to the Alert List";
  private final static String ALERTLIST_SUCCESS =
    "Successfully sent to the Alert List";

  /**
   * Executes the AlertList AlertAction.
   * 
   * @see com.gridnode.pdip.app.alert.helpers.IAlertActionHandler#execute(IProviderList, String)
   */
  public String execute(IProviderList providers, String attachment)
  {
    MessageTemplate msg = getMessageTemplate();

    return (
      createAlertList(
        msg.getFromAddr(),
        msg.getToAddr(),
        msg.getSubject(),
        msg.getMessage(),
        providers))
      ? ALERTLIST_SUCCESS
      : ALERTLIST_FAILURE;
  }

  /**
   * Create an AlertList for the message.
   *
   * @param from    Message Sender
   * @param to      Message Receiver
   * @param title   Title/Subject of the message
   * @param message Message
   * @param providerList Provider List containing the data providers.
   *
   * @return <b>true</b> if the AlertList is created successfully, <b>false</b>
   * otherwise.
   *
   * @exception thrown when an error occurs.
   */
  public boolean createAlertList(
    String from,
    String to,
    String title,
    String message,
    IProviderList providerList)
  {
    try
    {
      Repository repos = new Repository();

      String[] formatted =
        repos.format(new String[] { from, to, title, message }, providerList);

      /**@todo there is a implementation error!!!
       * The format() method would already convert the userids or roleids
       * to email addresses, which is not what we want here.
       * What we want is the UIDs of the users.
       */
      Long fromUid = Long.valueOf(formatted[0]);

      /**@todo this might be more than one users, so by right should be
       * a collection of Uids.
       */
      Long toUid = Long.valueOf(formatted[1]);

      AlertListEntityHandler alertList = AlertListEntityHandler.getInstance();
      AlertList inserted =
        alertList.createAlertList(fromUid, toUid, formatted[2], formatted[3]);
      alertList.create(inserted);
      return true;
    }
    catch (Throwable t)
    {
      AlertLogger.warnLog(
        "AlertlistActionHandler",
        "createAlertList",
        "Error",
        t);
      return false;
    }
  }
}
