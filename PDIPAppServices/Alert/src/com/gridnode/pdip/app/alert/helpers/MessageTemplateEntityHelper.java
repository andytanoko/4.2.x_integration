/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertEntityHelper.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 09 2003    Koh Han Sing        Created
 */
package com.gridnode.pdip.app.alert.helpers;

import com.gridnode.pdip.app.alert.model.MessageTemplate;

import com.gridnode.pdip.base.exportconfig.helpers.ICheckConflict;

import com.gridnode.pdip.framework.db.entity.IEntity;
import com.gridnode.pdip.framework.db.filter.DataFilterImpl;

import java.util.Collection;

/**
 * This class contains utitlies methods for the MessageTemplate entity.
 *
 * @author Koh Han Sing
 *
 * @version 2.1 I1
 * @since 2.1 I1
 */

public class MessageTemplateEntityHelper implements ICheckConflict
{

  private static MessageTemplateEntityHelper _self = null;

  private MessageTemplateEntityHelper()
  {
    super();
  }

  public static MessageTemplateEntityHelper getInstance()
  {
    if(_self == null)
    {
      synchronized(MessageTemplateEntityHelper.class)
      {
        if (_self == null)
        {
          _self = new MessageTemplateEntityHelper();
        }
      }
    }
    return _self;
  }

  public IEntity checkDuplicate(IEntity template) throws Exception
  {
    AlertLogger.debugLog("MessageTemplateEntityHelper", "checkDuplicate", "Start");
    String templateName = template.getFieldValue(MessageTemplate.NAME).toString();
    DataFilterImpl filter = new DataFilterImpl();
    filter.addSingleFilter(null, MessageTemplate.NAME, filter.getEqualOperator(),
      templateName, false);

    MessageTemplateEntityHandler handler = MessageTemplateEntityHandler.getInstance();
    Collection results = handler.getEntityByFilterForReadOnly(filter);
    if (!results.isEmpty())
    {
      return (MessageTemplate)results.iterator().next();
    }
    return null;
  }

}