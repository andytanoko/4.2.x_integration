/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jan 27 2003    Neo Sok Lay         Created
 * Feb 26 2003    Neo Sok Lay         Add additonal data providers for raising
 *                                    alert.
 * Mar 10 2003    Neo Sok Lay         Allow receive confirmation alert to
 *                                    obtain the attachment using reflection.
 * Apr 29 2003    Neo Sok Lay         Raise alert by alert UID and recipient
 *                                    list.
 *                                    Expose getDefaultAttachmentMethod().
 * Nov 23 2005    Neo Sok Lay         Get alert attachment from IAttachmentProvider
 *                                    instead of Method reflection invocation.                                   
 *
 */
package com.gridnode.gtas.server.docalert.helpers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import com.gridnode.gtas.server.docalert.model.DocAlertProviderList;
import com.gridnode.gtas.server.docalert.model.GridDocumentData;
import com.gridnode.gtas.server.docalert.model.UserDocumentData;
import com.gridnode.gtas.server.document.helpers.FileHelper;
import com.gridnode.gtas.server.document.model.GridDocument;
import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerHome;
import com.gridnode.pdip.app.alert.facade.ejb.IAlertManagerObj;
import com.gridnode.pdip.app.alert.providers.IDataProvider;
import com.gridnode.pdip.app.alert.providers.RecipientListData;
import com.gridnode.pdip.framework.exceptions.ServiceLookupException;
import com.gridnode.pdip.framework.util.ServiceLocator;

/**
 * This is a helper class for raising alerts.
 *
 * @author Neo Sok Lay
 *
 * @version 2.1
 * @since 2.0 I7
 */
public class AlertHelper
{
  public static final String EMAIL_PATH   = "alert.path.email";
  public static final String MAILID_FILE  = "code2email.properties";
  public static final String ROLE_FILE    = "code2role.properties";

  //private static Method _defGetAttachmentMethod = null;
  private static IAttachmentProvider _defAttachmentProvider = null;
  
  private static Properties _code2email;
  private static Properties _code2role;

  /**
   * Raise an alert on receive document response.
   *
   * @param responseDoc The Received document
   * @param recipientXpath The Xpath for extracting the email code of the
   * custom recipients of the alert.
   * @param alertName Name of the Alert to raise.
   */
/*030310NSL: replaced by method below.
  public static void raiseReceiveResponseAlert(GridDocument responseDoc,
                                               String recipientXpath,
                                               String alertName)
  {
    ArrayList customRecipients = new ArrayList();
    List emailCode = DocExtractionHelper.extractFromXpath(recipientXpath,
                                                            responseDoc);
    Logger.debug("[AlertHelper.raiseReceiveResponseAlert] Extracted email code list = "+emailCode);
    customRecipients.addAll(getRoleEmailsFromEmailCodes(emailCode));

    raiseAlert(responseDoc, concatEmails(customRecipients), alertName, true, null);
  }
*/

  /**
   * Raise an alert on receive document response.
   *
   * @param responseDoc The Received document
   * @param recipientXpath The Xpath for extracting the email code of the
   * custom recipients of the alert.
   * @param alertName Name of the Alert to raise.
   */
  public static void raiseReceiveResponseAlert(GridDocument responseDoc,
                                               String recipientXpath,
                                               String alertName,
                                               IAttachmentProvider attachmentProvider)
                                               //Method getAttachmentMethod,
                                               //Object methodTarget)
  {
    if (!isEmptyStr(alertName))
    {
      ArrayList customRecipients = new ArrayList();
      /*030430NSL Don't resolve email addresses here, use RecipientList.
        Note that any existing message templates using EMAIL_CODE_RECIPIENTS
        from UserDocument will be obsolete and need to be changed to
        DYNAMIC_RECIPIENTS from RecipientList.
      List emailCode = DocExtractionHelper.extractFromXpath(recipientXpath,
                                                              responseDoc);

      Logger.debug("[AlertHelper.raiseReceiveResponseAlert] Extracted email code list = "+emailCode);
      customRecipients.addAll(getRoleEmailsFromEmailCodes(emailCode));
      */
      addRecipient(customRecipients, recipientXpath);

      //raiseAlert(responseDoc, concatEmails(customRecipients), alertName,
      //  getAttachmentMethod, methodTarget, null);
      raiseAlert(alertName, responseDoc, attachmentProvider, null, customRecipients);
        //getAttachmentMethod, methodTarget, null, customRecipients);
    }
  }

  private static void addRecipient(ArrayList recipientList, String recipientXpath)
  {
    if (!isEmptyStr(recipientXpath))
      recipientList.add(RecipientListData.FIELD_EMAIL_CODE_XPATH +
                        recipientXpath);
  }

  /**
   * Raise an alert for reminder of unprocessed sent document by recipient.
   *
   * @param sentDoc The Sent document.
   * @param docRecipientXpath Xpath for extracting the email code of the document
   * recipients. This will indicate the recipients of the alert.
   * @param docSenderXpath Xpath for extracting the email code of the document
   * sender or internal users. This will indicate the recipients of the alert.
   * @param alertName The name of the alert to raise.
   */
  public static void raiseReminderAlert(GridDocument sentDoc,
                                        String docRecipientXpath,
                                        String docSenderXpath,
                                        String alertName)
  {
    ArrayList customRecipients = new ArrayList();
    /*030430NSL Don't resolve email addresses here, use RecipientList.
      Note that any existing message templates using EMAIL_CODE_RECIPIENTS
      from UserDocument will be obsolete and need to be changed to
      DYNAMIC_RECIPIENTS from RecipientList.
    List recptEmailCode = DocExtractionHelper.extractFromXpath(docRecipientXpath,
                                                               sentDoc);
    List senderEmailCode = DocExtractionHelper.extractFromXpath(docSenderXpath,
                                                                sentDoc);

    customRecipients.addAll(getMailIdsFromEmailCodes(recptEmailCode));
    customRecipients.addAll(getRoleEmailsFromEmailCodes(senderEmailCode));
    */
    addRecipient(customRecipients, docRecipientXpath);
    addRecipient(customRecipients, docSenderXpath);

    //raiseAlert(sentDoc, concatEmails(customRecipients), alertName, false, null);
    raiseAlert(alertName, sentDoc, false, null, customRecipients);
  }

  /**
   * Get the email addresses base on email codes from the code2email properties.
   * @param emailCodes Collection of email codes (String).
   * @return Collection of email addresses for each email code.
   * @deprecated
   *//*
  private static Collection getMailIdsFromEmailCodes(Collection emailCodes)
  {
    ArrayList emails = new ArrayList();
    if (emailCodes != null && !emailCodes.isEmpty())
    {
      String emailAddr = null;
      String code = null;
      for (Iterator i=emailCodes.iterator(); i.hasNext(); )
      {
        // lookup for mapped email addresses
        code = (String)i.next();
        if (!isEmptyStr(code))
          emailAddr = getEmailCodeMailIdProperties().getProperty(code, null);

        if (!isEmptyStr(emailAddr))
          emails.add(emailAddr);
      }
    }
    return emails;
  }*/

  /**
   * Get the email addresses of the users of certain roles base on email code
   * from the code2role properties.
   *
   * @param emailCodes Collection of emailCodes (String)
   * @return Collection of email addresses for the role corresponding to the
   * each email code.
   * @deprecated
   *//*
  private static Collection getRoleEmailsFromEmailCodes(Collection emailCodes)
  {
    ArrayList emails = new ArrayList();
    if (emailCodes != null && !emailCodes.isEmpty())
    {
      Collection roleEmails = null;
      for (Iterator i=emailCodes.iterator(); i.hasNext(); )
      {
        roleEmails = getRoleEmailsFromEmailCode((String)i.next());
        if (roleEmails != null)
           emails.addAll(roleEmails);
      }
    }

    return emails;
  }*/

  /**
   * Get the email addresses of the users of a particular role base on email
   * code from the code2role properties.
   *
   * @param emailCode The email code to lookup.
   * @return Collection of email addresses of those users of the role corresponding
   * to the emailCode.
   * @deprecated
   *//*
  private static Collection getRoleEmailsFromEmailCode(String emailCode)
  {
    Collection emails = null;
    try
    {
      Logger.debug("[AlertHelper.getRoleEmailsFromEmailCode] GetRoleEmails for EmailCode "+emailCode);
      if (emailCode != null)
      {
        // lookup for mapped Role
        String role = getEmailCodeRoleProperties().getProperty(emailCode, null);
        Logger.debug("[AlertHelper.getRoleEmailsFromEmailCode] Mapped role = "+emailCode);
        if (!isEmptyStr(role))
        {
          // call AlertMgr to get mail ids for Role
          emails = getMgr().getMailIdsFromRole(role);
        }
      }
    }
    catch (Exception ex)
    {
      Logger.err("[AlertHelper.getRoleEmailsFromEmailCode] Error ", ex);
    }

    return emails;
  }*/

  /**
   * Raise an alert using GridDocument and User document as data providers.
   *
   * @param gdoc The GridDocument..
   * @param recipientXpath The Xpath for extracting the email code of the
   * custom recipients of the alert.
   * @param alertName Name of the Alert to raise.
   * @param attachDoc <b>true</b> to attach the User document in email, <b>false</b>
   * otherwise.
   * @return The result of raising alert e.g. success, or failure, etc for
   * each Alert action performed.
   */
//  public static List raiseDocAlert(GridDocument gdoc,
//                                   String recipientXpath,
//                                   String alertName,
//                                   boolean attachDoc)
//  {
//    return raiseDocAlert(gdoc, recipientXpath, alertName, attachDoc, null);
//  }

  /**
   * Raise an alert using GridDocument and User document as data providers.
   *
   * @param gdoc The GridDocument..
   * @param recipientXpath The Xpath for extracting the email code of the
   * custom recipients of the alert.
   * @param alertName Name of the Alert to raise.
   * @param attachDoc <b>true</b> to attach the User document in email, <b>false</b>
   * otherwise.
   * @param additonalProviders Additional data providers for the alert.
   * Can be <b>Null</b> for none.
   * @return The result of raising alert e.g. success, or failure, etc for
   * each Alert action performed.
   */
  public static List raiseDocAlert(GridDocument gdoc,
                                   String recipientXpath,
                                   String alertName,
                                   boolean attachDoc,
                                   IDataProvider[] additionalProviders)
  {
    ArrayList customRecipients = new ArrayList();
    /*030430NSL Don't resolve email addresses here, use RecipientList.
      Note that any existing message templates using EMAIL_CODE_RECIPIENTS
      from UserDocument will be obsolete and need to be changed to
      DYNAMIC_RECIPIENTS from RecipientList.
    List emailCode = DocExtractionHelper.extractFromXpath(recipientXpath,
                                                          gdoc);
    customRecipients.addAll(getRoleEmailsFromEmailCodes(emailCode));
    */
    addRecipient(customRecipients, recipientXpath);

    //return raiseAlert(gdoc, concatEmails(customRecipients),
    //                  alertName, attachDoc, additionalProviders);
    return raiseAlert(alertName, gdoc, attachDoc, additionalProviders, customRecipients);
  }

  /**
   * Raise an alert.
   *
   * @param gdoc The GridDocument for constructing the data providers.
   * @param customRecipients The customRecipients of the alert.
   * @param alertName The name of the Alert to raise.
   * @param attachDoc <b>true</b> to attach the user document to the email (if applicable),
   * <b>false</b> otherwise.
   * @return The result of raising alert e.g. success, or failure, etc for
   * each Alert action performed.
   */
//  public static List raiseAlert(GridDocument gdoc,
//                                String customRecipients,
//                                String alertName,
//                                boolean attachDoc,
//                                IDataProvider[] additionalProviders)
  public static List raiseAlert(String alertName,
                                GridDocument gdoc,
                                boolean attachDoc,
                                IDataProvider[] additionalProviders,
                                List recipientList)
  {

    return raiseAlert(
             alertName,
             gdoc,
             //attachDoc? getDefaultAttachmentMethod() : null,
             //null,
             attachDoc? getDefaultAttachmentProvider() : null,
             additionalProviders,
             recipientList);

/*
    List result = null;
    try
    {
      String udocFilename = FileHelper.getUdocFile(gdoc).getCanonicalPath();

      // construct data providers
      DocAlertProviderList providers = new DocAlertProviderList(
                                         new GridDocumentData(gdoc),
                                         new UserDocumentData(udocFilename,
                                                              customRecipients));
      if (additionalProviders != null)
      {
        for (int i=0; i<additionalProviders.length; i++)
          providers.addProvider(additionalProviders[i]);
      }

      // raise alert

      result = getMgr().triggerAlert(alertName, providers,
                                     attachDoc? udocFilename : null);

      Logger.log("[AlertHelper.raiseAlert] Result: "+result);
    }
    catch (Exception ex)
    {
      Logger.err("[AlertHelper.raiseAlert] Fail to raise alert "+alertName, ex);
    }

    return result;
*/
  }


  /**
   * Raise an alert.
   *
   * @param gdoc The GridDocument for constructing the data providers.
   * @param customRecipients The customRecipients of the alert.
   * @param alertName The name of the Alert to raise.
   * @param attachDoc <b>true</b> to attach the user document to the email (if applicable),
   * <b>false</b> otherwise.
   * @return The result of raising alert e.g. success, or failure, etc for
   * each Alert action performed.
   */
//  public static List raiseAlert(GridDocument gdoc,
//                                String customRecipients,
//                                String alertName,
//                                Method getAttachmentMethod,
//                                Object methodTarget,
//                                IDataProvider[] additionalProviders)
  public static List raiseAlert(String alertName,
                                GridDocument gdoc,
                                //Method getAttachmentMethod,
                                //Object methodTarget,
                                IAttachmentProvider attachmentProvider,
                                IDataProvider[] additionalProviders,
                                List recipientList)
  {

    List result = null;
    try
    {
      String customRecipients = "";
      String udocFilename = FileHelper.getUdocFile(gdoc).getCanonicalPath();
      RecipientListData recptListData = new RecipientListData(recipientList,
                                                              udocFilename);

      // construct data providers
      DocAlertProviderList providers = new DocAlertProviderList(
                                         new GridDocumentData(gdoc),
                                         new UserDocumentData(udocFilename,
                                                              customRecipients));
      if (additionalProviders != null)
      {
        for (int i=0; i<additionalProviders.length; i++)
          providers.addProvider(additionalProviders[i]);
      }
      providers.addProvider(recptListData);

      // raise alert
      result = getMgr().triggerAlert(alertName,
                                     providers,
                                     getAttachmentFile(attachmentProvider,                                                       
                                       //getAttachmentMethod,
                                       //methodTarget,
                                       gdoc));

      Logger.log("[AlertHelper.raiseAlert] Result: "+result);
    }
    catch (Throwable t)
    {
      Logger.warn("[AlertHelper.raiseAlert] Fail to raise alert "+alertName, t);
    }

    return result;
  }

  /**
   * Get the attachment file for the alert using reflection.
   *
   * @param attachmentProvider Provides the alert attachment file. Can be <b>null</b>
   * if no attachment is required.
   * @param gridDoc The argument to pass to the getAttachmentMethod.
   */
//* @param getAttachmentMethod The method that would accept a GridDocument
//* object and returns a String filename of the attachment file.
//* If no attachment is necessary, this should be a <b>null</b>.
//* @param methodTarget The object that the getAttachmentMethod would be
//* invoked upon. This can be <b>null</b> if getAttachmentMethod is static.
//* Applicable only if getAttachmentMethod is not <b>null<b>.
  private static String getAttachmentFile(//Method getAttachmentMethod,
                                          //Object methodTarget,
                                          IAttachmentProvider attachmentProvider,
                                          GridDocument gridDoc)
    throws Throwable
  {
    String attachment = null;
    /*
    try
    {
      if (getAttachmentMethod != null)
      {
        attachment = (String)getAttachmentMethod.invoke(
                      methodTarget,
                      new Object[] {gridDoc});
      }
    }
    catch (InvocationTargetException ex)
    {
      throw ex.getTargetException();
    }*/
    if (attachmentProvider != null)
    {
      attachment = attachmentProvider.getAlertAttachment(gridDoc);
    }

    return attachment;
  }

  public static String getUdocFilename(GridDocument gridDoc) throws Exception
  {
    return FileHelper.getUdocFile(gridDoc).getCanonicalPath();
  }

  /*
  public static final synchronized Method getDefaultAttachmentMethod()
  {
    try
    {
      if (_defGetAttachmentMethod == null)
        _defGetAttachmentMethod = AlertHelper.class.getMethod("getUdocFilename",
                                    new Class[] {GridDocument.class});
    }
    catch (Exception ex)
    {
      Logger.err("[AlertHelper.getDefaultAttachmentMethod] "
      + "Unable to obtain the default getAttachmentMethod: ", ex);
    }
    return _defGetAttachmentMethod;
  }*/
  
  public static final synchronized IAttachmentProvider getDefaultAttachmentProvider()
  {
    if (_defAttachmentProvider == null)
    {
      _defAttachmentProvider = new IAttachmentProvider()
      {
        /**
         * serial version UID
         */
        private static final long serialVersionUID = 6442501515094229781L;

        public String getAlertAttachment(GridDocument gdoc) throws Exception
        {
          return FileHelper.getUdocFile(gdoc).getCanonicalPath();
        }
      };
    }
    return _defAttachmentProvider;
  }

  public static boolean isEmptyStr(String val)
  {
    return (val == null) || (val.length() == 0);
  }

  /**
   * Concatentate the collection of email addresses into a single string.
   *
   * @param emails The Collection of email addresses (String)
   * @return The concatenated string. Each email is delimited by comma.
   */
  protected static String concatEmails(Collection emails)
  {
    StringBuffer buff = new StringBuffer();
    for (Iterator i=emails.iterator(); i.hasNext(); )
    {
      if (buff.length() != 0)
        buff.append(",");
      buff.append(i.next());
    }
    return buff.toString();
  }

  protected static IAlertManagerObj getMgr() throws ServiceLookupException
  {
    return (IAlertManagerObj)ServiceLocator.instance(
             ServiceLocator.CLIENT_CONTEXT).getObj(
             IAlertManagerHome.class.getName(),
             IAlertManagerHome.class,
             new Object[0]);
  }
  /*
  private static synchronized Properties getEmailCodeMailIdProperties()
  {
    if (_code2email != null)
      return _code2email;

    _code2email = new Properties();
    try
    {
      _code2email.load(new FileInputStream(FileUtil.getFile(EMAIL_PATH, MAILID_FILE)));
    }
    catch (Exception ex)
    {
      Logger.err("[AlertHelper.getEmailCodeMailIdProperties] Unable to load "+
        MAILID_FILE);
    }

    return _code2email;
  }

  private static synchronized Properties getEmailCodeRoleProperties()
  {
    if (_code2role != null)
      return _code2role;

    _code2role = new Properties();
    try
    {
      _code2role.load(new FileInputStream(FileUtil.getFile(EMAIL_PATH, ROLE_FILE)));
    }
    catch (Exception ex)
    {
      Logger.err("[AlertHelper.getEmailCodeRoleProperties] Unable to load "+
        ROLE_FILE);
    }

    return _code2role;
  }*/

  public static List raiseAlert(Long alertUID,
                                GridDocument gdoc,
                                IDataProvider[] providers,
                                boolean attachDoc,
                                List recipientList)
  {
    return raiseAlert(alertUID,
                      gdoc,
                      providers,
                      //attachDoc? getDefaultAttachmentMethod() : null,
                      //null,
                      attachDoc? getDefaultAttachmentProvider() : null,
                      recipientList);
  }

  public static List raiseAlert(Long alertUID,
                                GridDocument gdoc,
                                IDataProvider[] providers,
                                IAttachmentProvider attachmentProvider,
                                //Method getAttachmentMethod,
                                //Object methodTarget,
                                List recipientList)
  {
    List result = null;
    String customRecipients = "";
    try
    {
      String udocFilename = FileHelper.getUdocFile(gdoc).getCanonicalPath();
      RecipientListData recptListData = new RecipientListData(recipientList,
                                                              udocFilename);

      DocAlertProviderList providerList = new DocAlertProviderList(
                                              new GridDocumentData(gdoc),
                                              new UserDocumentData(udocFilename,
                                                                   customRecipients));
      if (providers != null)
      {
        for (int i=0; i<providers.length; i++)
          providerList.addProvider(providers[i]);
      }
      providerList.addProvider(recptListData);

      // raise alert
      result = getMgr().triggerAlert(alertUID,
                                     providerList,
                                     getAttachmentFile(attachmentProvider,
                                       //getAttachmentMethod,
                                       //methodTarget,
                                       gdoc));

      Logger.log("[AlertHelper.raiseAlert] Result: "+result);
    }
    catch (Throwable t)
    {
      Logger.warn("[AlertHelper.raiseAlert] Fail to raise alert "+alertUID, t);
    }
    return result;
  }


}