/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertMailService.java
 *
 ****************************************************************************
 * Date      				Author              Changes
 ****************************************************************************
 * Jul 7, 2004 			Mahesh             	Created
 * Oct 27 2005      Neo Sok Lay         Use FileUtil to access email folders and files
 */
package com.gridnode.pdip.app.alert.mail;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.gridnode.pdip.app.alert.exceptions.ILogErrorCodes;
import com.gridnode.pdip.app.alert.helpers.AlertLogger;
import com.gridnode.pdip.app.alert.helpers.IEmailConfiguration;
import com.gridnode.pdip.app.alert.helpers.ServiceLookupHelper;
import com.gridnode.pdip.app.alert.model.EmailConfig;
import com.gridnode.pdip.base.time.entities.model.iCalAlarm;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;
import com.gridnode.pdip.framework.db.filter.IDataFilter;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.exceptions.FileAccessException;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

public class AlertMailService
{
  private static AlertMailService _alertMailService = new AlertMailService();

  protected EmailConfig _emailConfig;
  protected DateFormat _dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
  protected int _seqNumber = 0;
  
  private static final String CLASSNAME =  "AlertMailService";
  
  private AlertMailService()
  {
    
  }
  
  public static AlertMailService getInstance()
  {
    return _alertMailService;
  }

  public EmailConfig getEmailConfig() throws Throwable
  {
    if (_emailConfig == null)
    {
      synchronized (this)
      {
        if (_emailConfig == null)
          _emailConfig = (EmailConfig) (new EmailConfig()).deserialize(getEmailConfigFile());
      }
    }
    return _emailConfig;
  }

  /**
   * Saves the EmailConfig to the config file
   * @param emailConfig
   * @throws Throwable
   */
  public synchronized void saveEmailConfig(EmailConfig emailConfig) throws Throwable
  {
    try
    {
      _emailConfig = emailConfig;
      emailConfig.serialize(getEmailConfigFile());

      //remove the previous configured alarm if exists
      cancelEmailRetryAlarm();

      //dont schedule the alarm if MaxRetries <= 0
      if (emailConfig.getMaxRetries().intValue() > 0)
        addEmailRetryAlarm(emailConfig.getRetryInterval(), emailConfig.getMaxRetries());
    }
    catch (Throwable th)
    {
      _emailConfig = null;
      throw th;
    }
  }

  /**
   * Sends the mail. if processFailedMail is true then failed messages are resent
   * @param sender
   * @param recipient
   * @param ccRecipient
   * @param bccRecipient
   * @param subject
   * @param body
   * @param attachments
   * @param processFailedMail
   * @return
   */
  public boolean sendMail(String sender,String recipient,String ccRecipient,String bccRecipient,String subject,String body,String attachments,boolean processFailedMail)
  {
    Message msg = null;
    try
    {
      Session session = getMailSession(true);
      msg = buildMessage(session, sender, recipient, ccRecipient, bccRecipient, subject, body, attachments);
      Transport.send(msg);
      return true;
    }
    catch (Throwable th)
    {
      AlertLogger.warnLog(CLASSNAME,"sendMail","Error in sendMail",th);
      if (msg!=null && processFailedMail)
        addFailedMail(msg,isMessageException(th));
    }
    return false;
  }

  /**
   * Resends the mails from EMAIL_RETRY_FOLDER 
   *
   */
  public void retrySending()
  {
    try
    {
      EmailConfig emailConfig = getEmailConfig();
      int maxRetries = emailConfig.getMaxRetries().intValue();
      boolean saveFailedMails = emailConfig.getSaveFailedEmails();
      List messageFileList = getMessagesInFolder(IEmailConfiguration.EMAIL_RETRY_PATH);
      
      if (maxRetries > 0 && messageFileList.size()>0)
      {
        Session session = getMailSession(true);
        for (Iterator i = messageFileList.iterator(); i.hasNext();)
        {
          resendMail(session, (File)i.next(), maxRetries, saveFailedMails);
        }
      }
    }
    catch (Throwable th)
    {
      AlertLogger.warnLog(CLASSNAME,"retrySending","Error in retrySending",th);
    }
  }

  /**
   * Saves the failed mails to EMAIL_RETRY_PATH or EMAIL_FAILED_PATH folder 
   * @param msg
   * @param isMsgException
   */
  protected void addFailedMail(Message msg,boolean isMsgException)
  {
    try
    {
      EmailConfig emailConfig = getEmailConfig();
      String messageId=getNewMessageId();
      String fileName = messageId+".msg";
      msg.setHeader(IEmailConfiguration.ALERT_MESSAGEID_KEY, messageId);
      msg.setHeader(IEmailConfiguration.ALERT_RETRYCOUNT_KEY, "0");
      if (emailConfig.getMaxRetries().intValue() > 0 && !isMsgException)
      {
      	//NSL20051027 Get the directory path from FileUtil
      	File retryFolder = FileUtil.getFile(IEmailConfiguration.EMAIL_RETRY_PATH, "");
      	File msgFile = new File(retryFolder, fileName);
        //File msgFile = new File(FileUtil.getDomain()+File.separator+FileUtil.getPath(IEmailConfiguration.EMAIL_RETRY_PATH)+File.separator+fileName);
        saveMessage(msg,msgFile);
      }
      else if (emailConfig.getSaveFailedEmails())
      {
      	//NSL20051027 Get the directory path from FileUtil
      	File failedFolder = FileUtil.getFile(IEmailConfiguration.EMAIL_FAILED_PATH, "");
      	File msgFile = new File(failedFolder, fileName);
        //File msgFile = new File(FileUtil.getDomain()+File.separator+FileUtil.getPath(IEmailConfiguration.EMAIL_FAILED_PATH)+File.separator+fileName);
        saveMessage(msg,msgFile);
      }
      else
      {
        //dont save just log 
      }
    }
    catch (Throwable th)
    {
      AlertLogger.warnLog(CLASSNAME,"addFailedMail","Error in addFailedMail",th);
    }
  }

  /**
   * This methos resends the failed mails
   * @param session
   * @param messageFile
   * @param maxRetries
   * @param saveFailedMails
   * @return
   * @throws ApplicationException
   */
  protected boolean resendMail(Session session, File messageFile, final int maxRetries, final boolean saveFailedMails)
  {
    Message msg = null;
    int retryCount = -1;
    try
    {
      msg = readMessage(session, messageFile);
      retryCount = 1 + Integer.parseInt(msg.getHeader(IEmailConfiguration.ALERT_RETRYCOUNT_KEY)[0].trim());
      msg.setHeader(IEmailConfiguration.ALERT_RETRYCOUNT_KEY, "" + retryCount);
    }
    catch (Throwable th)
    {
      //if the exception occurs while reading/parsing nothing can 
      //be done so save it to failed folder
      AlertLogger.warnLog(CLASSNAME,"resendMail","Error in resendMail, messageFile="+messageFile,th);
      try
      {
        if (saveFailedMails)
          FileUtil.move(IEmailConfiguration.EMAIL_RETRY_PATH, IEmailConfiguration.EMAIL_FAILED_PATH, messageFile.getName());
      }
      catch(FileAccessException ex)
      {
        AlertLogger.warnLog(CLASSNAME,"resendMail","Error in moving messageFile="+messageFile,th);
      }
      return false;
    }

    try
    {
      Transport.send(msg);
      FileUtil.delete(IEmailConfiguration.EMAIL_RETRY_PATH, messageFile.getName());
      return true;
    }
    catch (FileAccessException ex)
    {
      AlertLogger.warnLog(CLASSNAME,"resendMail","Error while deleting message "+messageFile,ex);
    }
    catch (Throwable th)
    {
      AlertLogger.warnLog(CLASSNAME,"resendMail","Error in resendMail",th);
      if(isMessageException(th) || retryCount>=maxRetries)  
      {
        try
        {
          if(saveFailedMails)
            FileUtil.move(IEmailConfiguration.EMAIL_RETRY_PATH, IEmailConfiguration.EMAIL_FAILED_PATH, messageFile.getName());
          else 
            FileUtil.delete(IEmailConfiguration.EMAIL_RETRY_PATH,messageFile.getName());
        }
        catch(Throwable th1)
        {
          AlertLogger.warnLog(CLASSNAME,"resendMail","Error while moving/delete message "+messageFile,th1);
        }
      } 
      else if (retryCount < maxRetries)
      {
        try
        {
          saveMessage(msg, messageFile);
        }
        catch(Throwable th1)
        {
          AlertLogger.warnLog(CLASSNAME,"resendMail","Error while saving message "+messageFile,th1);
        }
      }
    }
    return false;
  }

  protected void saveMessage(Message message, File messageFile) throws MessagingException, IOException
  {
    OutputStream outStream = null;
    try
    {
      message.saveChanges();
      outStream = new FileOutputStream(messageFile);
      message.writeTo(outStream);
    }
    finally
    {
      try
      {
        if (outStream != null)
          outStream.close();
      }
      catch (Throwable th)
      {
        
      }
    }
  }

  protected Message readMessage(Session session, File messageFile) throws MessagingException, IOException
  {
    InputStream inStream = null;
    try
    {
      inStream = new FileInputStream(messageFile);
      return new MimeMessage(session, inStream);
    }
    finally
    {
      try
      {
        if (inStream != null)
          inStream.close();
      }
      catch (Throwable th)
      {
      }
    }
  }

  protected Message buildMessage(
    Session session,
    String sender,
    String recipient,
    String ccRecipient,
    String bccRecipient,
    String subject,
    String body,
    String attachments)
    throws MessagingException, IOException
  {
    MimeMessage msg = new MimeMessage(session);
    {
      InternetAddress[] theAddresses = InternetAddress.parse(sender);
      msg.addFrom(theAddresses);
    }

    {
      InternetAddress[] theAddresses = InternetAddress.parse(recipient);
      msg.addRecipients(Message.RecipientType.TO, theAddresses);
    }

    if (null != ccRecipient)
    {
      InternetAddress[] theAddresses = InternetAddress.parse(ccRecipient);
      msg.addRecipients(Message.RecipientType.CC, theAddresses);
    }

    if (null != bccRecipient)
    {
      InternetAddress[] theAddresses = InternetAddress.parse(bccRecipient);
      msg.addRecipients(Message.RecipientType.BCC, theAddresses);
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
      while (-1 != (posIndex = attachments.indexOf("///", startIndex)))
      {
        MimeBodyPart mbp = new MimeBodyPart();
        FileDataSource fds = new FileDataSource(attachments.substring(startIndex, posIndex));
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

    return msg;

  }

  protected List getMessagesInFolder(String pathKey) throws Throwable
  {
    //File file = new File(FileUtil.getDomain()+File.separator+FileUtil.getPath(pathKey));
  	File folder = FileUtil.getFile(pathKey, ""); //NSL20051027 Get folder from FileUtil
    File files[]=folder.listFiles();
    if(files!=null && files.length>0)
      return Arrays.asList(files);
    return Collections.EMPTY_LIST;
  }

  protected synchronized String getNewMessageId()
  {
    if (_seqNumber == Integer.MAX_VALUE)
      _seqNumber = 0;
    String messageId  = _dateFormat.format(new Date()) + (++_seqNumber);
    return messageId;
  }

  protected Session getMailSession(boolean newSession) throws Throwable
  {
    EmailConfig emailConfig = getEmailConfig();
    String smtpServerHost = emailConfig.getSmtpServerHost();
    String smtpUserName = emailConfig.getAuthUser();
    String smtpPassword = emailConfig.getAuthPassword();
    
    if(smtpServerHost==null || smtpServerHost.trim().length()==0)
    {
      AlertLogger.debugLog(CLASSNAME,"getMailSession","SmtpServerHost is not configured");
      throw new NullPointerException("SmtpServerHost is null");
    }
    
    Authenticator auth = null;
    
    Properties props = System.getProperties();
    props.put("mail.smtp.host", smtpServerHost);
    if(emailConfig.getSmtpServerPort()!=null)
      props.put("mail.smtp.port", emailConfig.getSmtpServerPort().toString());
      
    if (smtpUserName != null && smtpUserName.trim().length() > 0)
    {
      props.setProperty("mail.smtp.auth", "true");
      auth = new MailAuthenticator(smtpUserName, smtpPassword);
    }
    if (newSession)
      return Session.getInstance(props, auth);
    else
      return Session.getDefaultInstance(props, auth);
  }

  private static String getEmailConfigFile() throws Throwable
  {
    return FileUtil.getFile(IEmailConfiguration.EMAIL_PATH, IEmailConfiguration.EMAIL_CONFIG_FILE).getAbsolutePath();
  }

  //Alarm helper methods  
  private void cancelEmailRetryAlarm() throws Exception
  {
    IDataFilter filter = new DataFilterImpl();
    filter.addSingleFilter(null, iCalAlarm.CATEGORY, filter.getEqualOperator(), EmailConfig.ENTITY_NAME, false);
    filter.addSingleFilter(filter.getAndConnector(), iCalAlarm.SENDER_KEY, filter.getEqualOperator(), EmailConfig.ENTITY_NAME, false);
    filter.addSingleFilter(filter.getAndConnector(),iCalAlarm.RECEIVER_KEY,filter.getEqualOperator(),EmailConfig.ENTITY_NAME,false);
    ServiceLookupHelper.getICalManager().cancelAlarmByFilter(filter);
  }

  private void addEmailRetryAlarm(Long retryInterval, Integer retryCount) throws Exception
  {
    iCalAlarm alarm = new iCalAlarm();
    alarm.setCategory(EmailConfig.ENTITY_NAME);
    alarm.setReceiverKey(EmailConfig.ENTITY_NAME);
    alarm.setSenderKey(EmailConfig.ENTITY_NAME);
    alarm.setDelayPeriod(retryInterval);
    alarm.setStartDt(new Date(System.currentTimeMillis() + (retryInterval.longValue() * 1000)));
    ServiceLookupHelper.getICalManager().addAlarm(alarm);
  }
  
  /*
  private boolean isTransportError(Throwable th)
  {
    String transportError[] = {"Could not connect to SMTP host","Unknown SMTP host","AuthenticationFailedException","553"};
    String errMsg = th.getMessage();
    for(int i = 0 ;i < transportError.length;i++)
    {
      if(errMsg.indexOf(transportError[i])>-1)
        return true;
    }
    return false;     
  }
  */
  
  private boolean isMessageException(Throwable th)
  {
    String permanentError[] = {"550"};
    String errMsg = th.getMessage();
    for(int i = 0 ;i < permanentError.length;i++)
    {
      if(errMsg.indexOf(permanentError[i])>-1)
        return true;
    }
    return false;     
  }
  
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