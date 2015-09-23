/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: DefaultAlertActionHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 20 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.alert.engine;

import com.gridnode.pdip.app.alert.providers.IProviderList;

/**
 * An AlertActionHandler to handle any AlertAction not specifically handled.
 * 
 * @author Neo Sok Lay
 * @since GT 2.1
 */
public class DefaultAlertActionHandler extends AbstractAlertActionHandler
{
  public final String UNKNOWN_MSG_TYPE   = "Unknown Message Type set in message template";

  /**
   * Does nothing.
   * 
   * @return Unknown message type as the result.
   * @see com.gridnode.pdip.app.alert.helpers.IAlertActionHandler#execute(IProviderList, String)
   */
  public String execute(IProviderList providers, String attachment)
  {
    return UNKNOWN_MSG_TYPE;
  }
}
