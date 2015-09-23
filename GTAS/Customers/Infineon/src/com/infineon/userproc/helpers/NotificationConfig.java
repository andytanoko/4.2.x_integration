/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: NotificationConfig.java
 *
 ****************************************************************************
 * Date           Author                  Changes
 ****************************************************************************
 * Feb 04 2002    Jagadeesh              Created
 * Feb 07 2003    Neo Sok Lay             Config file obtain from data directory
 *                                        instead of conf directory because
 *                                        this is not a system configuration
 *                                        file, it belongs to the user.
 */

package com.infineon.userproc.helpers;

import com.gridnode.pdip.framework.file.helpers.FileUtil;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class NotificationConfig
{
  public static final String CONFIG_PATH = "alert.path.userproc";

  public static final String KEY_DOC_TYPES = "doctypes";
  public static final String KEY_ALERT_EXT = ".alert";
  public static final String KEY_EMAILCODE_EXT = ".emailcode";

  private static final String DELIMITER = ",";

  private Properties _props = null;
  private ArrayList _docTypeList = null;

  private static NotificationConfig _self = null;
  private static Object lock = new Object();

  private NotificationConfig(String configName)
  {
    readConfig(configName);

    _docTypeList = new ArrayList();
    parseAndSetDocType(_props.getProperty(KEY_DOC_TYPES));
  }

  private void parseAndSetDocType(String docTypes)
  {
    StringTokenizer docTypeTokens = new StringTokenizer(docTypes,DELIMITER);
    while(docTypeTokens.hasMoreElements())
    {
      _docTypeList.add(docTypeTokens.nextElement());
    }
  }

  private synchronized void readConfig(String configName)
  {
    _props = new Properties();
    try
    {
      _props.load(new FileInputStream(FileUtil.getFile(CONFIG_PATH, configName)));
    }
    catch (Exception ex)
    {
      Logger.err("[NotificationConfig.readConfig] Unable to load "+
        configName);
    }
  }

  public static NotificationConfig getNotificationConfig(String configName)
  {
     if(_self == null)
     {
        synchronized(NotificationConfig.class)
        {
          if(_self == null)
            _self = new NotificationConfig(configName);
        }
     }
     return _self;
  }

  public String getAlertToRaise(String docType)
  {
    String alertToRaise = null;
    if(isDocTypeExists(docType))
    {
      String alertKey = docType+KEY_ALERT_EXT;
      alertToRaise = _props.getProperty(alertKey);
    }
    return alertToRaise;
  }

  public String getEmailCodeXpath(String docType)
  {
    String emailCodeXpath = null;
    if(isDocTypeExists(docType))
    {
      String emailKey = docType+KEY_EMAILCODE_EXT;
      emailCodeXpath = _props.getProperty(emailKey);
    }
    return emailCodeXpath;
  }

  public boolean isDocTypeExists(String docType)
  {
    return (_docTypeList.contains(docType));
  }

//  public static void main(String args[])
//  {
//    UserProcAlertConfigRegistry registry = UserProcAlertConfigRegistry.getUserProcAlertRegistry();
//    System.out.println("DocType Exists ="+ registry.isDocTypeExists("docType1"));
//    System.out.println("EmailCode Xpath ="+ registry.getEmailCodeXpath("docType1"));
//    System.out.println("Alert To Raise ="+ registry.getAlertToRaise("docType1"));
//
//  }

}