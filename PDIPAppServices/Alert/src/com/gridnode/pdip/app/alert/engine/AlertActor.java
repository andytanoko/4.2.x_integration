/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertActor.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 20 2003    Neo Sok Lay         Created
 * Oct 17 2005    Neo Sok Lay         For JDK1.5 compliance: MessageFormat.format()
 *                                    must pass in Object[] instead of String[].
 * Feb 28 2006    Neo Sok Lay         Use generics for ArrayList.                                   
 */
package com.gridnode.pdip.app.alert.engine;

import com.gridnode.pdip.app.alert.helpers.AlertActionEntityHandler;
import com.gridnode.pdip.app.alert.helpers.ITempDirConfig;
import com.gridnode.pdip.app.alert.model.Alert;
import com.gridnode.pdip.app.alert.model.AlertAction;
import com.gridnode.pdip.app.alert.providers.IProviderList;
import com.gridnode.pdip.framework.file.helpers.FileUtil;

import java.io.File;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

/**
 * This class acts on an Alert that needs to be triggered.
 * 
 * @author Neo Sok Lay
 * @since GT 2.1
 */
public class AlertActor
{
  private final String _TRIGGER_FAILURE =
    "Trigger Attribute has not been set properly";
  private String _RESULT_PATTERN = "{0} for message {1}";
  private static final AlertActor _self = new AlertActor();

  private AlertActor()
  {
  }

  /**
   * Get an instance of the AlertActor.
   * 
   * @return an instance for AlertActor.
   */
  public static AlertActor getInstance()
  {
    return _self;
  }

  /**
   * Trigger the Alert with an InputStream for attachment.
   *
   * @param alert alert The Alert to trigger
   * @param providerList Provider List containing the Data Providers.
   * @param fileAttachmentInputStream The input stream of the file attachment
   *
   * @return The success/Failure for each Alert Actions triggered will be returned.
   *
   * @exception Throwable thrown when an error occurs.
   */
  public List triggerAlert(
    Alert alert,
    IProviderList providerList,
    InputStream fileAttachmentInputStream)
    throws Throwable
  {
    List result = null;
    File file = saveFile(fileAttachmentInputStream);
    String filePath = file.getAbsolutePath();
    result = triggerAlert(alert, providerList, filePath);
    return result;
  }

  /**
   * Trigger the Alert
   *
   * @param alert The Alert to trigger.
   * @param providerList The Provider List containing the Data Providers.
   * @param filePath Location of the file.(relative path to alert log location)
   *
   * @return The success/Failure for each alert actions triggered will be returned.
   *
   * @exception Throwable thrown when an error occurs.
   */
  public List triggerAlert(
    Alert alert,
    IProviderList providerList,
    String filePath)
    throws Throwable
  {
    ArrayList<String> resultList = new ArrayList<String>();

    if (isValidTriggerAttribute(alert, providerList))
    {
      Collection alertActionUids =
        AlertActionEntityHandler.getInstance().getAlertActionsByAlertUid(
          (Long) alert.getKey());

      for (Iterator i = alertActionUids.iterator(); i.hasNext();)
      {
        String result =
          triggerAlertAction((AlertAction) i.next(), providerList, filePath);
        resultList.add(result);
      }
    }
    else
      resultList.add(_TRIGGER_FAILURE);

    return resultList;
  }

  /**
   * Saves as a file when the input for attachment comes as an InputStream.
   *
   * @param input Inputstream for attachment
   *
   * @return File The saved File.
   *
   * @exception Exception thrown when an error occurs.
   */
  protected File saveFile(InputStream input) throws Exception
  {
    String attachFN =
      FileUtil.create(ITempDirConfig.ATTACHMENT_DIR, "attach.tmp", input);
    return FileUtil.getFile(ITempDirConfig.ATTACHMENT_DIR, attachFN);
  }

  /**
   * To check whether the trigger attributes are same or not.
   *
   * @param alert The alert
   * @param providerList List of data providers.
   *
   * @return <b>true</b> if the Alert can be triggered, <b>false</b>
   * otherwise.
   *
   * @exception Throwable thrown when an error occurs.
   */
  protected boolean isValidTriggerAttribute(
    Alert alert,
    IProviderList providerList)
    throws Throwable
  {
    Repository repos = new Repository();
    String trigger = alert.getTrigger();

    if (trigger == null || trigger.equals(""))
    {
      return true;
    }
    else
    {
      StringTokenizer st = new StringTokenizer(trigger, ",");
      int count = st.countTokens();
      String[] triggerAttrib = new String[count];
      if (count == 0)
      {
        triggerAttrib[0] = repos.format(trigger, providerList);
      }
      else
      {
        for (int m = 0; m < count; m++)
        {
          triggerAttrib[m] = repos.format(st.nextToken(), providerList);
        }
      }
      StringTokenizer st1;
      for (int j = 0; j < count; j++)
      {
        String trigger1 = triggerAttrib[j];
        String triggerKey = "";
        String triggerValue = "";
        char c = 'E';

        st1 = new StringTokenizer(trigger1, "-");
        int count1 = st1.countTokens();
        if (count1 > 1)
        {
          while (st1.hasMoreTokens())
          {
            triggerKey = st1.nextToken();
            c = st1.nextToken().charAt(0);
            triggerValue = st1.nextToken();
          }
        }

        int intKey = 0;
        int intVal = 0;

        switch (c)
        {
          case 'E' :
            if (!triggerKey.equals(triggerValue))
              return false;
            break;
          case 'N' :
            if (triggerKey.equals(triggerValue))
              return false;
            break;
          case 'L' :
            intKey = Integer.valueOf(triggerKey).intValue();
            intVal = Integer.valueOf(triggerValue).intValue();
            if (intKey >= intVal)
              return false;
            break;
          case 'G' :
            intKey = Integer.valueOf(triggerKey).intValue();
            intVal = Integer.valueOf(triggerValue).intValue();
            if (intKey <= intVal)
              return false;
            break;
        }
      }
    }
    return true;
  }

  /**
   * Trigger an AlertAction.
   * 
   * @param alertAction The AlertAction to trigger.
   * @param providerList List of data providers.
   * @param attachment Relative filepath to attachment.
   */
  protected String triggerAlertAction(
    AlertAction alertAction,
    IProviderList providerList,
    String attachment)
    throws Throwable
  {
    IAlertActionHandler handler =
      AlertActionHandlerFactory.getInstance().getHandlerFor(alertAction);

    String result = handler.execute(providerList, attachment);

    return MessageFormat.format(
      _RESULT_PATTERN,
      new Object[] { result, handler.getMessageTemplate().getName()});
  }

}
