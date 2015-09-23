/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AbstractAlertActionHandler
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 20 2003    Neo Sok Lay         Created
 */
package com.gridnode.pdip.app.alert.engine;

import com.gridnode.pdip.app.alert.model.MessageTemplate;


/**
 * 
 * 
 * @author Neo Sok Lay
 * @since GT 2.1
 */
public abstract class AbstractAlertActionHandler implements IAlertActionHandler
{
  private MessageTemplate _template;
  
  /**
   * @see com.gridnode.pdip.app.alert.helpers.IAlertActionHandler#setMessageTemplate(MessageTemplate)
   */
  public void setMessageTemplate(MessageTemplate msg)
  {
    _template = msg;
  }
  /**
   * @see com.gridnode.pdip.app.alert.helpers.IAlertActionHandler#getMessageTemplate()
   */
  public MessageTemplate getMessageTemplate()
  {
    return _template;
  }
}
