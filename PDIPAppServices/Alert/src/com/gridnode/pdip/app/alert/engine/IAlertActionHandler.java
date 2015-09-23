/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: IAlertActionHandler.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 20 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.alert.engine;

import com.gridnode.pdip.app.alert.model.MessageTemplate;
import com.gridnode.pdip.app.alert.providers.IProviderList;

/**
 * Interface for an handler for executing an AlertAction.
 * 
 * @author Neo Sok Lay
 * @since GT 2.1
 */
public interface IAlertActionHandler
{
  /**
   * Set the MessageTemplate to use when the AlertAction executes.
   * 
   * @param msg The message template.
   */
  void setMessageTemplate(MessageTemplate msg);

  /**
   * Get the MessageTemplate to use when the AlertAction executes.
   * 
   * @return The message template set.
   */
  MessageTemplate getMessageTemplate();

  /**
   * Executes an AlertAction using the message template set.
   * 
   * @param providers List of data providers to format the message.
   * @param attachment Relative path of the attachment, <b>null</b> if
   * no attachment required.
   */  
  String execute(IProviderList providers,
                 String attachment);
}
