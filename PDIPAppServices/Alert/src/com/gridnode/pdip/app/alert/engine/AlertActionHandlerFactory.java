/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2003 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertActionHandlerFactory.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * Jun 20 2003    Neo Sok Lay         Created
 * Feb 28 2006    Neo Sok Lay         Use generic type for Hashtable.
 */
package com.gridnode.pdip.app.alert.engine;

import com.gridnode.pdip.app.alert.helpers.ActionEntityHandler;
import com.gridnode.pdip.app.alert.helpers.MessageTemplateEntityHandler;
import com.gridnode.pdip.app.alert.model.Action;
import com.gridnode.pdip.app.alert.model.AlertAction;
import com.gridnode.pdip.app.alert.model.MessageTemplate;

import java.util.Hashtable;

/**
 * Factory for creating an AlertActionHandler to handle the execution of
 * an AlertAction.
 * 
 * @author Neo Sok Lay
 * @since GT 2.1
 */
public final class AlertActionHandlerFactory
{
  private final Hashtable<String,IAlertActionHandler> _SPECIFIC_HANDLERS = new Hashtable<String,IAlertActionHandler>();
  private static final AlertActionHandlerFactory _self =
    new AlertActionHandlerFactory();

  /**
   * Get an instance of the AlertActionHandlerFactory.
   * 
   * @return An instance of the AlertActionHandlerFactory.
   */
  public static AlertActionHandlerFactory getInstance()
  {
    return _self;
  }

  private AlertActionHandlerFactory()
  {
    _SPECIFIC_HANDLERS.put(MessageTemplate.MSG_TYPE_EMAIL, new EmailActionHandler());
    _SPECIFIC_HANDLERS.put(MessageTemplate.MSG_TYPE_ALERT_LIST, new AlertlistActionHandler());
    _SPECIFIC_HANDLERS.put(MessageTemplate.MSG_TYPE_LOG, new LogActionHandler());
    _SPECIFIC_HANDLERS.put(MessageTemplate.MSG_TYPE_JMS, new JmsActionHandler());
  }

  /**
   * Get a handler to execute the specified AlertAction.
   * 
   * @param alertAction The AlertAction.
   * @return An handler suitable for executing the specified <code>alertAction</code>.
   * The returned value will not be <b>null</b>.
   */
  public IAlertActionHandler getHandlerFor(AlertAction alertAction)
    throws Throwable
  {
    MessageTemplate msg = getMessageTemplate(alertAction.getActionUid());

    IAlertActionHandler handler = null;
    String msgType = msg.getMessageType();

    handler = _SPECIFIC_HANDLERS.get(msgType);
    if (handler == null)
    {
      handler = new DefaultAlertActionHandler();
    }

    handler.setMessageTemplate(msg);

    return handler;
  }

  /**
   * Get the MessageTemplate linked to an Action.
   *
   * @param actionUid The UID of the Action.
   * @return MessageTemplate the <code>MessageTemplate</code> entity
   *
   * @exception thrown when an error occurs.
   */
  private MessageTemplate getMessageTemplate(Long actionUid) throws Throwable
  {
    MessageTemplate msg = null;
    Long msgUid =
      ((Action) ActionEntityHandler
        .getInstance()
        .getEntityByKeyForReadOnly(actionUid))
        .getMsgUid();
    msg =
      (MessageTemplate) MessageTemplateEntityHandler
        .getInstance()
        .getEntityByKeyForReadOnly(msgUid);

    return msg;
  }

}
