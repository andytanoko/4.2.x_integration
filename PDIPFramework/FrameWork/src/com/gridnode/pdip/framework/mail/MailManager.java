/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: MailManager.java
 *
 * ****************************************************************************
 * Date           Author           Changes
 * ****************************************************************************
 * 06 Dec 2002    Srinath	   Creation
 * 31 Oct 2003    Mahesh     Added smtp authentication
 * Feb 07 2007		Alain Ah Ming			Use new error codes
 */

package com.gridnode.pdip.framework.mail;

import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.gridnode.pdip.framework.config.Configuration;
import com.gridnode.pdip.framework.config.ConfigurationManager;
import com.gridnode.pdip.framework.exceptions.ILogErrorCodes;
import com.gridnode.pdip.framework.log.Log;

/**
 * 
 * @author Srinath
 * @since
 * @version GT 4.0 VAN
 */
public class MailManager implements IMailManager
{

  // Sender, Recipient, CCRecipient, and BccRecipient are comma-separated
  // lists of addresses. Body can span multiple CR/LF-separated lines.
  // Attachments is a ///-separated list of file names.
  public boolean send(String sender, String recipient, String ccRecipient,
        String bccRecipient, String subject, String body, String attachments)
  {
    ConfigurationManager configManager = ConfigurationManager.getInstance();
    Configuration config = configManager.getConfig(MAIL_CONFIG_NAME);

    String smtpServer =  new String(config.getString(SMTP_SERVER));
    String smtpUsername = config.getString(SMTP_USERNAME);
    String smtpPassword = config.getString(SMTP_PASSWORD);

    // Create some properties and get the default Session;
    Properties props = System.getProperties();
    props.put("mail.smtp.host", smtpServer);

    Authenticator auth = null;

    if(smtpUsername!=null && smtpUsername.trim().length()>0)
    { 
      //set the Authenticator only if smtpUsername is configured
      props.setProperty("mail.smtp.auth", "true");
      auth = new MailAuthenticator(smtpUsername,smtpPassword);
    }
    
    try
    {
      Session session = Session.getDefaultInstance(props, auth);
      // Create a message.
      MimeMessage msg = new MimeMessage(session);
      {
        InternetAddress[] theAddresses = InternetAddress.parse(sender);
        msg.addFrom(theAddresses);
      }

      {
        InternetAddress[] theAddresses = InternetAddress.parse(recipient);
        msg.addRecipients(Message.RecipientType.TO,theAddresses);
      }

      if (null != ccRecipient)
      {
        InternetAddress[] theAddresses = InternetAddress.parse(ccRecipient);
        msg.addRecipients(Message.RecipientType.CC,theAddresses);
      }

      if (null != bccRecipient)
      {
        InternetAddress[] theAddresses = InternetAddress.parse(bccRecipient);
        msg.addRecipients(Message.RecipientType.BCC,theAddresses);
      }

      msg.setSubject(subject);
      Multipart mp = new MimeMultipart();
      {
        MimeBodyPart mbp = new MimeBodyPart();
        mbp.setText(body);
        mp.addBodyPart(mbp);
      }

      if (null != attachments)
      {
        int startIndex = 0, posIndex = 0;
        while (-1 != (posIndex = attachments.indexOf("///",startIndex)))
        {
          MimeBodyPart mbp = new MimeBodyPart();
          FileDataSource fds = new FileDataSource(attachments.substring(startIndex,posIndex));
          mbp.setDataHandler(new DataHandler(fds));
          mbp.setFileName(fds.getName());
          mp.addBodyPart(mbp);
          posIndex += 3;
          startIndex = posIndex;
        }
        if (startIndex < attachments.length())
        {
          MimeBodyPart mbp = new MimeBodyPart();
          FileDataSource fds = new FileDataSource(attachments.substring(startIndex));
          mbp.setDataHandler(new DataHandler(fds));
          mbp.setFileName(fds.getName());
          mp.addBodyPart(mbp);
        }
      }

      msg.setContent(mp);
      msg.setSentDate(new Date());
      Transport.send(msg);

      Log.log("MAIL", "[MailManager.send] Successfully sent email on smtp server "+smtpServer);

      return true;
    }
    catch (MessagingException ex)
    {
      Log.error(ILogErrorCodes.EMAIL_GENERIC, 
                "MAIL", 
                "[MailManager.send] Failed to send email. Error: "+ex.getMessage(), 
                ex);

      return false;
    }
  }
}

class MailAuthenticator extends Authenticator
{
  String _username;
  String _password;

  public MailAuthenticator(String username, String password)
  {
    _username=username;
    _password=password;
  }

  public PasswordAuthentication getPasswordAuthentication()
  {
    return new PasswordAuthentication(_username, _password);
  }

}