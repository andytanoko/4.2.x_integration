/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: CommURLValidator.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Oct 17 2003    Neo Sok Lay         Created
 */
package com.gridnode.gtas.server.channel.helpers;

import com.gridnode.pdip.base.transport.comminfo.CommInfoFactory;
import com.gridnode.pdip.base.transport.comminfo.ICommInfo;
import com.gridnode.pdip.base.transport.exceptions.GNTptInvalidProtocolException;
import com.gridnode.pdip.framework.exceptions.ApplicationException;
import com.gridnode.pdip.framework.rpf.event.EventException;

import java.net.MalformedURLException;

/**
 * Communication Info URL validator.
 * 
 * @author Neo Sok Lay
 * @since GT 2.3
 */
public class CommURLValidator
{
  /**
   * Validates URL that is used for a specified protocol type.
   * 
   * @param protocolType The Protocol Type
   * @param url The URL to be validated.
   * @throws MalformedURLException Invalid URL for the specified protocol type.
   * @throws GNTptInvalidProtocolException Unsupported protocol type.
   */
  public static void validateURL(String protocolType, String url)
    throws Exception
  {
    ICommInfo commInfo = null;
    try
    {
      commInfo = CommInfoFactory.getTransportCommInfo(protocolType);
      commInfo.parseURL(url);
    }
    catch (MalformedURLException e)
    {
      throw new EventException(
        e.getMessage() + " -- Please provide URL in the form: "+commInfo.getURLPattern());
    }
    catch (GNTptInvalidProtocolException e)
    {
      throw new ApplicationException(
        e.getMessage() + " -- Please ensure the Protocol is supported: "+protocolType, e);
    }
  }
}
