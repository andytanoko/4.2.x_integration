/**
 * This software is the proprietary information of GridNode Pte Ltd.
 * Use is subjected to license terms.
 *
 * Copyright 2001-2002 (C) GridNode Pte Ltd. All Rights Reserved.
 *
 * File: AlertEntityFieldID.java
 *
 ****************************************************************************
 * Date           Author              Changes
 ****************************************************************************
 * 2003-01-27     Daniel D'Cotta      Created
 * Apr 22 2003    Neo Sok Lay         Combine FieldIDs for different entities.
 * Jul 01 2004    Mahesh              Added FieldIDs for EmailConfig.
 * Jan 12 2006		SC									Added FieldIDs for MessageTemplate.
 */
package com.gridnode.gtas.model.alert;

import java.util.Hashtable;

/**
 * This class provides the fieldIDs of the entities in the Alert module.
 * The fieldIDs will be the ones that are available for the client to access.
 *
 * @author Daniel D'Cotta
 *
 * @version 2.1
 * @since 2.0
 */
public class AlertEntityFieldID
{
  private Hashtable _table;
  private static AlertEntityFieldID _self = null;

  private AlertEntityFieldID()
  {
    _table = new Hashtable();

    // Alert
    _table.put(IAlert.ENTITY_NAME,
      new Number[]
      {

        IAlert.UID,
        IAlert.NAME,
        IAlert.TYPE,
        IAlert.CATEGORY,
        IAlert.TRIGGER,
        IAlert.DESCRIPTION,
        IAlert.BIND_ACTIONS_UIDS,
      });

    // AlertType
    _table.put(IAlertType.ENTITY_NAME,
      new Number[]
      {

        IAlertType.UID,
        IAlertType.NAME,
        IAlertType.DESCRIPTION,
      });

    // Action
    _table.put(IAction.ENTITY_NAME,
      new Number[]
      {
        IAction.UID,
        IAction.NAME,
        IAction.DESCRIPTION,
        IAction.MSG_UID,
      });

    // AlertCategory
    _table.put(IAlertCategory.ENTITY_NAME,
      new Number[]
      {

        IAlertCategory.UID,
        IAlertCategory.CODE,
        IAlertCategory.NAME,
        IAlertCategory.DESCRIPTION,
      });

    // MessageTemplate
    _table.put(IMessageTemplate.ENTITY_NAME,
      new Number[]
      {
        IMessageTemplate.UID,
        IMessageTemplate.NAME,
        IMessageTemplate.CONTENT_TYPE,
        IMessageTemplate.MESSAGE_TYPE,
        IMessageTemplate.FROM_ADDR,
        IMessageTemplate.TO_ADDR,
        IMessageTemplate.CC_ADDR,
        IMessageTemplate.SUBJECT,
        IMessageTemplate.MESSAGE,
        IMessageTemplate.LOCATION,
        IMessageTemplate.APPEND,
        IMessageTemplate.JMS_DESTINATION,
        IMessageTemplate.MESSAGE_PROPERTIES,
      });

    // AlertTrigger
    _table.put(IAlertTrigger.ENTITY_NAME,
      new Number[]
      {
        IAlertTrigger.UID,
        IAlertTrigger.ALERT_UID,
        IAlertTrigger.ALERT_TYPE,
        IAlertTrigger.CAN_DELETE,
        IAlertTrigger.DOC_TYPE,
        IAlertTrigger.IS_ATTACH_DOC,
        IAlertTrigger.IS_ENABLED,
        IAlertTrigger.LEVEL,
        IAlertTrigger.PARTNER_GROUP,
        IAlertTrigger.PARTNER_ID,
        IAlertTrigger.PARTNER_TYPE,
        IAlertTrigger.RECIPIENTS,
      });

    // EmailConfig
    _table.put(IEmailConfig.ENTITY_NAME,
      new Number[]
      {
        IEmailConfig.SMTP_SERVER_HOST,
        IEmailConfig.SMTP_SERVER_PORT,
        IEmailConfig.AUTH_USER,
        IEmailConfig.AUTH_PASSWORD,
        IEmailConfig.RETRY_INTERVAL,
        IEmailConfig.MAX_RETRIES,
        IEmailConfig.SAVE_FAILED_EMAILS,
      });
    
    /* MessageProperty */
    _table.put(IMessageProperty.ENTITY_NAME, new Number[] {
				IMessageProperty.KEY, IMessageProperty.TYPE, IMessageProperty.VALUE
		});
    
    /* JmsDestination */
    _table.put(IJmsDestination.ENTITY_NAME, new Number[] {
				IJmsDestination.UID, IJmsDestination.NAME, IJmsDestination.TYPE,
				IJmsDestination.JNDI_NAME, IJmsDestination.DELIVERY_MODE,
				IJmsDestination.PRIORITY, IJmsDestination.CONNECTION_FACTORY_JNDI,
				IJmsDestination.CONNECTION_USER, IJmsDestination.CONNECTION_PASSWORD,
				IJmsDestination.LOOKUP_PROPERTIES, IJmsDestination.RETRY_INTERVAL,
				IJmsDestination.MAXIMUM_RETRIES
		});
  }

  public static Hashtable getEntityFieldID()
  {
    if (_self == null)
    {
      _self = new AlertEntityFieldID();
    }
    return _self._table;
  }
}