/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EmailActionHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 20 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.alert.engine;

import com.gridnode.pdip.app.alert.helpers.AlertLogger;
import com.gridnode.pdip.app.alert.mail.AlertMailService;
import com.gridnode.pdip.app.alert.model.MessageTemplate;
import com.gridnode.pdip.app.alert.providers.IProviderList;


/**
 * An AlertAction handler for Email type of AlertActions.
 * 
 * @author Neo Sok Lay
 * @since GT 2.1
 */
public class EmailActionHandler extends AbstractAlertActionHandler
{
  private static final String EMAIL_FAILURE = "Failed to send the e-mail";
  private static final String EMAIL_SUCCESS = "Successfully sent the mail";

  /**
   * Executes the Email AlertAction.
   * 
   * @see com.gridnode.pdip.app.alert.helpers.IAlertActionHandler#execute(IProviderList, String)
   */
  public String execute(IProviderList providers, String attachment)
  {
    MessageTemplate msg = getMessageTemplate();

    return (
      sendMail(
        msg.getToAddr(),
        msg.getFromAddr(),
        msg.getCcAddr(),
        msg.getSubject(),
        msg.getMessage(),
        attachment,
        providers))
      ? EMAIL_SUCCESS
      : EMAIL_FAILURE;
  }

  /**
   * Send the message as a mail
   *
   * @param toAddr  E-Mail address to whom the mail is being sent
   * @param fromAddr  E-mail from whom the mail is sent
   * @param cc    CC for the mail
   * @param subject     The subject for the mail
   * @param message     The message of the mail
   * @param filePath    the absolute path of the file
   * @param providerList The list of data providers.
   *
   * @return <b>true</b> if the mail is sent successfully.
   */
  protected boolean sendMail(
    String toAddr,
    String fromAddr,
    String cc,
    String subject,
    String message,
    String filePath,
    IProviderList providerList)
  {
    try
    {
      Repository repos = new Repository();
      String[] formatted =
        repos.format(
          new String[] { fromAddr, toAddr, cc, subject, message },
          providerList);

      AlertMailService mailService = AlertMailService.getInstance();
      return mailService.sendMail(
        formatted[0], //fromAddr
        formatted[1], //toAddr
        formatted[2], //cc
        null,
        formatted[3], //subject
        formatted[4], //message
        filePath,true);
    }
    catch (Throwable t)
    {
      AlertLogger.warnLog("EmailActionHandler", "sendMail", "Error", t);
      return false;
    }
  }
}
