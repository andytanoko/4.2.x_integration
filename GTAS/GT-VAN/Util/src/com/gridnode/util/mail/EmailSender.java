/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: EmailSender.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 7, 2006    i00107              Created
 * Jan 22, 2007   Regina Zeng         Add sendWithAttachments() and 
 *                                    createMessageWithAttachments()
 * Mar 05, 2007		Alain Ah Ming       Added error code	      
 * Apr 10, 2007   Regina Zeng         Modified sendWithAttachments() to support 
 *                                    displaying of messages
 */

package com.gridnode.util.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.*;

import com.gridnode.util.StringUtil;
import com.gridnode.util.config.ConfigurationStore;
import com.gridnode.util.exceptions.ILogErrorCodes;
import com.gridnode.util.log.Logger;
import com.gridnode.util.log.LoggerManager;

/**
 * @author i00107
 * This class handles the sending of emails
 */
public class EmailSender
{
  //property keys for the SMTP server settings
  public static final String MAIL_SMTP_HOST = "mail.smtp.host";
  public static final String MAIL_SMTP_PORT = "mail.smtp.port";
  public static final String MAIL_SMTP_AUTH = "mail.smtp.auth";
  public static final String MAIL_SMTP_USER = "mail.smtp.user";
  public static final String MAIL_SMTP_AUTH_PWD = "mail.smtp.auth.pwd";
  public static final String MAIL_FROM = "mail.from";
  
  //categories for configuration
  /**
   * Category for the default SMTP server settings
   */
  public static final String DEFAULT_SMTP_SERVER = "smtp.server.default";
  /**
   * Category for the Mail recipients settings
   */
  public static final String MAIL_RECIPIENTS = "mail.recipients";
  
  public static final String RECIPIENTS_DELIM = ";";
  
  private Logger _logger;
  private Properties _props;
  private MailAuthenticator _authenticator;
  
  public EmailSender()
  {
    _logger = LoggerManager.getInstance().getLogger(LoggerManager.TYPE_UTIL, "EmailSender");
  }
  
  public void setSendProperties(Properties props)
  {
    _props = props;
    setAuth();
  }
  
  public void useDefaultSendProperties()
  {
    _props = ConfigurationStore.getInstance().getProperties(DEFAULT_SMTP_SERVER);
    setAuth();
  }
  
  private void setAuth()
  {
    boolean auth = Boolean.valueOf(_props.getProperty(MAIL_SMTP_AUTH, "false"));
    if (auth)
    {
      String user = _props.getProperty(MAIL_SMTP_USER, null);
      String pass = _props.getProperty(MAIL_SMTP_AUTH_PWD, null);
      if (StringUtil.isBlank(user) || StringUtil.isBlank(pass))
      {
        _logger.logError(ILogErrorCodes.EMAIL_SENDER_SET_AUTH, "setAuth", null, "Mail authentication properties not set properly. Either user or password is not specified.", null);
      }
      else
      {
        _authenticator = new MailAuthenticator(user, pass);
      }
    }
    if (_authenticator == null)
    {
      _props.setProperty(MAIL_SMTP_AUTH, "false");
      _props.remove(MAIL_SMTP_USER);
      _props.remove(MAIL_SMTP_AUTH_PWD);
    }
  }
  
  public void send(List<String> recipientGroups, String subject, String message)
  {
    String mtdName = "send";
    Object[] params = {recipientGroups, subject, message};
    
    Address[] recipients = getRecipientAddresses(recipientGroups);
    if (recipients.length == 0)
    {      
      _logger.logError(ILogErrorCodes.EMAIL_SENDER_SEND, mtdName, params, "No recipient addresses found!", null);
      return;
    }
    
    Session mailSession = Session.getInstance(_props, _authenticator);
    try
    {
      Message mailMsg = createMessage(mailSession, recipients, subject, message);
      Transport.send(mailMsg);
    }
    catch (MessagingException ex)
    {
      _logger.logError(ILogErrorCodes.EMAIL_SENDER_SEND, mtdName, params, "Fail to send email", ex);
    }
  }
  
  //Regina Jan 22 2007: Added to support sending of attachment
  public void sendWithAttachments(List<String> recipients, String subject, String message, File[] files)
  {
    if(files!=null)
    {
      String mtdName = "sendWithAttachments";
      Object[] params = {recipients, subject, message, files};
      
      Address[] recipient = getAddresses(recipients);
      if (recipient.length == 0)
      {
        _logger.logError(ILogErrorCodes.EMAIL_SENDER_ATTACHEMENTS_SEND, mtdName, params, "No recipient addresses found!", null);
        return;
      }

      Session mailSession = Session.getInstance(_props, _authenticator);
      try
      {
        Message mailMsg = createMessageWithAttachments(mailSession, recipient, subject, message, files);
        Transport.send(mailMsg);
        _logger.logMessage("sendWithAttachments", params, "EMAIL SENT!");
      }
      catch (MessagingException ex)
      {
        _logger.logError(ILogErrorCodes.EMAIL_SENDER_ATTACHEMENTS_SEND, mtdName, params, "Fail to send email with attachments", ex);
      }
    }
    else
    {
      this.send(recipients, subject, message);
    }
  }
  
  //Regina Jan 22 2007: Added
  private Message createMessageWithAttachments(Session mailSession, Address[] recipients, String subject, String message, File[] files)
    throws MessagingException
  {
    MimeMessage msg = new MimeMessage(mailSession);
    Multipart mp = new MimeMultipart();    
    BodyPart body = new MimeBodyPart();
    body.setText(message);
    mp.addBodyPart(body);
    for(int i=0; i<files.length; i++)
    {
      MimeBodyPart mbp = new MimeBodyPart();
      DataSource ds = new FileDataSource(files[i]);
      mbp.setDataHandler(new DataHandler(ds));
      mbp.setFileName(ds.getName());
      mp.addBodyPart(mbp);
    }
    msg.addRecipients(RecipientType.TO, recipients);
    msg.setSubject(subject);          
    msg.setSentDate(new Date());
    msg.setContent(mp);    
    return msg;
  }
  
  private Message createMessage(Session mailSession, Address[] recipients, String subject, String message)
    throws MessagingException
  {
    MimeMessage msg = new MimeMessage(mailSession);
    
    msg.addRecipients(RecipientType.TO, recipients);
    msg.setSubject(subject);
    msg.setText(message);
    msg.setSentDate(new Date());
    return msg;
  }
  
  private Address[] getRecipientAddresses(List<String> recipientGroups)
  {
    List<Address> addr = new ArrayList<Address>();
    for (String group : recipientGroups)
    {
      addr.addAll(getRecipientAddresses(group));
    }
    return addr.toArray(new InternetAddress[addr.size()]);
  }
  
  //Regina Jan 22 2007: Added
  private Address[] getAddresses(List<String> recipients)
  {
    List<Address> addr = new ArrayList<Address>();
    for(int i=0; i<recipients.size(); i++)
    {
      try
      {
        addr.add(new InternetAddress(recipients.get(i)));
      }
      catch(AddressException ex)
      {
        _logger.logError(ILogErrorCodes.EMAIL_SENDER_ADDRESSES, "getAddresses", null, "Invalid email address: "+recipients.get(i), ex);
      }      
    }
    return addr.toArray(new InternetAddress[addr.size()]);
  }
  
  private List<Address> getRecipientAddresses(String group)
  {
    List<String> strList = ConfigurationStore.getInstance().getListProperty(MAIL_RECIPIENTS, group, RECIPIENTS_DELIM);
    List<Address> addrList = new ArrayList<Address>();
    for (String addrStr : strList)
    {
      try
      {
        addrList.add(new InternetAddress(addrStr));
      }
      catch (AddressException ex)
      {
        _logger.logError(ILogErrorCodes.EMAIL_SENDER_RECIPIENT_ADDRESSES, "getRecipientAddresses", null, "Invalid email address: "+addrStr, ex);
      }
    }
    return addrList;
  }
  
  class MailAuthenticator extends Authenticator
  {
    String _username;
    String _password;

    public MailAuthenticator(String username, String password)
    {
      _username = username;
      _password = password;
    }

    public PasswordAuthentication getPasswordAuthentication()
    {
      return new PasswordAuthentication(_username, _password);
    }

  }
}
