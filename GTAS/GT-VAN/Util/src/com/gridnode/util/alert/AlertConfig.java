/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2005 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertConfig.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Nov 14, 2006        i00107             Created
 */

package com.gridnode.util.alert;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * @author i00107
 *
 */
public class AlertConfig extends Properties
{
  /**
   * Serial Version UID
   */
  private static final long serialVersionUID = -7723616879436734743L;
  public static final String DELIVERY_MODE = "delivery.mode";
  public static final String RECIPIENTS = "recipients";
  public static final String SUBJECT = "subject";
  public static final String MESSAGE = "message";
  
  public AlertConfig()
  {
  }

  /**
   * @param defaults
   */
  public AlertConfig(Properties defaults)
  {
    super(defaults);
  }

  public int getDeliveryMode(int defVal)
  {
    String prop = getProperty(DELIVERY_MODE, null);
    if (prop != null)
    {
      try
      {
        return Integer.parseInt(prop);
      }
      catch (NumberFormatException ex)
      {
        System.err.println("Invald delivery mode format: "+prop);
        return defVal;
      }
    }
    else
    {
      return defVal;
    }
  }
  
  public String getSubject()
  {
    return getProperty(SUBJECT, null);
  }
  
  public String getMessage()
  {
    return getProperty(MESSAGE, null);
  }
  
  public List<String> getRecipients()
  {
    String recipientsStr = getProperty(RECIPIENTS, null);
    if (recipientsStr != null && recipientsStr.trim().length()>0)
    {
      List<String> recipientList = new ArrayList<String>();
      StringTokenizer stoken = new StringTokenizer(recipientsStr, ",");
      while (stoken.hasMoreTokens())
      {
        recipientList.add(stoken.nextToken());
      }
      return recipientList;
    }
    else
    {
      return null;
    }
  }

}
